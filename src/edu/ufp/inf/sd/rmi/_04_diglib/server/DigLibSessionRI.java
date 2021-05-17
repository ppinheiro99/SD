package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2021</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface DigLibSessionRI extends Remote {
    public void returnBook(String title, String author) throws RemoteException;
    public Book[] searchBooks(String t, String a) throws RemoteException;
    public void logout() throws RemoteException;

}
