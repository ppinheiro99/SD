package edu.ufp.inf.sd.project.server.jobgroup;

import com.rabbitmq.client.*;
import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;


public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    private static final String QUEUE_NAME = "jssp_ga";
    transient private static int nGroups = 0;


    private ArrayList<String> filas = new ArrayList<String>();
    transient private final HashMap<String, WorkerRI> workers;
    private final HashMap<String, Integer> makespan;
    private GroupInfoState groupInfoState;
    private GroupStatusState groupStatusState;
    // JobGroup Info
    transient private int coins;
    private final int id;
    private final String name;
    private final String owner;
    private final ArrayList<String> ficheiros;
    private final String strat;
    private final Integer nrworkers;
    private String exchangeName;




    //////////////////////////////////
    // Constructor
    public JobGroupImpl(int coins,String name, String owner,ArrayList<String> path,String strat, String nrworkers) throws RemoteException {
        super();
        this.name = name;
        this.coins = coins;
        this.owner = owner;
        this.ficheiros = path;
        this.id = ++nGroups;
        this.strat = strat;
        this.nrworkers = Integer.parseInt(nrworkers);
        this.makespan = new HashMap<>();
        this.workers = new HashMap<>();
        this.exchangeName = "exchange_" + this.id + "_" + this.name;
        // GROUP STARTING STATUS
        this.groupStatusState = new GroupStatusState("CONTINUE");
        this.groupInfoState = new GroupInfoState(path,this.exchangeName);

        server_says("Ready and waiting");

    }


    public static void sendMessage(Channel channel, String message,String q) throws IOException {
        channel.basicPublish("", q, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
    }





    public void receiveResults(String id , int makespan) {
        server_says("Getting the result from " + id);
        ///Se o nosso worker estiver associado ao jobgroup
        if(this.workers.containsKey(id)){
            ///Atualizamos o nosso hashmap de resultados(makespan)
            this.makespan.put(id,makespan);

        }
        //a cada resultado recebido retiramos uma moeda, mas so pagamos na parte de anunciar vencedor
        this.coins--;
        server_says("Result Received");
        try {
            verify_winner();
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }



    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {

        this.coins = coins;
    }





    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }









    public void verify_winner() throws IOException, InterruptedException, TimeoutException {
        ///Começamos a verificar pela primeira posiçao

        //For each em que verificamos se o valor atual do ciclo é menor que o que temos guardado, caso seja , substituimos
        if (this.coins > 10){
            return ;
        }

        int aux = 0;
        String winner="";

        if(this.strat.compareTo("ga")==0){
            ConnectionFactory factory=new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            //factory.setPassword("guest4rabbitmq");

            /* try-with-resources\. will close resources automatically in reverse order... avoids finally */
            //Create a channel, which is where most of the API resides
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();
            //verificamos se todas as filas ja estao vazias

            for(int i= 0 ; i < filas.size();i++){
                AMQP.Queue.DeclareOk response = channel.queueDeclarePassive(filas.get(i)+"_results");
                if(response.getMessageCount()>0){
                    return;
                }
            }

            //se a estrategia for ga , damos um sleep de 6000milis , para termos tempo de receber os resultados

            for(Map.Entry<String, Integer> entry : this.makespan.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                //Cada worker recebe 1 pelo trabalho

                this.workers.get(entry.getKey()).receiveCoins(1);
                if(aux == 0){
                    aux = value;
                    winner = key;

                }
                if(value < aux){

                    aux = value;
                    winner = key;

                }
            }


            ///Depois de encontrado a melhor soluçao:

            //Mudamos o estado do jobgroup para CONCLUIDO
            this.groupStatusState.setStatus("MATCH_FOUND");
            //Enviamos notificaçao a todos os workers da solução
            String message = "Winner foi:" + winner + "Makespan:" + aux + "\n";



            /* We must declare a queue to send to; this i
            s idempotent, i.e.,
            it will only be created if it doesn't exist already;
            then we can publish a message to the queue; The message content is a
            byte array (can encode whatever we need). */

                channel.queueDeclare(id+this.name, false, false, false, null);
                channel.exchangeDeclare(this.exchangeName, "direct");
                channel.queueBind(id+this.name, this.exchangeName, id+this.name);

            for(int i= 0 ; i < filas.size();i++){
                channel.basicPublish(this.exchangeName, filas.get(i), null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
                channel.queueDelete(filas.get(i));
            }

            //plafon a 0
            this.coins = 0;
            this.workers.get(winner).receiveCoins(10);
            System.out.println();
            //Fazemos detach de todos os workers
            this.workers.forEach((id, workerRI) -> {
                try {
                    this.detach(workerRI);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }else {

            for(Map.Entry<String, Integer> entry : this.makespan.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();



                 //int media = (int) value.stream().mapToInt(val -> val).average().orElse(0.0);

                this.workers.get(entry.getKey()).receiveCoins(1);
                if(aux == 0){
                    aux = value;
                    winner = key;

                }
                if(value < aux){
                    aux = value;
                    winner = key;

                }
            }


            ///Depois de encontrado a melhor soluçao:

            //Mudamos o estado do jobgroup para CONCLUIDO
            this.groupStatusState.setStatus("MATCH_FOUND");
            //Enviamos notificaçao a todos os workers da solução
            notifyAllObservers("Nome do utilizador:" + winner + "Makespan:" + aux + "\n");
            //plafon a 0
            this.coins = 0;
            this.workers.get(winner).receiveCoins(10);
            System.out.println("Winner foi:" + winner + "Makespan:" + aux + "\n");
            //Fazemos detach de todos os workers
            this.workers.forEach((id, workerRI) -> {
                try {
                    this.detach(workerRI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }


    public void notifyAllObservers(String msg) {
        this.workers.forEach((id, workerRI) -> {
            try {
                workerRI.update(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    //////////////////////////////////
    // Methods RI
    @Override
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException {
        if(this.strat.compareTo("ts")==0){
            // Already in the list.
            if(this.workers.containsValue(workerRI) || this.groupStatusState.getStatus().compareTo("PAUSE")==0 || this.groupStatusState.getStatus().compareTo("MATCH_FOUND") == 0){
                server_says(" Worker already on the list! or state is paused or match_found!");
                return null;
            }
            ///Se for >10 podemos dar attach , senao temos q verificar o winner
            if(this.coins > 10){
                // Generate a new Unique ID
                server_says(" new worker detected. Generating new id ...");

                String newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 1000 + 1);
                while(this.workers.containsKey(newID))
                    newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 1000 + 1);

                server_says("worker id: " + newID);

                this.workers.put(newID, workerRI);
                workerRI.setId(newID);

                check_nrworkers();
                return this.groupInfoState;
            }else {
                try {
                    verify_winner();
                } catch (IOException | InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }else {
            // Already in the list.
            if(this.workers.containsValue(workerRI) || this.groupStatusState.getStatus().compareTo("PAUSE")==0 || this.groupStatusState.getStatus().compareTo("MATCH_FOUND") == 0){
                server_says(" Worker already on the list! or state is paused or match_found!");
                return null;
            }
            ///Se for >10 podemos dar attach , senao temos q verificar o winner
            if(this.coins > 10){
                // Generate a new Unique ID
                server_says(" new worker detected. Generating new id ...");

                String newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 1000 + 1);
                while(this.workers.containsKey(newID))
                    newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 1000 + 1);

                server_says("worker id: " + newID);

                this.workers.put(newID, workerRI);
                workerRI.setId(newID);
                //Antes de darmos o trabalho ao worker , temos que abrir o canal de comunicaçao , chamando o producer
                this.coins--;

                //this.workers.get(newID).receiveJob(this.groupInfoState);
                return this.groupInfoState;
            }else {
                try {
                    verify_winner();
                } catch (IOException | InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }






    }

    private void check_nrworkers() {
        ///Se os workers attached forem maiores ou iguais que o nr de workers minimo , arrancamos o algoritmo

        if(this.workers.size() >= nrworkers){
            notifyAllObservers("The server has a task for you");
            for(Map.Entry<String, WorkerRI> entry : this.workers.entrySet()) {
                String key = entry.getKey();
                //Se o worker ainda nao tiver recebido o trabalho , entao enviamos


                try {
                    this.workers.get(key).receiveJob(this.groupInfoState);

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

        }
        return;
    }


    @Override
    public void detach(WorkerRI workerRI) throws RemoteException{
        System.out.println("\nDetach user ..." + workerRI.getUser());
        workers.remove(workerRI);

    }

    @Override
    public GroupStatusState getState() throws RemoteException {
        System.out.println("\nGet state ...");
        return this.groupStatusState;
    }

    @Override
    public void setState(GroupStatusState s) throws RemoteException{
        System.out.println("\nSet state ...");
        this.groupStatusState = s;

        this.notifyAllObservers(this.groupStatusState.getStatus());
    }




    private void server_says(String msg){
        System.out.println("[Server]: " + msg);
    }
}
