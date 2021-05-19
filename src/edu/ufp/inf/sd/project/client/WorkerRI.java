package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.server.states.GroupInfoState;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    public void update(String msg) throws RemoteException;
    public String getUser() throws RemoteException;

    public void receiveJob(GroupInfoState groupInfoState) throws IOException;

    public void setId(String id) throws RemoteException;
}
