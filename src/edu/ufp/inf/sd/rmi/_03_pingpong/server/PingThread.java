package edu.ufp.inf.sd.rmi._03_pingpong.server;

import edu.ufp.inf.sd.rmi._03_pingpong.client.PongRI;

import java.rmi.RemoteException;

public class PingThread extends Thread{
    private Ball b;
    private PongRI p;

    public PingThread(Ball b, PongRI p) {
        this.b = b;
        this.p = p;
    }

    public void run(){
        try {
            p.pong(b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
