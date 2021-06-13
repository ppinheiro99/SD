package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.server.auth.AuthFactoryRI;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

import edu.ufp.inf.sd.project.server.auth.AuthFactoryRI;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;



public class JobShopClient{

    private SetupContextRMI contextRMI;
    private JobGroupRI jobGroupRI;
    private Scanner scanner;
    public boolean isLoggedIn = false;
    private AuthFactoryRI authRI;
    private UserSessionRI sessionRI;
    private ArrayList<WorkerImpl> workers = new ArrayList<WorkerImpl>();
    private Random random = new Random();
    private Integer workersNr = random.nextInt(8 - 1 + 1) + 1 ;

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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException | InterruptedException e) {
                e.printStackTrace();
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
    public void menu_session() throws IOException, TimeoutException, InterruptedException {
        System.out.println("\nSession:");
        System.out.println("[1] - Logout");

        System.out.println("Users:");
        System.out.println("[2] - List Users");
        System.out.println("[*] - My Username");
        System.out.println("[c] - My Coins");
        System.out.println("[a] - Add Coins");

        System.out.println("Group:");
        System.out.println("[3] - Create Group and attach worker.");
        System.out.println("[4] - List existing groups.");
        System.out.println("[5] - Pause existing group.");
        System.out.println("[6] - Attach worker to group.");
        System.out.println("[7] - Delete existing group.");
        System.out.println("[8] - Continue existing group.");
        System.out.println();

        String option = scanner.nextLine();


        // Logout
        if (option.equals("1"))
            user_logout();

        // List Users
        if (option.equals("2"))
            user_list();

        // My Coins
        if (option.equals("a"))
            add_coins();

        // My Coins
        if (option.equals("c"))
            System.out.println("\n\t My Coins: " + this.sessionRI.showCoins());

        // My Username
        if (option.equals("*"))
            System.out.println("\n\t My username: " + this.sessionRI.showMyUsername());


        // Create Group with Worker
        if (option.equals("3"))
            jobgroup_create();

        // List Groups
        if (option.equals("4"))
            jobgroup_list();

        // Pause Job Group
        if (option.equals("5")) {
            pause_job_group();
        }

        // Add worker to group
        if (option.equals("6")) {
            jobgroup_add_worker();
        }
        // Delete job group
        if (option.equals("7")) {
            delete_jobgroup();
        }

        // Continue job group
        if (option.equals("8")) {
            continue_jobgroup();
        }




    }



    private void add_coins() throws RemoteException {
        if (this.sessionRI != null) {
            System.out.print("\nCoins a adicionar: ");
            String saldo = scanner.nextLine();
            this.getCoinsPayment(Integer.parseInt(saldo));
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
    private void jobgroup_create() throws IOException, TimeoutException, InterruptedException {
        if (this.sessionRI != null) {
            ArrayList<String> ficheiros = new ArrayList<>();

            System.out.print("\nNome do Grupo: ");
            String name = scanner.nextLine();
            System.out.println("\nNumero de Ficheiros a enviar:");
            String nr_path = scanner.nextLine();
            for (int i = 0; i < Integer.parseInt(nr_path); i++) {
                System.out.println("\nNome do ficheiro (sem \".txt\":");
                String name_file = scanner.nextLine();
                String path = "edu/ufp/inf/sd/project/data/"+name_file+".txt";
                try {
                    ficheiros.add(readFile(path, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /**Debug**/ System.out.println(ficheiros);

            }

            System.out.println("\nPlafon para o JobGroup:");
            String plafon = scanner.nextLine();
            System.out.println("\nEstrategia para o JobGroup (ts ou ga):");
            String strat = scanner.nextLine();
            System.out.println("\nNumero minimo de workers para arrancar:");
            String nrminworker = scanner.nextLine();


            if(strat.compareTo("ts")==0 || strat.compareTo("ga")==0){
                //String path = "edu/ufp/inf/sd/project/data/la04.txt";
                ///Só podemos criar um jobgroup se o plafon for inferior ao saldo



                if(Integer.parseInt(plafon) < sessionRI.showCoins()){
                    this.jobGroupRI = this.sessionRI.createJobGroup(name, Integer.parseInt(plafon),ficheiros,strat,nrminworker);
                    //tira ao saldo o plafon para o jobgroup
                    this.getCoinsPayment(-Integer.parseInt(plafon));
                    if (jobGroupRI != null) {

                    } else {
                        System.out.println("Erro ao criar grupo?");
                    }
                }else {
                    System.out.println("Plafon é superior ao saldo!");
                }
            }


        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        return Files.readString(Paths.get(path), encoding);
    }


    /*
     *  Ask and pause one job group
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
     *  Ask and list all job groups
     */
    private void pause_job_group() throws RemoteException {
        if (this.sessionRI != null) {
            System.out.print("\nID do Grupo: ");
            String id = scanner.nextLine();
            ArrayList<String> groups = this.sessionRI.listJobGroups();
            System.out.println("\n\t List of Groups:");
            for (String name : groups)
                System.out.println(name);

            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));

            if(jobGroupRI!=null){
                if(jobGroupRI.getOwner().compareTo(this.sessionRI.showMyUsername())==0){
                    GroupStatusState s = new GroupStatusState("PAUSE");
                    jobGroupRI.setState(s);
                }
            }
        }
    }

    /*
     *  Add worker to jobgroup
     */
    private void jobgroup_add_worker() throws IOException, TimeoutException, InterruptedException {
        if (this.sessionRI != null) {

            System.out.println("ID do Grupo: ");
            String id = scanner.nextLine();
            System.out.println("Quantos workers quer associar ao jobgroup? (" + workersNr + ")"  );
            String wnr = scanner.nextLine();
            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));
            if(jobGroupRI != null) {
                    //Se o tamanho do nosso arraylist de workers for menor que o numero total de workers que temos , entao ainda podemos associar
                    if(workers.size() < workersNr){
                        //Enquanto i for menor que o numero de workers que queremos associar , associamos
                        for (int i = 0 ; i < Integer.parseInt(wnr) ; i++) {

                            workers.add(new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI,this)) ;
                        }
                    }
                    System.out.println("Criado com sucesso!");



                }else {
                    System.out.println("Erro ao adicionar worker!");
                }


        }
    }

    public void delete_jobgroup() throws RemoteException {
        if (this.sessionRI != null) {

            System.out.print("\nNome do Grupo: ");
            String name = scanner.nextLine();
            //String path = "edu/ufp/inf/sd/project/data/la04.txt";
            if(jobGroupRI.getOwner().compareTo(this.sessionRI.showMyUsername())==0){ this.sessionRI.deleteJobGroup(Integer.parseInt(name));
                System.out.println("JobGroup apagado com sucesso!");}



        }
    }

    private void continue_jobgroup() throws RemoteException {
        if (this.sessionRI != null) {
            System.out.print("\nID do Grupo: ");
            String id = scanner.nextLine();
            ArrayList<String> groups = this.sessionRI.listJobGroups();
            System.out.println("\n\t List of Groups:");
            for (String name : groups)
                System.out.println(name);

            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));

            if(jobGroupRI!=null){
                if(jobGroupRI.getOwner().compareTo(this.sessionRI.showMyUsername())==0){
                    GroupStatusState s = new GroupStatusState("CONTINUE");
                    jobGroupRI.setState(s);
                }

            }
        }
    }

    public void print_msg(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public void getCoinsPayment(Integer coins) throws RemoteException {
        if(this.sessionRI!=null){
            sessionRI.addCoins(coins);
        }
    }

}
