package edu.ufp.inf.sd.project.server.auth;

import edu.ufp.inf.sd.project.server.DBMockup;
import edu.ufp.inf.sd.project.server.session.UserSessionImpl;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.rabbitmqservices.util.JwtToken;
import io.jsonwebtoken.Claims;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;

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

    public Boolean registry(String token) throws RemoteException, MalformedClaimException, JoseException {
        //System.out.println(rsaJsonWebKey);
        JwtClaims claims =JwtToken.decode(token);
        if(claims==null) {
            System.out.println("Erro no registo por JWT Token!");
            return null;
        }

        if(this.db.usernameIsAvailable(claims.getIssuer())){
            this.db.register(claims.getIssuer(), claims.getSubject());
            return true;
        }

        return false;
    }

    public UserSessionRI login(String token) throws RemoteException, MalformedClaimException, JoseException {

        //System.out.println(rsaJsonWebKey);
        JwtClaims claims =JwtToken.decode(token);
        if(claims==null) {
            System.out.println("Username ou Password invalidos!");
            return null;
        }

        System.out.println("MATCH NO JWT TOKEN SESSION!");
        //System.out.println(claims);
        String username=claims.getIssuer();
        String password=claims.getSubject();


        //JwtToken.teste();
        if (sessions.containsKey(username)) {
            return sessions.get(username);
        } else {
            DBMockup db = this.getDb();
            if (db.exists(username, password)) { // verifica se o user está na BD
                UserSessionRI session;
                System.out.println("Login feito com sucesso " + "User: " +username + " pass: " +password);
                session = new UserSessionImpl(new User(username, password),this,token); // Criamos uma sessao
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

