package edu.ufp.inf.sd.project.server.session;

import edu.ufp.inf.sd.project.server.user.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserSessionRI extends Remote {
    // Session
    public void logout() throws RemoteException;
    // User
    public int showCoins() throws RemoteException;
    public void addCoins(int coins) throws RemoteException;
    public ArrayList<User> listUsers() throws RemoteException;
    public String showMyUsername() throws RemoteException;

}
