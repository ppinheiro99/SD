package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class DigLibFactoryImpl extends UnicastRemoteObject implements DigLibFactoryRI {

    private final DBMockup db = new DBMockup();
    private final HashMap<String, DigLibSessionRI> sessions = new HashMap<>();

    public DBMockup getDb() {
        return this.db;
    }

    public HashMap<String, DigLibSessionRI> getSessions() {
        return sessions;
    }

    public DigLibFactoryImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    @Override
    public DigLibSessionRI login(String u, String p) throws RemoteException {
        if (sessions.containsKey(u)) {
            return sessions.get(u);
        } else {
            DBMockup db = this.getDb();
            if (db.exists(u, p)) { // verifica se o user está na BD
                DigLibSessionRI session;
                System.out.println("Login feito com sucesso " + "User: " +u + " pass: " +p);
                session = new DigLibSessionImpl(this, new User(u, p)); // Criamos uma sessao
                this.sessions.put(u, session); // adicionamos à HashMap
                return sessions.get(u);
            }

            return null;
        }
    }
}
