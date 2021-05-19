package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.rmi._05_observer.server.State;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectImpl;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;


public class JobGroupImpl extends UnicastRemoteObject implements JobGroupRI {
    transient private static int nGroups = 0;

    private State subjectState;
    private ArrayList<State> states = new ArrayList<>();
    private ArrayList<WorkerRI> workers = new ArrayList<>();
    private ArrayList<Integer> makespan = new ArrayList<>();
    private GroupInfoState groupInfoState;
    private GroupStatusState groupStatusState;
    // JobGroup Info
    transient private int coins;
    private final int id;
    private final String name;
    private final String owner;
    private final String path;
    private static String nome ;
    private static Integer solucao ;

    //////////////////////////////////
    // Constructor
    public JobGroupImpl(int coins,String name, String owner,String path) throws RemoteException {
        super();
        this.coins = coins;
        this.id = nGroups++;
        this.name = name;
        this.owner = owner;
        this.path = path;
        this.groupStatusState = new GroupStatusState("CONTINUE");
        this.groupInfoState = new GroupInfoState(path,coins);

        this.nome = "";
        this.solucao = 999999999;
    }

    public ArrayList<WorkerRI> getWorkers() {
        return workers;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {

        this.coins = coins;
    }

    public  String getNome() {
        return nome;
    }

    public int getSolucao() {
        return solucao;
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

    public ArrayList<Integer> getMakespan() {
        return makespan;
    }

    // States
    //transient private GroupStatusState groupStatusState;
    //transient private final GroupInfoState groupInfoState;

    // DB
    //transient private final HashMap<String, WorkerRI> workers;



    public void addMakespan(int make, String nick){
        if(this.solucao > make ){
            this.solucao = make;
            this.nome = nick;
        }
        this.makespan.add(make);

    }

    public void verify_winner(){
        System.out.println("Nome do utilizador:" + this.nome + "Makespan:" + this.solucao + "\n");
    }


    public void notifyAllObservers() {
        for (WorkerRI work: workers) {
            try {
                work.update();
            }catch (RemoteException ex){
                Logger.getLogger(SubjectImpl.class.getName());
            }
        }
    }

    //////////////////////////////////
    // Methods RI
    @Override
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException {

        // Already in the list.
        if(this.workers.contains(workerRI))
            return null;

        // Generate a new Unique ID
        server_says(" new worker detected. Generating new id ...");


        this.workers.add(workerRI);

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
        this.notifyAllObservers();
    }




    private void server_says(String msg){
        System.out.println("[Server]: " + msg);
    }
}
