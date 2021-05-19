package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.server.auth.AuthFactoryRI;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class        JobShopClient{

    private SetupContextRMI contextRMI;
    private JobGroupRI jobGroupRI;
    private Scanner scanner;
    public boolean isLoggedIn = false;
    private AuthFactoryRI authRI;
    private UserSessionRI sessionRI;

    ///////////////////////////////////////////
    // Main
    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.exit(-1);
        } else {
            JobShopClient hwc=new JobShopClient(args);
            hwc.lookupService();
            hwc.playService();
        }
    }
    ///////////////////////////////////////////
    // Initial Setup
    public JobShopClient(String[] args) {
        try {
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});

        } catch (RemoteException e) {
            Logger.getLogger(JobShopClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    ///////////////////////////////////////////
    // RMI Register
    // jobShopRI = (JobShopRI) registry.lookup(serviceUrl);
    private Remote lookupService() {
        try {
            Registry registry = contextRMI.getRegistry();
            if (registry != null) {
                String serviceUrl = contextRMI.getServicesUrl(0);
                authRI = (AuthFactoryRI) registry.lookup(serviceUrl);

            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return authRI;
    }

    ///////////////////////////////////////////
    // Call functions
    private void playService() {
        System.out.println();

        this.scanner = new Scanner(System.in);

        while(true) {
            try {

                // Login Menu
                if(! this.isLoggedIn)
                    isLoggedIn = menu_login();
                    // Methods Menu
                else
                    menu_session();

                    // Methods Menu
                //else {

                    // menu_session();


                    //============ Call TS remote service ============
                    /*
                    String jsspInstancePath = "edu/ufp/inf/sd/project/data/la01.txt";
                    int makespan = this.jobShopRI.runTS(jsspInstancePath);
                    Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                            "[TS] Makespan for {0} = {1}",
                            new Object[]{jsspInstancePath, String.valueOf(makespan)});


                    //============ Call GA ============
                    String queue = "jssp_ga";
                    String resultsQueue = queue + "_results";
                    CrossoverStrategies strategy = CrossoverStrategies.ONE;
                    Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                            "GA is running for {0}, check queue {1}",
                            new Object[]{jsspInstancePath, resultsQueue});
                    GeneticAlgorithmJSSP ga = new GeneticAlgorithmJSSP(jsspInstancePath, queue, strategy);
                    ga.run();
                    */

              //  }

            } catch (RemoteException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
    }

    /*
     *  Show initial menu
     */
    public boolean menu_login() throws RemoteException {
        System.out.println("\nAUTH:");
        System.out.println("[1] - Registry");
        System.out.println("[2] - Login");
        System.out.println("[9] - Exit");

        String option = scanner.nextLine();

        // Registry
        if(option.equals("1"))
            user_create();

        // Login
        if(option.equals("2"))
            return user_login();

        // Exit
        if(option.equals("9"))
            System.exit(1);


        System.out.println();
        return false;
    }

    /*
     *  Create new user
     */
    private void user_create() throws RemoteException {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Boolean success = authRI.registry(username, password);

        if(success)
            System.out.println("User criado com sucesso!");
        else
            System.out.println("Erro ao criar um usuario!");
    }

    /*
     *  Login user
     */
    private boolean user_login() throws RemoteException {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        sessionRI = authRI.login(username, password);

        if(sessionRI != null){
            System.out.println("Sessao iniciada com sucesso!");
            return true;
        }
        else {
            System.out.println("Erro ao iniciar sessao!");
            return false;
        }
    }

    /*
     *  Show session menu
     */
    public void menu_session() throws RemoteException {
        System.out.println("\nSession:");
        System.out.println("[1] - Logout");

        System.out.println("Users:");
        System.out.println("[2] - List Users");
        System.out.println("[*] - My Username");

        System.out.println("Group:");
        System.out.println("[3] - Create Group and attach worker.");
        System.out.println("[4] - List existing groups.");
        System.out.println("[5] - Attach worker to group.");
        System.out.println();

        String option = scanner.nextLine();


        // Logout
        if (option.equals("1"))
            user_logout();

        // List Users
        if (option.equals("2"))
            user_list();

        // My Username
        if (option.equals("*"))
            System.out.println("\n\t My username: " + this.sessionRI.showMyUsername());


        // Create Group with Worker
        if (option.equals("3"))
            jobgroup_create();

        // List Groups
        if (option.equals("4"))
            jobgroup_list();


        // Add worker to group
        if (option.equals("5")) {
            jobgroup_add_worker();
        }


    }

    /*
     *  List all users online
     */
    private void user_list() throws RemoteException {
        if (this.sessionRI != null) {
            ArrayList<User> users = this.sessionRI.listUsers();
            System.out.println("\nUsers Online:");
            for (User a : users) {
                System.out.println(a.getUsername());
            }
            System.out.println();
        }
    }

    /*
     *  User Logout
     */
    private void user_logout() throws RemoteException {
        if (this.sessionRI != null) {
            this.sessionRI.logout();
            this.sessionRI = null;
            this.isLoggedIn = false;
        }
    }

    ///Create JobGroup

    private void jobgroup_create() throws RemoteException {
        if (this.sessionRI != null) {

            System.out.print("\nNome do Grupo: ");
            String name = scanner.nextLine();

            this.jobGroupRI = this.sessionRI.createJobGroup(name, 1000);
            String path = "edu/ufp/inf/sd/project/data";
            if (jobGroupRI != null) {
                System.out.println("Criado com sucesso!");
                WorkerImpl worker = new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI,path);
            } else {
                System.out.println("Erro ao criar grupo?");
            }
        }
    }


    /*
     *  Ask and list all task groups
     */
    private void jobgroup_list() throws RemoteException {
        if (this.sessionRI != null) {

            ArrayList<String> groups = this.sessionRI.listJobGroups();
            System.out.println("\n\t List of Groups:");
            for (String name : groups)
                System.out.println(name);
        }
    }

    /*
     *  Add worker to jobgroup
     */
    private void jobgroup_add_worker() throws RemoteException {
        if (this.sessionRI != null) {

            System.out.println("ID do Grupo: ");
            String id = scanner.nextLine();
            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));
            String path = "edu/ufp/inf/sd/project/data";
            if(jobGroupRI != null) {
                new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI,path);
            }
        }
    }

}
