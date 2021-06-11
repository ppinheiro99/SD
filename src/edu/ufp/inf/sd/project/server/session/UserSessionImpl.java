package edu.ufp.inf.sd.project.server.session;

import edu.ufp.inf.sd.project.server.DBMockup;
import edu.ufp.inf.sd.project.server.auth.AuthFactoryImpl;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupImpl;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class UserSessionImpl extends UnicastRemoteObject implements UserSessionRI {

    private User user;
    private DBMockup db;
    private AuthFactoryImpl factory;
    private UserSessionRI sessionRI;

    ///////////////////////////////////////////
    // Constructor
    public UserSessionImpl(User user, AuthFactoryImpl factory) throws RemoteException {
        this.user = user;
        this.factory = factory;
        this.db = this.factory.getDb();
    }

    ///////////////////////////////////////////
    // Methods

    /*
     *  Session logout
     */
    public void logout() throws RemoteException {
        this.factory.removeSession(this.user);
    }


    /*
     *  Show user coins
     */
    public int showCoins() throws RemoteException {
        return this.user.getCoins();
    }

    /*
     *  Add coins to user.
     */
    public void addCoins(int coins) throws RemoteException {
       this.user.setCoins(this.user.getCoins() + coins);
       this.db.updateUser(this.user);
    }

    /*
     *  List all users online.
     */
    public ArrayList<User> listUsers() throws RemoteException {
        ArrayList<User> users = new ArrayList<>();
        this.factory.getSessions().forEach((u, s) -> {
            try {
                users.add(db.userByUsername(u));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        return users;
    }

    /*
     *  Return user name.
     */
    public String showMyUsername() throws RemoteException {
        return this.user.getUsername();
    }


    ///////////////////////////////////////////
    // Get's & Set's
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public DBMockup getDb() {
        return db;
    }
    public void setDb(DBMockup db) {
        this.db = db;
    }
    public AuthFactoryImpl getFactory() {
        return factory;
    }
    public void setFactory(AuthFactoryImpl factory) {
        this.factory = factory;
    }


    /*
     *  Create JobGroup
     */
    @Override
    public JobGroupRI createJobGroup(String name, int coins, ArrayList<String> ficheiros, String strat) throws RemoteException, FileNotFoundException {
        System.out.println("[User: " + this.user.getUsername() + "] novo grupo: " + name);

   /*    String path= "edu/ufp/inf/sd/project/data/ficheiro_recebido.txt";

        try (PrintStream out = new PrintStream(new FileOutputStream(new File("edu/ufp/inf/sd/project/data/ficheiro_recebido.txt")))) {
            out.print(ficheiro);
             out.flush();
             out.close();
        }
*/

        JobGroupImpl jobGroup = new JobGroupImpl(coins,name,this.getUser().getUsername(),ficheiros,strat);
        this.db.addJobGroupRi(jobGroup);
        return jobGroup;
    }

    @Override
    /*
     *  List all JobGroup
     */
    public ArrayList<String> listJobGroups() throws RemoteException {
        return this.db.getJobGroupsNames();
    }

    /*
     *  Delete JobGroup
     */
    public void deleteJobGroup(int id) throws RemoteException {
        ArrayList<JobGroupImpl> jobGroups = this.db.getJobGroups();
        for (JobGroupImpl jobgroup : jobGroups) {
            if(jobgroup.getId() == id){
                //jobgroup.delete();
                this.db.getJobGroups().remove(jobgroup);
            }
        }
    }

    /*
     *  Get JobGroup Join Impl.
     */
    public JobGroupRI joinJobGroup(int id) throws RemoteException {
        ArrayList<JobGroupImpl> jobGroups = this.db.getJobGroups();
        for (JobGroupImpl jobgroup : jobGroups) {
            if(jobgroup.getId() == id){
                return jobgroup;
            }
        }
        return null;
    }

}
