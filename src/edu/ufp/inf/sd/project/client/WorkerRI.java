package edu.ufp.inf.sd.project.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRI extends Remote {
    public void update() throws RemoteException;
}
