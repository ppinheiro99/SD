package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupStatusState implements Serializable {
    private  String status;
    public GroupStatusState(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String s){
        this.status = s;
    }
}
