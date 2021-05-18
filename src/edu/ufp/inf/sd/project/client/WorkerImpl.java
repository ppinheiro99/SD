package edu.ufp.inf.sd.project.client;


import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatus;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
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

    private final GroupStatusState groupStatusState;

    private String user;
    protected final JobGroupRI jobGroupRI;
    private GroupInfoState groupInfoState;
    private JobShopClient jobs;

    public WorkerImpl(String user, JobGroupRI jobgroupRI, JobShopClient j) throws IOException {
        super();
        this.user = user;
        this.jobGroupRI = jobgroupRI;
        this.groupStatusState = new GroupStatusState(GroupStatus.CONTINUE);
        this.groupInfoState = this.jobGroupRI.attach(this);
        this.jobGroupRI.addMakespan(processJob(groupInfoState.getPath()));
        this.jobs = j;
    }



    @Override
    public void update() throws RemoteException {
         ArrayList<Integer> aux =  jobGroupRI.getMakespan();
         jobs.print_makespan(aux);
    }

    private void workerSays(String msg) {
        System.out.println( msg);
    }

    /*
     * Process and do the job received from jobGroup.
     */
    private int processJob(String jsspInstancePath) throws IOException {


        TabuSearchJSSP ts = new TabuSearchJSSP(jsspInstancePath);
        int makespan = ts.run();

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "[TS] Makespan for {0} = {1}", new Object[]{jsspInstancePath,String.valueOf(makespan)});
        System.out.println("grande makespan" + makespan) ;
        return makespan;
    }
}
