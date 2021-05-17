package edu.ufp.inf.sd.rmi._05_observer.server;

import edu.ufp.inf.sd.rmi._05_observer.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {
    private State subjectState;
    private ArrayList<State> states = new ArrayList<>();
    private ArrayList<ObserverRI> observers = new ArrayList<>();


    public SubjectImpl() throws RemoteException {
        super();
        this.subjectState = new State("","");
    }

    // Methods

    public void notifyAllObservers() {
        for (ObserverRI obs: observers) {
            try {
                obs.update();
            }catch (RemoteException ex){
                Logger.getLogger(SubjectImpl.class.getName());
            }
        }
    }


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

    @Override
    public ArrayList<State> getAll() throws RemoteException{
        return this.states;
    }

}
