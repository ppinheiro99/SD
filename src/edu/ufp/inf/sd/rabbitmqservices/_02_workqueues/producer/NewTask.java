/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufp.inf.sd.rabbitmqservices._02_workqueues.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import edu.ufp.inf.sd.rabbitmqservices.util.RabbitUtils;

/**
 * Example from RabbitMQ site:
 * https://www.rabbitmq.com/tutorials/tutorial-two-java.html
 *
 * Create a Work Queue (aka: Task Queue) that will be used to distribute
 * time-consuming tasks among multiple workers. Task Queues avoid doing a
 * resource-intensive task immediately and wait for it to complete. Instead
 * we schedule the task to be done later.
 * Encapsulate a task as a message and send it to a queue. A worker process
 * running in background will pop the tasks and eventually execute the job.
 * When running many workers, tasks will be shared between them.
 * This concept is especially useful in web apps where it is impossible to
 * handle a complex task during a short HTTP request time-window.
 *
 * We could send strings that stand for complex tasks (e.g. images to be resized
 * or pdf files to be rendered). Instead we fake tasks with Thread.sleep(1000);
 * for every dot on the message string, e.g., a fake task "Hello..." will
 * take 3 seconds.
 *
 * @author rui
 *
 */
public class NewTask {

    // Name of the queue
    //public final static String TASK_QUEUE_NAME = "task_queue";

    /**
     * Allow arbitrary messages to be sent from the command line.
     * @param args
     */
    public static void main(String[] args) throws Exception {
        RabbitUtils.printArgs(args);

        //Read args passed via shell command
        String host=args[0];
        int port=Integer.parseInt(args[1]);
        String queueName=args[2];

        //try-with-resources
        try (Connection connection=RabbitUtils.newConnection2Server(host, port, "guest", "guest");
             Channel channel=RabbitUtils.createChannel2Server(connection)) {
            /* We must declare a queue to send to (this is idempotent, i.e. it will only be created if it doesn't exist;
               then we can publish a message to the queue;
               The message content is a byte array (can encode whatever we need).
               The previous queue was not Durable... persistent */
            //channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

            /* Declare a queue as Durable (queue won't be lost even if RabbitMQ restarts);
               RabbitMQ does not allow redefine an existing queue with different parameters (hence create a new one) */
            boolean durable=true;
            channel.queueDeclare(queueName, durable, false, false, null);

            //String message = "Hello...";
            //Receive message to send via argv[3]
            String message=RabbitUtils.getMessage(args, 3);

            /* To avoid loosing queues when rabbitmq crashes, mark messages as persistent by setting
             MessageProperties (which implements BasicProperties) to value PERSISTENT_TEXT_PLAIN. */
            //channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        /* Lastly, we close the channel and the connection... not anymore since try-with-resources closes resources! */
        //channel.close();
        //connection.close();
    }
}
