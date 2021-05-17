package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.rmi._05_observer.client.ObserverRI;
import edu.ufp.inf.sd.rmi._05_observer.server.State;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobGroupRI extends Remote {
    public void attach(ObserverRI observerRI) throws RemoteException;

    public void detach(ObserverRI observerRI) throws RemoteException;

    public void setState(State s) throws RemoteException;

    public State getState() throws RemoteException;

}
