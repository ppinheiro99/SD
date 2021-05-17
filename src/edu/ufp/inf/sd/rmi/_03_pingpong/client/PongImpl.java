package edu.ufp.inf.sd.rmi._03_pingpong.client;

import edu.ufp.inf.sd.rmi._03_pingpong.server.Ball;
import edu.ufp.inf.sd.rmi._03_pingpong.server.PingRI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PongImpl extends UnicastRemoteObject implements PongRI {

    private PingRI pingRI;

    public PongImpl(PingRI pingRI)  throws RemoteException {
      super();
      this.pingRI=pingRI;
    }

    public void startPlay(Ball b) throws RemoteException {
        try {
            pingRI.ping(b,this);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void pong(Ball b) throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "pong(): ball "+b.getPlayerID());
        System.out.println("pong(): Receive ball"+b.getPlayerID());
        Random r = new Random();
        int dropball = r.nextInt(100)+1;
        System.out.println("dropball = "+ dropball);
        if(dropball > 20){
            int delay = r.nextInt(100)+1;
            try{
                Thread.currentThread().sleep(delay);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            this.pingRI.ping(b,this);
        }
    }
}
