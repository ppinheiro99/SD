package edu.ufp.inf.sd.project.server.states;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupInfoState implements Serializable {


    private final ArrayList<String> path;
    private final String exchangeName;

    //////////////////////////////////
    // Constructor
    public GroupInfoState(ArrayList<String> path,String exchange) {
        this.path = path;
        this.exchangeName = exchange;
    }

    //////////////////////////////////

    public String getExchangeName() {
        return exchangeName;
    }
    // Get's & Set's

    public ArrayList<String>  getPath() {
        return path;
    }
}
