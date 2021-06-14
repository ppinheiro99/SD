package edu.ufp.inf.sd.project.server.auth;

import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.rabbitmqservices.util.JwtToken;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthFactoryRI extends Remote {
    public Boolean registry(String username, String password) throws RemoteException;
    //public UserSessionRI login(String username, String password) throws RemoteException;
    public UserSessionRI login(String token, RsaJsonWebKey rsaJsonWebKey) throws RemoteException, MalformedClaimException, JoseException;

}