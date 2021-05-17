package edu.ufp.inf.sd.rmi._03_pingpong.server;

import edu.ufp.inf.sd.rmi._03_pingpong.client.PongRI;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @author Rui S. Moreira
 * @version 3.0
 */
public class PingImpl extends UnicastRemoteObject implements PingRI { //so tem univcaste se forem ser registadas no rmi resgistry

    public PingImpl() throws RemoteException {
        super(); //cria no rmi
    }

    @Override
    public void ping(Ball b, PongRI pongRI) throws RemoteException {
       // Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Ping {0}", new Object[]{b.getPlayerID()});
       // pongRI.pong(b);
        System.out.println(b.getPlayerID());
        PingThread pt = new PingThread(b,pongRI);
        pt.start();
    }

}