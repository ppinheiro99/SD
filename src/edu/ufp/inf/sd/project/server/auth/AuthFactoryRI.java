package edu.ufp.inf.sd.project.server.auth;

import edu.ufp.inf.sd.project.server.session.UserSessionRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthFactoryRI extends Remote {
    public Boolean registry(String username, String password) throws RemoteException;
    //public UserSessionRI login(String username, String password) throws RemoteException;
    public UserSessionRI login(String token) throws RemoteException;
}