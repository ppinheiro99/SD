package edu.ufp.inf.sd.rmi._05_observer.client;

import edu.ufp.inf.sd.rmi._05_observer.server.State;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {
    private String username;
    private State lastObserverState;
    protected SubjectRI subjectRI;
    protected ObserverGuiClient chatFrame;

    public ObserverImpl(String username, ObserverGuiClient observerGuiClient, SubjectRI subjectRI) throws RemoteException {
        super();
        this.username = username;
        this.lastObserverState = new State(username,"");
        this.chatFrame = observerGuiClient;
        this.subjectRI = subjectRI;
    }

    @Override
    public void update() throws RemoteException {
        lastObserverState = subjectRI.getState();
        chatFrame.updateTextArea();
    }

    public void setLastObserverState(State state)  {
        this.lastObserverState = state;
    }

    protected State getLastObserverState(){
        return this.lastObserverState;
    }

    @Override
    public String getUsername() throws RemoteException{
        return this.username;
    }

    public void setUsername(String username) throws RemoteException{
        this.username = username;
    }
}
