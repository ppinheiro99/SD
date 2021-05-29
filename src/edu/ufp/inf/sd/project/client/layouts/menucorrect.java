package edu.ufp.inf.sd.project.client.layouts;

import edu.ufp.inf.sd.project.client.JobShopClient;
import edu.ufp.inf.sd.project.client.WorkerImpl;
import edu.ufp.inf.sd.project.server.auth.AuthFactoryImpl;
import edu.ufp.inf.sd.project.server.jobgroup.JobGroupRI;
import edu.ufp.inf.sd.project.server.session.UserSessionRI;
import edu.ufp.inf.sd.project.server.states.GroupStatusState;
import edu.ufp.inf.sd.project.server.user.User;

import javax.mail.Session;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;

import static edu.ufp.inf.sd.project.client.layouts.createJobGroupLayout.createJobGroupLayoutcorrect;

public class menucorrect extends JFrame {
    static menucorrect themenucorrect;

    JPanel pnPanel0;
    JTextArea taArea0;
    JLabel lbLabel0;
    JButton btSent;
    JButton btListexistinggroups;
    JButton btPauseexistinggroup;
    JButton btCreateGroupandattachworker;
    JButton btAttachworkertogroup;
    JButton btDeleteexistinggroup;
    JButton btContinueexistinggroup;
    JButton btAddCoins;
    JButton btListUsers;
    JButton btLogout;
    JLabel lbLabel1;
    JLabel lbLabel2;

    private static UserSessionRI sessionRI_static;
    private static JobShopClient JobShopClient_static;

    /**
     *
     */
    public static void menucorrect(JobShopClient JobShopClient, String args[]) throws RemoteException {
        JobShopClient_static = JobShopClient;
        sessionRI_static = JobShopClient_static.getSessionRI();
        main(args);
    }

    public static void main(String args[]) throws RemoteException {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
        themenucorrect = new menucorrect();
    }

    /**
     *
     */
    public menucorrect() throws RemoteException {
        super("Session:");

        pnPanel0 = new JPanel();
        GridBagLayout gbPanel0 = new GridBagLayout();
        GridBagConstraints gbcPanel0 = new GridBagConstraints();
        pnPanel0.setLayout(gbPanel0);

        taArea0 = new JTextArea(2, 10);
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 6;
        gbcPanel0.gridwidth = 13;
        gbcPanel0.gridheight = 3;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 1;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(taArea0, gbcPanel0);
        pnPanel0.add(taArea0);

        lbLabel0 = new JLabel("DISPLAY:");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 0;
        gbcPanel0.gridwidth = 21;
        gbcPanel0.gridheight = 6;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 1;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(lbLabel0, gbcPanel0);
        pnPanel0.add(lbLabel0);

        lbLabel1 = new JLabel( ""  );
        lbLabel1.setBackground( new Color( 255,249,44 ) );
        gbcPanel0.gridx = 17;
        gbcPanel0.gridy = 10;
        gbcPanel0.gridwidth = 4;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 1;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints( lbLabel1, gbcPanel0 );
        pnPanel0.add( lbLabel1 );


        lbLabel2 = new JLabel( ""  );
        lbLabel2.setBackground( new Color( 154,238,238 ) );
        gbcPanel0.gridx = 17;
        gbcPanel0.gridy = 9;
        gbcPanel0.gridwidth = 4;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 1;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints( lbLabel2, gbcPanel0 );
        pnPanel0.add( lbLabel2 );

        if (JobShopClient_static.getSessionRI() != null) {
            lbLabel1.setText("My Coins: " + sessionRI_static.showCoins());
            lbLabel2.setText("My username: " + sessionRI_static.showMyUsername());
        } else {
            lbLabel1.setText("My Coins: ERRO");
            lbLabel2.setText("My username: ERRO");
        }
/******************************************************* BUTTOES *****************************************************/
        btSent = new JButton("sent");
        gbcPanel0.gridx = 13;
        gbcPanel0.gridy = 6;
        gbcPanel0.gridwidth = 3;
        gbcPanel0.gridheight = 3;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btSent, gbcPanel0);
        pnPanel0.add(btSent);
        btSent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btSent(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        btListexistinggroups = new JButton("List existing groups");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 9;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 1;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btListexistinggroups, gbcPanel0);
        pnPanel0.add(btListexistinggroups);
        btListexistinggroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btListexistinggroups(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        btPauseexistinggroup = new JButton("Pause existing group");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 10;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btPauseexistinggroup, gbcPanel0);
        pnPanel0.add(btPauseexistinggroup);
        btPauseexistinggroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btPauseexistinggroup(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btCreateGroupandattachworker = new JButton("Create Group and attach worker");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 12;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btCreateGroupandattachworker, gbcPanel0);
        pnPanel0.add(btCreateGroupandattachworker);
        btCreateGroupandattachworker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btCreateGroupandattachworker(evt);
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

