package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.consumer.Consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.util.geneticalgorithm.CrossoverStrategies;
import edu.ufp.inf.sd.project.util.geneticalgorithm.GeneticAlgorithmJSSP;
import edu.ufp.inf.sd.project.util.tabusearch.TabuSearchJSSP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerImpl extends UnicastRemoteObject implements WorkerRI {


    private String id;
    private String user;
    protected final JobGroupRI jobGroupRI;
    private GroupInfoState groupInfoState;
    private GroupStatusState groupStatus;
    private JobShopClient jobs;
    private Consumer c = new Consumer();

    public WorkerImpl(String user, JobGroupRI jobgroupRI, JobShopClient j) throws IOException, TimeoutException {
        super();
        this.id = "";
        this.user = user;
        this.jobGroupRI = jobgroupRI;
        this.jobs = j;
        this.groupInfoState = this.jobGroupRI.attach(this);




    //rabbito();
      //  testerabbit();


    }
    public static void main(String[] args){}

public void testerabbit() throws IOException, TimeoutException {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(groupInfoState.getExchangeName(),"direct");
    //String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(this.id+jobGroupRI.getName(), groupInfoState.getExchangeName(), this.id+jobGroupRI.getName());

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");

        System.out.println(" [x] Received '" +
                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            if(message.compareTo("1")!=0 || message.compareTo("2")!=0 || message.compareTo("3")!=3 || message.compareTo("stop")!=0){

                algoritmo(message,this.id+jobGroupRI.getName(),CrossoverStrategies.ONE);
            }




    };
    channel.basicConsume(this.id+jobGroupRI.getName(), true, deliverCallback, consumerTag -> { });




}

    public void algoritmo(String message,  String qeue , CrossoverStrategies strat){
        GeneticAlgorithmJSSP ga = new GeneticAlgorithmJSSP(message,qeue, strat);
        ga.run();

    }

    public String getUser() {
        return user;
    }

    @Override
    public void setId(String id) throws RemoteException {
        this.id = id;
    }

    @Override
    public void update(String msg) throws RemoteException {
        jobs.print_msg(msg);
    }

    public void receiveJob(GroupInfoState groupInfoState) throws IOException {
        //Recebemos o nosso job a desempenhar , executamos e enviamos  o resultado para o jobgroup
        int makespan = processJob(groupInfoState.getPath());
        jobGroupRI.receiveResults(this.id,makespan);

    }

    public void receiveCoins(Integer coins) throws IOException {
       //Recebemos as coins e mandamos para o nosso user
        this.jobs.getCoinsPayment(coins);

    }
    private void workerSays(String msg) {
        System.out.println( msg);
    }


    //###############String path = "edu/ufp/inf/sd/project/data/la04.txt";######################
    /*
     * Process and do the job received from jobGroup.
     */
    private int processJob(ArrayList<String> ficheiros) throws IOException {
        File pasta = new File("edu/ufp/inf/sd/project/data/workers/"+this.getUser()+"/");
        ArrayList<Integer> makespans = new ArrayList<>();
        if (!pasta.exists()) pasta.mkdirs();


        for (String ficheiro: ficheiros) {
            int indice = makespans.size()+1;
            String jsspInstancePath ="edu/ufp/inf/sd/project/data/workers/"+this.getUser()+"/ficheiro_recevido_"+indice+".txt";
            File file = new File(jsspInstancePath);
            try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
                out.print(ficheiro);
                out.flush();
                out.close();
            }



       /* System.out.println("ficheiro recebido:");
        System.out.println(ficheiro);
        System.out.println(jsspInstancePath);*/

        /*
        //Create the file
        if (file.createNewFile())
        {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }
*/

        try (PrintStream out = new PrintStream(new FileOutputStream(file))) {
            out.print(ficheiro);
            out.flush();
            out.close();
        }


        TabuSearchJSSP ts = new TabuSearchJSSP(jsspInstancePath);
        int makespan = ts.run();
        makespans.add(makespan);

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "[TS] Makespan for {0} = {1}", new Object[]{jsspInstancePath,String.valueOf(makespan)});

        }

        int makespan = (int) makespans.stream().mapToInt(val -> val).average().orElse(0.0);


        workerSays("Job Done! Makespan average was :" + makespan);


        return makespan;
    }

    /*
     * Check JobGroup Status (PAUSE, CONTINUE, MATCH_FOUND).
     */
    private void checkGroupStatus() throws IOException {

        // Get status from server.
        this.groupStatus = this.jobGroupRI.getState();

        workerSays("The Status of JobGroup is: " + this.groupStatus.getStatus());
        //if(this.groupStatus.getStatus().compareTo("CONTINUE")==0)
            //this.jobGroupRI.askForJob(this.id);

    }
}
