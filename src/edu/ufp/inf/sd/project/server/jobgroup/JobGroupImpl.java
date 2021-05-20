package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.rmi._05_observer.server.State;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectImpl;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;


public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    transient private static int nGroups = 0;




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

        SendJobs();
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

    public void receiveResults(String id , Integer makespan) {
        server_says("Getting the result from " + id);
        ///Se o nosso worker estiver associado ao jobgroup
        if(this.workers.containsKey(id)){
            ///Atualizamos o nosso hashmap de resultados(makespan)
            this.makespan.put(id,makespan);

        }

        server_says("Jobs Sent");
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
            //Falta adicionar ao saldo do user!!!!!

            this.workers.get(workerID).receiveJob(this.groupInfoState);
            this.workers.get(workerID).receiveCoins(1);

        }else {
            ///Entra aqui assim que as coins forem 10 , verificamos quem tem a melhor solução

            verify_winner();
            //Falta a parte de meter o saldo ao vencedor
        }

    }
    public void verify_winner() throws IOException {
        ///Começamos a verificar pela primeira posiçao
        int aux = 0;
        String winner="";
        //For each em que verificamos se o valor atual do ciclo é menor que o que temos guardado, caso seja , substituimos

        for(Map.Entry<String, Integer> entry : this.makespan.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
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
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException {

        // Already in the list.
        if(this.workers.containsValue(workerRI)){
            server_says(" Worker already on the list!");
            return null;
        }


        // Generate a new Unique ID
        server_says(" new worker detected. Generating new id ...");

        String newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 10 + 1);
        while(this.workers.containsKey(newID))
            newID = "worker_" + ThreadLocalRandom.current().nextInt(0, 10 + 1);

        server_says("worker id: " + newID);

        this.workers.put(newID, workerRI);
        workerRI.setId(newID);
        return this.groupInfoState;


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
