package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;

public class GroupInfoState implements Serializable {


    private final String path;
    private final String exchangeName;

    //////////////////////////////////
    // Constructor
    public GroupInfoState(String path,String exchange) {
        this.path = path;
        this.exchangeName = exchange;
    }

    //////////////////////////////////

    public String getExchangeName() {
        return exchangeName;
    }
    // Get's & Set's

    public String getPath() {
        return path;
    }
}
