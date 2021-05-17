package edu.ufp.inf.sd.project.server.user;

import java.io.Serializable;

public class User implements Serializable {

    private final String username;
    transient private final String password;
    transient private int coins;

    //////////////////////////////////
    // Constructor
    public User(String us, String pw){
        this.username = us;
        this.password = pw;
        this.coins = 0;
    }

    //////////////////////////////////
    // Get's & Set's
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
