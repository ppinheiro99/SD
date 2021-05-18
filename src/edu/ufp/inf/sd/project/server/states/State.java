package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class State implements Serializable {

    private String path;
    public State(String path) {
        this.path = path;
    }
}
