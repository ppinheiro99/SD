package edu.ufp.inf.sd.project.server.jobgroup;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.producer.Producer;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.util.geneticalgorithm.CrossoverStrategies;




import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    transient private static int nGroups = 0;
    public final static String QUEUE_NAME="jssp_ga";



    transient private final HashMap<String, WorkerRI> workers;
    private final HashMap<String, Integer> makespan;
    private GroupInfoState groupInfoState;
    private GroupStatusState groupStatusState;

    // JobGroup Info
    transient private int coins;

    private final int id;
    private final String name;
    private final String owner;
    private final String path;
    private Channel channel;


    //////////////////////////////////
    // Constructor
    public JobGroupImpl(int coins,String name, String owner,String path) throws RemoteException {
        super();
        this.name = name;
        this.coins = coins;
       
        this.owner = owner;
        this.path = path;
        this.id = ++nGroups;
        this.makespan = new HashMap<>();
        this.workers = new HashMap<>();


        // GROUP STARTING STATUS
        this.groupStatusState = new GroupStatusState("CONTINUE");
        this.groupInfoState = new GroupInfoState(path);

        //rabbitmq();
        SendJobs();

    }

    private void rabbitmq(){
        //Connection connection=null;
        //Channel channel=null;

        /* Create a connection to the server (abstracts the socket connection,
           protocol version negotiation and authentication, etc.) */
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //factory.setPassword("guest4rabbitmq");

        /* try-with-resources\. will close resources automatically in reverse order... avoids finally */
        try (//Create a channel, which is where most of the API resides
             Connection connection=factory.newConnection();
             Channel channel=connection.createChannel()
        ) {
            /* We must declare a queue to send to; this is idempotent, i.e.,
            it will only be created if it doesn't exist already;
            then we can publish a message to the queue; The message content is a
            byte array (can encode whatever we need). */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            // Change strategy to CrossoverStrategies.TWO
            sendMessage(channel, this.path);
            Thread.currentThread().sleep(2000);

            // Change strategy to CrossoverStrategies.THREE
            sendMessage(channel, String.valueOf(CrossoverStrategies.THREE.strategy));
            Thread.currentThread().sleep(2000);

            // Stop the GA
            //sendMessage(channel, "stop");

        } catch (IOException | TimeoutException | InterruptedException e) {
            Logger.getLogger(Producer.class.getName()).log(Level.INFO, e.toString());
        } /* The try-with-resources will close resources automatically in reverse order
            finally {
            try {
                // Lastly, we close the channel and the connection
                if (channel != null) { channel.close(); }
                if (connection != null) { connection.close(); }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */
    }

    public static void sendMessage(Channel channel, String message) throws IOException {
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
    }



    public void SendJobs() {
        server_says("Sending the jobs");

        ///Mandamos os detalhes do job a cada worker
        this.workers.forEach((id, workerRI) -> {
            try {
                ///Antes de enviarmos jobs para cada worker temos que verificar que temos plafon suficiente.
                if(this.coins > 10){
                    //Enviamos o job e ele executa
                    //Falta adicionar ao saldo do user!!!!!
                    this.coins--;
                    workerRI.receiveJob(this.groupInfoState);
                }else {
                    ///Entra aqui assim que as coins forem 10 , verificamos quem tem a melhor solução
                    verify_winner();
                    //Falta a parte de meter o saldo ao vencedor
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        server_says("Jobs Sent");
    }

    public void receiveResults(String id , Integer makespan) throws IOException {
        server_says("Getting the result from " + id);
        ///Se o nosso worker estiver associado ao jobgroup
        if(this.workers.containsKey(id)){
            ///Atualizamos o nosso hashmap de resultados(makespan)
            this.makespan.put(id,makespan);
        }

        server_says("Result Received");
        verify_winner();
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








    public void askForJob(String workerID) throws IOException {
        server_says("Worker asked for a job");

        if(getCoins() > 10){
            //Enviamos o job e ele executa
            this.coins--;


            this.workers.get(workerID).receiveJob(this.groupInfoState);

            this.workers.get(workerID).receiveCoins(1);

        }else {
            ///Entra aqui assim que as coins forem 10 , verificamos quem tem a melhor solução

            verify_winner();

        }

    }
    public void verify_winner() throws IOException {
        //se for maior que 10 , podemos continuar a trabalhar , logo nao faz nada
        if(this.coins > 10){
            return;
        }
        ///Começamos a verificar pela primeira posiçao
        int aux = 0;
        String winner="";
        //For each em que verificamos se o valor atual do ciclo é menor que o que temos guardado, caso seja , substituimos

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
        notifyAllObservers("Nome do utilizador:" + winner + "Makespan:" + aux + "\n");
        //Damos reset ao plafon
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
    public void attach(WorkerRI workerRI) throws IOException {

        // Already in the list.
        if(this.workers.containsValue(workerRI) || this.groupStatusState.getStatus().compareTo("PAUSE")==0 || this.groupStatusState.getStatus().compareTo("MATCH_FOUND")==0){
            server_says(" Worker already on the list! or Jobgroup paused or match found");
            return ;
        }
        //Se ainda tivermos plafon , podemos associar um novo worker
        if(this.coins >10){
            // Generate a new Unique ID
            server_says(" new worker detected. Generating new id ...");

            String newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 10 + 1);
            while(this.workers.containsKey(newID))
                newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 10 + 1);

            server_says("worker id: " + newID);

            this.workers.put(newID, workerRI);
            workerRI.setId(newID);
            ///Manda a tarefa ao worker
            workerRI.receiveJob(this.groupInfoState);
        }else {
            verify_winner();
        }




    }



    @Override
    public void detach(WorkerRI workerRI) throws RemoteException{
        System.out.println("\nDetach user ...");
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
        //this.notifyAllObservers();
    }




    private void server_says(String msg){
        System.out.println("[Server]: " + msg);
    }
}
