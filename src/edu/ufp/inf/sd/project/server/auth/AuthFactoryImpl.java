package edu.ufp.inf.sd.project.server.auth;

import edu.ufp.inf.sd.project.server.DBMockup;
import edu.ufp.inf.sd.project.server.session.UserSessionImpl;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.user.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class AuthFactoryImpl extends UnicastRemoteObject implements AuthFactoryRI {

    private DBMockup db;
    private final HashMap<String, UserSessionRI> sessions = new HashMap<>();

    public AuthFactoryImpl() throws RemoteException {
        super();
        this.db = new DBMockup();
    }
    public DBMockup getDb() {
        return this.db;
    }
    public HashMap<String, UserSessionRI> getSessions() {
        return sessions;
    }

    public Boolean registry(String username, String password) throws RemoteException{
        System.out.println("teste 2: " +username + " " + password + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if(this.db.usernameIsAvailable(username)){
            this.db.register(username,password);
            return true;
        }

        return false;
    }

    public UserSessionRI login(String username, String password) throws RemoteException{
        if (sessions.containsKey(username)) {
            return sessions.get(username);
        } else {
            DBMockup db = this.getDb();
            if (db.exists(username, password)) { // verifica se o user está na BD
                UserSessionRI session;
                System.out.println("Login feito com sucesso " + "User: " +username + " pass: " +password);
                session = new UserSessionImpl(new User(username, password), this ); // Criamos uma sessao
                this.sessions.put(username, session); // adicionamos à HashMap
                return sessions.get(username);
            }

            return null;
        }
    }

    /*
           SESSIONS
    */
    public void removeSession(User u){
        this.sessions.remove(u);
    }
}

