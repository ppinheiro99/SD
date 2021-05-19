package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupInfoState implements Serializable {


    private final String path;
    private final Integer coins;


    //////////////////////////////////
    // Constructor
    public GroupInfoState(String path,Integer coins) {
        this.path = path;
        this.coins = coins;
    }

    //////////////////////////////////
    // Get's & Set's

    public String getPath() {
        return path;
    }
}
