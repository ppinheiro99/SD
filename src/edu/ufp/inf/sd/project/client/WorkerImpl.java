package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.consumer.Consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import edu.ufp.inf.sd.project.producer.Producer;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.states.GroupInfoState;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.util.geneticalgorithm.CrossoverStrategies;
import edu.ufp.inf.sd.project.util.geneticalgorithm.GeneticAlgorithmJSSP;
import edu.ufp.inf.sd.project.util.tabusearch.TabuSearchJSSP;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeoutException;
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
        testerabbit();


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
                algoritmo(message,this.id+jobGroupRI.getName());
    };
    channel.basicConsume(this.id+jobGroupRI.getName(), true, deliverCallback, consumerTag -> { });



}
    public void rabbito() {
        try {
            /* Open a connection and a channel, and declare the queue from which to consume.
            Declare the queue here, as well, because we might start the client before the publisher. */
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            //Use same username/passwd as the for accessing Management UI @ http://localhost:15672/
            //Default credentials are: guest/guest (change accordingly)
            factory.setUsername("guest");
            factory.setPassword("guest");
            //factory.setPassword("guest4rabbitmq");
            Connection connection=factory.newConnection();
            Channel channel=connection.createChannel();

            String resultsQueue = "PILA";

            channel.queueDeclare(resultsQueue, false, false, false, null);

            //channel.queueDeclare(Producer.QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            /* The server pushes messages asynchronously, hence we provide a
            DefaultConsumer callback that will buffer the messages until we're ready to use them.
            Consumer client = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message=new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(Producer.QUEUE_NAME, true, client    );
            */

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

                System.out.println(message + " " + resultsQueue + " " + CrossoverStrategies.ONE);
                algoritmo(message,resultsQueue);
            };

            channel.basicConsume(resultsQueue, true, deliverCallback, consumerTag -> { });

        } catch (Exception e){
            //Logger.getLogger(Recv.class.getName()).log(Level.INFO, e.toString());
            e.printStackTrace();
        }
    }
    public void algoritmo(String message,  String qeue ){
        GeneticAlgorithmJSSP ga = new GeneticAlgorithmJSSP(message,qeue,CrossoverStrategies.TWO);
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
        Integer makespan = processJob(groupInfoState.getPath());
        jobGroupRI.receiveResults(this.id,makespan);

    }

    public void receiveCoins(Integer coins) throws IOException {
       //Recebemos as coins e mandamos para o nosso user
        this.jobs.getCoinsPayment(coins);

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
        workerSays("Job Done! Makespan was :" + makespan);
        return makespan;
    }

    /*
     * Check JobGroup Status (PAUSE, CONTINUE, MATCH_FOUND).
     */
    private void checkGroupStatus() throws IOException {

        // Get status from server.
        this.groupStatus = this.jobGroupRI.getState();

        workerSays("The Status of JobGroup is: " + this.groupStatus.getStatus());
        if(this.groupStatus.getStatus().compareTo("CONTINUE")==0)
            this.jobGroupRI.askForJob(this.id);

    }
}
