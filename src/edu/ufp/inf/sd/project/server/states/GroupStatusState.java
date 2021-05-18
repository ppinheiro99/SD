package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupStatusState implements Serializable {
    private final String status;
    public GroupStatusState(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
