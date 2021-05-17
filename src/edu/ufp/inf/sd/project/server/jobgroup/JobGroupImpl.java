package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.rmi._05_observer.client.ObserverRI;
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
    private ArrayList<ObserverRI> observers = new ArrayList<>();

    // JobGroup Info
    transient private int coins;
    private final int id;
    private final String name;
    private final String owner;

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

// States
    //transient private GroupStatusState groupStatusState;
    //transient private final GroupInfoState groupInfoState;

    // DB
    //transient private final HashMap<String, WorkerRI> workers;


    //////////////////////////////////
    // Constructor
    public JobGroupImpl(int coins,String name, String owner) throws RemoteException {
        super();
        this.coins = coins;
        this.id = nGroups++;
        this.name = name;
        this.owner = owner;
        this.subjectState = new State("","");
    }

    public void notifyAllObservers() {
        for (ObserverRI obs: observers) {
            try {
                obs.update();
            }catch (RemoteException ex){
                Logger.getLogger(SubjectImpl.class.getName());
            }
        }
    }

    //////////////////////////////////
    // Methods RI
    @Override
    public void attach(ObserverRI observerRI) throws RemoteException{
        System.out.println("\nAttached user ...");
        observers.add(observerRI);
    }

    @Override
    public void detach(ObserverRI observerRI) throws RemoteException{
        System.out.println("\nDetach user ...");
        observers.remove(observerRI);
    }

    @Override
    public State getState() throws RemoteException {
        System.out.println("\nGet state ...");
        return this.subjectState;
    }



    @Override
    public void setState(State s) throws RemoteException{
        System.out.println("\nSet state ...");
        this.subjectState = s;
        this.states.add(s);
        this.notifyAllObservers();
    }

    public void delete() {
    }


}
