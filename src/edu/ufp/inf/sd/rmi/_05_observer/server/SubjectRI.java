package edu.ufp.inf.sd.rmi._05_observer.server;

import edu.ufp.inf.sd.rmi._05_observer.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SubjectRI extends Remote {

    public void attach(ObserverRI observerRI) throws RemoteException;

    public void detach(ObserverRI observerRI) throws RemoteException;

    public void setState(State s) throws RemoteException;

    public State getState() throws RemoteException;

    public ArrayList<State> getAll() throws RemoteException;
}
