package edu.ufp.inf.sd.project.client;

import edu.ufp.inf.sd.project.server.auth.AuthFactoryRI;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.server.user.User;
import edu.ufp.inf.sd.project.util.jwt.JWT;
import edu.ufp.inf.sd.project.client.layouts.menucorrect;
import edu.ufp.inf.sd.rmi._04_diglib.client.DigLibClient;
import edu.ufp.inf.sd.rmi._05_observer.server.SubjectRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.awt.*;
import java.io.IOException;
import java.lang.module.FindException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.swing.*;

import static javax.swing.GroupLayout.Alignment.*;


public class JobShopClient extends javax.swing.JFrame {

    private String[] args;
    private SetupContextRMI contextRMI;
    private JobGroupRI jobGroupRI;
    private Scanner scanner;
    public boolean isLoggedIn = false;
    private AuthFactoryRI authRI;
    private UserSessionRI sessionRI;

    /**
     * Parte Grafica
     **/
    private javax.swing.JLabel jLabelUserName;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JTextField jTextFieldUsername;
    private javax.swing.JTextField jTextFieldPassword;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonRegistry;
    private javax.swing.JScrollPane jScrollPane1;

    public UserSessionRI getSessionRI() {
        return sessionRI;
    }

    public JobGroupRI getJobGroupRI() {
        return jobGroupRI;
    }

    public void setJobGroupRI(JobGroupRI jobGroupRI) {
        this.jobGroupRI = jobGroupRI;
    }

    ///////////////////////////////////////////
    // Initial Setup
    public JobShopClient(String[] args) throws RemoteException {

        String registryIP = args[0];
        String registryPort = args[1];
        String serviceName = args[2];
        this.args = args;
        this.args[0] = registryIP;
        this.args[1] = registryPort;
        this.args[2] = serviceName;

        System.out.println("#################################################################################");
        System.out.println("###############" + Arrays.toString(this.args) + "###############");
        System.out.println("#################################################################################");

        //1. Init the GUI components
        initComponents();
        //2. Init the RMI context (load security manager, lookup subject, etc.)
        // contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        initContext(args);
        contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        //3. Create observer (which attaches himself to subject)
        //initObserver(args);

    }

    private void initContext(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP=args[0];
            String registryPort=args[1];
            String serviceName=args[2];
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to setup RMI context...");
            //Create a context for RMI setup
            contextRMI=new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
            //Lookup service
            this.authRI=(AuthFactoryRI) lookupService();

        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }


