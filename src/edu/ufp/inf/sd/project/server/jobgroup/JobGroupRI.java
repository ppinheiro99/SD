package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.rmi._05_observer.server.State;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobGroupRI extends Remote {
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException;

    public void detach(WorkerRI workerRI) throws RemoteException;

    public void setState(State s) throws RemoteException;

    public State getState() throws RemoteException;

    public int getId() throws RemoteException;

}
