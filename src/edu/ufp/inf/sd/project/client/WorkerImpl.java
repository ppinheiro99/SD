package edu.ufp.inf.sd.project.client;

import com.rabbitmq.client.Channel;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    private String id;
    private Channel channel;
    private String user;
    protected final JobGroupRI jobGroupRI;


    public WorkerImpl(String user, JobGroupRI jobgroupRI) throws RemoteException {
        super();
        this.id = "";
        this.user = user;
        this.jobGroupRI = jobgroupRI;


    }

    @Override
    public void setId(String id) throws RemoteException {
        this.id = id;
        workerSays("new id received.");
    }

    @Override
    public void update() throws RemoteException {

    }

    private void workerSays(String msg) {
        System.out.println("[" + this.id + "] " + msg);
    }
}