    ///////////////////////////////////////////
    // Main
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (args.length >= 0) {
                    try {
                        UIManager.setLookAndFeel(
                                "javax.swing.plaf.metal.MetalLookAndFeel");
                        //  "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                        //UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        new JobShopClient(args).setVisible(true);
                    } catch (RemoteException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    System.out.println(JobShopClient.class + ": call must have the following args: <rmi_ip> <rmi_port> <rmi_service_prefix>");
                }
            }
        });
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

        while (true) {
            try {

                // Login Menu
                if (!this.isLoggedIn)
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
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     *  Show initial menu
     */
    public boolean menu_login() throws RemoteException {
        System.out.println("\nAUTH:");
        System.out.println("[0] - Teste");
        System.out.println("[1] - Registry");
        System.out.println("[2] - Login");
        System.out.println("[9] - Exit");

        String option = scanner.nextLine();

        // Registry
        if(option.equals("0"))
            teste();

        if(option.equals("1"))
            user_create();

        // Login
        if (option.equals("2"))
            return user_login();

        // Exit
        if (option.equals("9"))
            System.exit(1);


        System.out.println();
        return false;
    }

    private void teste(){
        String username = "pedro";
        String password = "12345";

        if(!username.isEmpty() || !password.isEmpty()){
            JWT.createJWT("null",username,password,1000000000);
            //System.out.println(token);
        }
    }

    /*
     *  Create new user registo
     */
    private void user_create() throws RemoteException {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Boolean success = authRI.registry(username, password);

        if (success)
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

        if (sessionRI != null) {
            System.out.println("Sessao iniciada com sucesso!");
            return true;
        } else {
            System.out.println("Erro ao iniciar sessao!");
            return false;
        }
    }

    /*
     *  Show session menu
     */
    public void menu_session() throws IOException, TimeoutException {
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
    public void user_logout() throws RemoteException {
        if (this.sessionRI != null) {
            this.sessionRI.logout();
            this.sessionRI = null;
            this.isLoggedIn = false;
        }
    }

    ///Create JobGroup

    public void jobgroup_create() throws IOException, TimeoutException {
        if (this.sessionRI != null) {
            System.out.print("\nNome do Grupo: ");
            String name = scanner.nextLine();
            System.out.println("\nPath para Ficheiro:");
            String path = scanner.nextLine();
            System.out.println("\nPlafon para o JobGroup:");
            String plafon = scanner.nextLine();
            System.out.println("\nEstrategia para o JobGroup (ts ou ga):");
            String strat = scanner.nextLine();
            if (strat.compareTo("ts") == 0 || strat.compareTo("ga") == 0) {
                //String path = "edu/ufp/inf/sd/project/data/la04.txt";
                ///Só podemos criar um jobgroup se o plafon for inferior ao saldo
                if (Integer.parseInt(plafon) < sessionRI.showCoins()) {
                    this.jobGroupRI = this.sessionRI.createJobGroup(name, Integer.parseInt(plafon), path, strat);
                    //tira ao saldo o plafon para o jobgroup
                    this.getCoinsPayment(-Integer.parseInt(plafon));
                    if (jobGroupRI != null) {
                        if (jobGroupRI.getCoins() == 10) {
                            WorkerImpl worker = new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI, this);
                            jobGroupRI.verify_winner();
                        } else {
                            WorkerImpl worker = new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI, this);
                        }

                    } else {
                        System.out.println("Erro ao criar grupo?");
                    }
                } else {
                    System.out.println("Plafon é superior ao saldo!");
                }
            }


        }
    }


    /*
     *  Ask and pause one job group
     */
    public void jobgroup_list() throws RemoteException {
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
    public void pause_job_group() throws RemoteException {
        if (this.sessionRI != null) {
            System.out.print("\nID do Grupo: ");
            String id = scanner.nextLine();
            ArrayList<String> groups = this.sessionRI.listJobGroups();
            System.out.println("\n\t List of Groups:");
            for (String name : groups)
                System.out.println(name);

            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));
            if (jobGroupRI != null) {
                GroupStatusState s = new GroupStatusState("PAUSE");
                jobGroupRI.setState(s);
            }
        }
    }

    /*
     *  Add worker to jobgroup
     */
    public void jobgroup_add_worker() throws IOException, TimeoutException {
        if (this.sessionRI != null) {

            System.out.println("ID do Grupo: ");
            String id = scanner.nextLine();
            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));
            if (jobGroupRI != null) {
                System.out.println("Criado com sucesso!");
                ///Temos que verificar se já nao tivemos um worker neste jobgroup(evitar duplicação de esforços)

                ///Temos que verificar se as coins disponiveis no plafon sao suficientes(>10)

                if (jobGroupRI.getCoins() > 10) {
                    new WorkerImpl(this.sessionRI.showMyUsername(), jobGroupRI, this);

                } else if (jobGroupRI.getCoins() == 10) {
                    //Se entrar aqui , significa que estamos no limite do plafon para a melhor solução
                    jobGroupRI.verify_winner();
                }

            } else {
                System.out.println("Erro ao adicionar worker!");
            }


        }
    }

    public void delete_jobgroup() throws RemoteException {
        if (this.sessionRI != null) {

            System.out.print("\nNome do Grupo: ");
            String name = scanner.nextLine();
            //String path = "edu/ufp/inf/sd/project/data/la04.txt";
            this.sessionRI.deleteJobGroup(Integer.parseInt(name));
            System.out.println("JobGroup apagado com sucesso!");

        }
    }

    public void continue_jobgroup() throws RemoteException {
        if (this.sessionRI != null) {
            System.out.print("\nID do Grupo: ");
            String id = scanner.nextLine();
            ArrayList<String> groups = this.sessionRI.listJobGroups();
            System.out.println("\n\t List of Groups:");
            for (String name : groups)
                System.out.println(name);

            JobGroupRI jobGroupRI = this.sessionRI.joinJobGroup(Integer.parseInt(id));
            if (jobGroupRI != null) {
                GroupStatusState s = new GroupStatusState("CONTINUE");
                jobGroupRI.setState(s);
            }
        }
    }

    public void print_msg(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public void getCoinsPayment(Integer coins) throws RemoteException {
        if (this.sessionRI != null) {
            sessionRI.addCoins(coins);
        }
    }

    //###############################################################################
    //#############################  Interface Grafica  ################################################
    //################################################################################
    //##############################################################################
    /*
     *  Gui
     */

    /**
     * Create new user
     *
     * @param evt evento relativo ao carregar no botão
     */
    public void jButtonRegistryActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        if (!jLabelUserName.getText().isEmpty() || !jLabelPassword.getText().isEmpty()) {

            System.out.println("#################################################################################");
            System.out.println("###############" + "Registy: " + jTextFieldUsername.getText() + "PASS: " + jTextFieldPassword.getText() + "###############");
            System.out.println("#################################################################################");

            System.out.println(jTextFieldUsername.getText() + jTextFieldPassword.getText());

            Boolean success = authRI.registry(jTextFieldUsername.getText(), jTextFieldPassword.getText());
            System.out.println(success);
            if (success) {
                JobShopClient.main(this.args);
                this.setVisible(false);
            } else {
                System.out.println("Tem de se autenticar");
            }
        } else {
            System.out.println("Preencha todos os campos");
        }

    }

    /**
     * Fazer login do cliente
     *
     * @param evt evento relativo ao carregar no butão
     * @throws Exception
     */
    public void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) throws Exception {
        if (!jTextFieldUsername.getText().isEmpty() || !jTextFieldPassword.getText().isEmpty()) {
            System.out.println("#################################################################################");
            System.out.println("###############" + "LOGIN: " + jTextFieldUsername.getText() + "PASS: " + jTextFieldPassword.getText() + "###############");
            System.out.println("#################################################################################");

            System.out.println(jTextFieldUsername.getText()+" "+jTextFieldPassword.getText()+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.sessionRI = this.authRI.login(jTextFieldUsername.getText(), jTextFieldPassword.getText());
            if (sessionRI != null) {
                System.out.println("Sessao iniciada com sucesso!");
                menucorrect.menucorrect(this,args);
            } else {
                System.out.println("Erro ao iniciar sessao!");

            }

            //  String token = JWT.createJWT("null",jTextFieldUsername.getText(),jTextFieldPassword.getText(),1000000000);
            // HashSessionRI session= hashFactory.login(token);
            if (sessionRI != null) {
                JobShopClient.main(this.args);
                this.setVisible(false);
            } else {
                System.out.println("Tem de se autenticar");
            }
        } else {
            System.out.println("Preencha todos os campos");
        }
    }


    public void initComponents() {

        /**
         * Inicializar as variáveis
         */
        jScrollPane1 = new javax.swing.JScrollPane();

        jButtonLogin = new javax.swing.JButton();
        jButtonRegistry = new javax.swing.JButton();

        jTextFieldUsername = new javax.swing.JTextField();
        jTextFieldPassword = new javax.swing.JTextField();

        jLabelUserName = new javax.swing.JLabel();
        jLabelPassword = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        /**
         * Atribuir textos e respetivas funções às labels e butões
         */
        jButtonLogin.setText("Login");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButtonLoginActionPerformed(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        jButtonRegistry.setText("Registry");
        jButtonRegistry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButtonRegistryActionPerformed(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        jTextFieldUsername.setText("username");
        jTextFieldPassword.setText("password");
        jLabelUserName.setText("Username");
        jLabelPassword.setText("Password:");

        /**
         * Parte gráfica
         */
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(jLabelUserName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextFieldUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                                .addComponent(jLabelPassword)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextFieldPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonLogin)
                                .addComponent(jButtonRegistry)

                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonLogin)
                                        .addComponent(jButtonRegistry)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelUserName)
                                        .addComponent(jTextFieldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPassword)
                                        .addComponent(jTextFieldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
}


