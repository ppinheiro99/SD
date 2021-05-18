package edu.ufp.inf.sd.project.client;

import com.rabbitmq.client.Channel;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.util.tabusearch.TabuSearchJSSP;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {
    private String id;
    private Channel channel;
    private String user;
    protected final JobGroupRI jobGroupRI;
    private String jsspInstance;


    public WorkerImpl(String user, JobGroupRI jobgroupRI,String jsspInstance) throws RemoteException {
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

    private void workerSays(String msg) {
        System.out.println("[" + this.id + "] " + msg);
    }

    /*
     * Process and do the job received from jobGroup.
     */
    private int processJob() throws IOException {

        String jsspInstancePath = "edu/ufp/inf/sd/project/data/la01.txt";
        TabuSearchJSSP ts = new TabuSearchJSSP(jsspInstancePath);
        int makespan = ts.run();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "[TS] Makespan for {0} = {1}", new Object[]{jsspInstancePath,String.valueOf(makespan)});

        return makespan;
    }
}
