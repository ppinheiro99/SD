package edu.ufp.inf.sd.project.server.jobgroup;

import edu.ufp.inf.sd.project.client.WorkerRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.rmi._05_observer.server.State;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface JobGroupRI extends Remote {
    public GroupInfoState attach(WorkerRI workerRI) throws RemoteException;

    public void detach(WorkerRI workerRI) throws RemoteException;

    public void setState(GroupStatusState s) throws RemoteException;

    public GroupStatusState getState() throws RemoteException;

    public int getId() throws RemoteException;

    public int getCoins() throws RemoteException;
    public void setCoins(int coins) throws RemoteException;
    public ArrayList<Integer> getMakespan() throws RemoteException;
    public int getSolucao() throws RemoteException;
    public void verify_winner() throws RemoteException;

    public String getNome() throws RemoteException;
    public void addMakespan(int make, String nick) throws RemoteException;
    public ArrayList<WorkerRI> getWorkers() throws RemoteException;

}
