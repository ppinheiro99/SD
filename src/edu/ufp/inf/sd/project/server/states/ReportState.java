package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;
import java.util.HashMap;

public class ReportState implements Serializable {

    private final String user;
    private final Boolean match;
    private final String password;
    private final HashMap<String, String> passwordsTried;


    //////////////////////////////////
    // Constructor
    public ReportState(String user, Boolean match, String password, HashMap<String, String> passwordsTried) {
        this.user = user;
        this.match = match;
        this.password = password;
        this.passwordsTried = passwordsTried;
    }


    //////////////////////////////////
    // Get's & Set's
    public String getUser() {
        return user;
    }
    public Boolean getMatch() {
        return match;
    }
    public String getPassword() {
        return password;
    }
    public HashMap<String, String> getPasswordsTried() {
        return passwordsTried;
    }
}

