package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

public interface JobGroupRI extends Remote {
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException;

    public void detach(WorkerRI workerRI) throws RemoteException;

    public void setState(GroupStatusState s) throws RemoteException;

    public GroupStatusState getState() throws RemoteException;

    public int getId() throws RemoteException;
    public String getName() throws RemoteException;
    public int getCoins() throws RemoteException;
    public String getOwner() throws RemoteException;
    public void setCoins(int coins) throws RemoteException;
    public void receiveResults(String id , int makespan) throws RemoteException;

    public void verify_winner() throws IOException, InterruptedException, TimeoutException;





}
