package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupTask implements Serializable {
    private final String id;
    private final int startingPoint;

    //////////////////////////////////
    // Constructor
    public GroupTask(String id, int startingPoint) {
        this.id = id;
        this.startingPoint = startingPoint;
    }

    //////////////////////////////////
    // Get's & Set's
    public String getId() {
        return id;
    }

    public int getStartingPoint() {
        return startingPoint;
    }
}
