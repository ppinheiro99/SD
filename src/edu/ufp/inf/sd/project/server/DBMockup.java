package edu.ufp.inf.sd.project.server;

import edu.ufp.inf.sd.project.server.jobgroup.JobGroupImpl;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBMockup {

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<JobGroupImpl> jobGroups = new ArrayList<>();


    ///////////////////////////////////////////
    // Constructor
    public DBMockup() {
        //Add user
        users.add(new User("admin", "admin"));
    }

    ///////////////////////////////////////////
    // Methods

    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUsername().compareTo(u) == 0 && usr.getPassword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    // Search User
    public User userExists(String username, String password) {
        for (User u : this.users)
            if(u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;

        return null;
    }


    /*
            USERS
     */

    // Check if an username is available.
    public Boolean usernameIsAvailable(String username){
        for (User u : this.users) {
            if(u.getUsername().equals(username))
                return false;
        }

        return true;
    }

    /*
     *  Return user by username.
     */
    public User userByUsername(String username) throws RemoteException {
        for (User u : this.users) {
            if(u.getUsername().equals(username))
                return u;
        }

        return null;
    }

    // Update user information.
    public void updateUser(User user){
        for (User u : this.users) {
            if(u.getUsername().equals(user.getUsername())) {
                u.setCoins(user.getCoins());
                return;
            }
        }
    }


     /*
            JOB GROUPS
     */

    // Add Group
    public void addJobGroupRi(JobGroupImpl group){
        this.jobGroups.add(group);
    }

    // Search Group
    public JobGroupImpl findJobGroupRi(int id){
        for (JobGroupImpl jobGroup : this.jobGroups)
            if(jobGroup.getId() == id)
                return jobGroup;

        return null;
    }

    public ArrayList<String> getJobGroupsNames() {
        ArrayList<String> names = new ArrayList<>();
        for (JobGroupImpl jg : jobGroups) {
            names.add(jg.getId() + " | " + jg.getName());
        }
        return names;
    }

    public ArrayList<JobGroupImpl> getJobGroups() {
        return this.jobGroups;
    }

}