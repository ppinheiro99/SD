package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobGroupRI extends Remote {
    public void attach(WorkerRI workerRI) throws IOException;

    public void detach(WorkerRI workerRI) throws RemoteException;

    public void setState(GroupStatusState s) throws RemoteException;

    public GroupStatusState getState() throws RemoteException;

    public int getId() throws RemoteException;

    public int getCoins() throws RemoteException;
    public void setCoins(int coins) throws RemoteException;
    public void receiveResults(String id , Integer makespan) throws IOException;

    public void verify_winner() throws IOException;

    public void askForJob(String workerID) throws RemoteException, IOException;


    public void SendJobs() throws RemoteException;

}