        btAttachworkertogroup = new JButton(" Attach worker to group");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 14;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btAttachworkertogroup, gbcPanel0);
        pnPanel0.add(btAttachworkertogroup);
        btAttachworkertogroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btAttachworkertogroup(evt);
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });


        btDeleteexistinggroup = new JButton("Delete existing group");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 16;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btDeleteexistinggroup, gbcPanel0);
        pnPanel0.add(btDeleteexistinggroup);
        btDeleteexistinggroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btDeleteexistinggroup(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btContinueexistinggroup = new JButton("Continue existing group");
        gbcPanel0.gridx = 0;
        gbcPanel0.gridy = 18;
        gbcPanel0.gridwidth = 1;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btContinueexistinggroup, gbcPanel0);
        pnPanel0.add(btContinueexistinggroup);
        btContinueexistinggroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btContinueexistinggroup(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        btAddCoins = new JButton("Add Coins");
        gbcPanel0.gridx = 18;
        gbcPanel0.gridy = 18;
        gbcPanel0.gridwidth = 3;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btAddCoins, gbcPanel0);
        pnPanel0.add(btAddCoins);
        btAddCoins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btAddCoins(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btListUsers = new JButton("List Users");
        gbcPanel0.gridx = 18;
        gbcPanel0.gridy = 14;
        gbcPanel0.gridwidth = 3;
        gbcPanel0.gridheight = 2;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btListUsers, gbcPanel0);
        pnPanel0.add(btListUsers);
        btListUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btListUsers(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btLogout = new JButton("Logout");
        gbcPanel0.gridx = 16;
        gbcPanel0.gridy = 6;
        gbcPanel0.gridwidth = 5;
        gbcPanel0.gridheight = 3;
        gbcPanel0.fill = GridBagConstraints.BOTH;
        gbcPanel0.weightx = 1;
        gbcPanel0.weighty = 0;
        gbcPanel0.anchor = GridBagConstraints.NORTH;
        gbPanel0.setConstraints(btLogout, gbcPanel0);
        pnPanel0.add(btLogout);
        btLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btLogout(evt);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(pnPanel0);
        pack();
        setVisible(true);
    }

    /***************************************fUNCOES****************************************************************/

    private void btSent(java.awt.event.ActionEvent evt) throws RemoteException {
    }

    private void btListexistinggroups(java.awt.event.ActionEvent evt) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html>DISPLAY:<br/> **List existing groups** <br/>");

        lbLabel0.setText(stringBuilder.toString());
        if (JobShopClient_static.getSessionRI() != null) {

            ArrayList<String> groups = JobShopClient_static.getSessionRI().listJobGroups();
            stringBuilder.append("<br/> List of Groups:");
            for (String name : groups) {
                stringBuilder.append(name);
                stringBuilder.append("<br/>");
            }
            lbLabel0.setText(stringBuilder.toString());
        } else {
            lbLabel0.setText("<html> ERR0 A ACEDER A CLASS SESSAORI :(<br/>");
        }
    }

    private void btPauseexistinggroup(java.awt.event.ActionEvent evt) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html>DISPLAY:<br/> **Pause existing group** <br/>");
        lbLabel0.setText(stringBuilder.toString());
        if (JobShopClient_static.getSessionRI() != null) {
            System.out.print("<br/>ID do Grupo: ");
            String id = "";
            if (btSent.getModel().isPressed()) {
                id = taArea0.getText();
                if (id.compareTo("") == 0) return;
            }
            ArrayList<String> groups = JobShopClient_static.getSessionRI().listJobGroups();
            stringBuilder.append("<br/>\t List of Groups:");

            for (String name : groups) {
                stringBuilder.append(name);
                stringBuilder.append("<br/>");
            }

            lbLabel0.setText(stringBuilder.toString());
            JobGroupRI jobGroupRI = JobShopClient_static.getSessionRI().joinJobGroup(Integer.parseInt(id));
            if (jobGroupRI != null) {
                GroupStatusState s = new GroupStatusState("PAUSE");
                jobGroupRI.setState(s);
            }
        } else {
            lbLabel0.setText(" ERR0 A ACEDER A CLASS SESSAORI :(<br/>");
        }
    }

    private void btCreateGroupandattachworker(java.awt.event.ActionEvent evt) throws IOException, TimeoutException {
        createJobGroupLayoutcorrect(JobShopClient_static,null);
    }

    private void btAttachworkertogroup(java.awt.event.ActionEvent evt) throws IOException, TimeoutException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html> DISPLAY: <br/> **Attach worker to group** <br/>");
        lbLabel0.setText(stringBuilder.toString());
        if (JobShopClient_static.getSessionRI() != null) {
            stringBuilder.append("ID do Grupo: <br/>");
            lbLabel0.setText(stringBuilder.toString());
            String id = "";

            JobGroupRI jobGroupRI = JobShopClient_static.getSessionRI().joinJobGroup(Integer.parseInt(taArea0.getText()));
            if (jobGroupRI != null) {
                stringBuilder.append("<br/>Criado com sucesso!");
                lbLabel0.setText(stringBuilder.toString());
                ///Temos que verificar se já nao tivemos um worker neste jobgroup(evitar duplicação de esforços)
                ///Temos que verificar se as coins disponiveis no plafon sao suficientes(>10)

                if (jobGroupRI.getCoins() > 10) {
                    new WorkerImpl(JobShopClient_static.getSessionRI().showMyUsername(), jobGroupRI, JobShopClient_static);

                } else if (jobGroupRI.getCoins() == 10) {
                    //Se entrar aqui , significa que estamos no limite do plafon para a melhor solução
                    jobGroupRI.verify_winner();
                }

            } else {
                stringBuilder.append("Erro ao adicionar worker!");
                lbLabel0.setText(stringBuilder.toString());
            }
        } else {
            lbLabel0.setText(" ERR0 A ACEDER A CLASS SESSAORI :(<br/>");
        }
    }

    private void btDeleteexistinggroup(java.awt.event.ActionEvent evt) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html>DISPLAY:<br/> **Delete existing groupe** <br/>");
        lbLabel0.setText(stringBuilder.toString());
        if (JobShopClient_static.getSessionRI() != null) {
            stringBuilder.append("<br/>Nome do Grupo: ");
            lbLabel0.setText(stringBuilder.toString());
            String name = "";
            if (btSent.getModel().isPressed()) {
                name = taArea0.getText();
                if (name.compareTo("") == 0) return;
            }

            //String path = "edu/ufp/inf/sd/project/data/la04.txt";
            JobShopClient_static.getSessionRI().deleteJobGroup(Integer.parseInt(name));

            stringBuilder.append("<br/>JobGroup apagado com sucesso!");
            lbLabel0.setText(stringBuilder.toString());

        } else {
            lbLabel0.setText(" ERR0 A ACEDER A CLASS SESSAORI :(<br/>");
        }

    }

    private void btContinueexistinggroup(java.awt.event.ActionEvent evt) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html>DISPLAY:<br/> **Continue existing groupe** <br/>");
        lbLabel0.setText(stringBuilder.toString());

        if (JobShopClient_static.getSessionRI() != null) {
            stringBuilder.append("<br/>ID do Grupo: ");
            lbLabel0.setText(stringBuilder.toString());
            String id = "";
            if (btSent.getModel().isPressed()) {
                id = taArea0.getText();
                if (id.compareTo("") == 0) return;
            }

            ArrayList<String> groups = JobShopClient_static.getSessionRI().listJobGroups();
            stringBuilder.append("<br/>\t List of Groups:");

            for (String name : groups) {
                stringBuilder.append(name);
                stringBuilder.append("<br/>");
            }

            lbLabel0.setText(stringBuilder.toString());
            JobGroupRI jobGroupRI = JobShopClient_static.getSessionRI().joinJobGroup(Integer.parseInt(id));
            if (jobGroupRI != null) {
                GroupStatusState s = new GroupStatusState("CONTINUE");
                jobGroupRI.setState(s);
            }
        } else {
            lbLabel0.setText(" ERR0 A ACEDER A CLASS SESSAORI :(<br/>");

        }
    }

    private void btAddCoins(java.awt.event.ActionEvent evt) throws RemoteException {
        JobShopClient_static.getCoinsPayment(Integer.parseInt(taArea0.getText()));
        lbLabel1.setText("My Coins: " + sessionRI_static.showCoins());
    }

    private void btListUsers(java.awt.event.ActionEvent evt) throws RemoteException {
        StringBuilder stringBuilder = new StringBuilder(9999);
        stringBuilder.append("<html> DISPLAY:<br/>");
        lbLabel0.setText(stringBuilder.toString());
        if (sessionRI_static != null) {
            ArrayList<User> users = sessionRI_static.listUsers();
            stringBuilder.append("<br/>Users Online:");
            for (User a : users) {
                stringBuilder.append(a.getUsername());
                stringBuilder.append("<br/>");
            }
            stringBuilder.append("<br/>");
            stringBuilder.append("</html>");

            lbLabel0.setText(stringBuilder.toString());
        } else {
            lbLabel0.setText("<html> ERR0 A ACEDER A CLASS SESSAORI <br/>:( </html>");
        }
    }

    private void btLogout(java.awt.event.ActionEvent evt) throws RemoteException {
        JobShopClient_static.user_logout();
        dispose(); //sair da janela
    }
}