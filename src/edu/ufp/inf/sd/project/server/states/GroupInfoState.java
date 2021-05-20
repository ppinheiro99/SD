package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupInfoState implements Serializable {


    private final String path;


    //////////////////////////////////
    // Constructor
    public GroupInfoState(String path) {
        this.path = path;
    }

    //////////////////////////////////
    // Get's & Set's

    public String getPath() {
        return path;
    }
}
