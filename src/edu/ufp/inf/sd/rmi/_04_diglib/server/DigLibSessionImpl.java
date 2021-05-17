package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: UFP </p>
 * @author Rui S. Moreira
 * @version 3.0
 */
public class DigLibSessionImpl extends UnicastRemoteObject implements DigLibSessionRI {

    private User user;
    private DBMockup db;
    private DigLibFactoryImpl digLibFactory;

    public DigLibSessionImpl(DigLibFactoryImpl digLibFactory, User user) throws RemoteException {
        super();
        this.user = user;
        this.digLibFactory = digLibFactory;
        this.db = this.digLibFactory.getDb();
    }

    public User getUser() {
        return user;
    }

    public DigLibFactoryImpl getDigLibFactory() {
        return digLibFactory;
    }

    @Override
    public void returnBook(String title, String author) throws RemoteException{
        System.out.println(user.toString() + " called: returnBook() - ["+title + "; " + author +"];");

        Book b = this.db.findBook(title, author);

        if(b == null || !b.isTaken() || b.getTakenBy().getUname().compareTo(user.getUname()) != 0)
            return;

        b.setIsTaken(false);
        b.setTakenBy(null);
    }

    @Override
    public Book[] searchBooks(String t, String a) throws RemoteException {
        return db.select(t,a);
    }

    @Override
    public void logout() throws RemoteException {
        System.out.println(user.toString() + " called: logout().");

        this.digLibFactory.getSessions().remove(this.user.getUname());

    }
}