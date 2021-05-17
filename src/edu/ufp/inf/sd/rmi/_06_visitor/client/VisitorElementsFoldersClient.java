package edu.ufp.inf.sd.rmi._06_visitor.client;

import edu.ufp.inf.sd.rmi._01_helloworld.client.HelloWorldClient;
import edu.ufp.inf.sd.rmi._01_helloworld.server.HelloWorldRI;
import edu.ufp.inf.sd.rmi._06_visitor.server.ElementFolderRI;
import edu.ufp.inf.sd.rmi._06_visitor.server.ElementsFoldersServer;
import edu.ufp.inf.sd.rmi._06_visitor.server.VisitorFoldersOperationCreateFile;
import edu.ufp.inf.sd.rmi._06_visitor.server.VisitorFoldersOperationDeleteFile;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitorElementsFoldersClient {

    public static ElementFolderRI booksRI;
    private SetupContextRMI contextRMI;

    public static void main(String[] args) {
        /**
                                                CÓDIGO DO STOR
              String serviceNames[] = {
                  ElementsFoldersServer.SERVICE_NAME_ELEMENT_STATE_BOOKS,
                  ElementsFoldersServer.SERVICE_NAME_ELEMENT_STATE_MAGAZINES
              };

               ObjectStructureElementsFolders elements = new ObjectStructureElementsFolders(serviceNames);

               String newFile = "NewFile.txt";
               VisitorFoldersOperationCreateFile vsfocf = new VisitorFoldersOperationCreateFile(newFile);
               Boolean b = elements.dispatchVisitorFoldersOperation(vsfocf);

               System.out.println("VisitorCreateFolderClient - main(): create file " +newFile + "on server = " + b);

                                                **************
         **/

        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.inf.rmi._06_visitor.server.HelloWorldClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            VisitorElementsFoldersClient hwc = new VisitorElementsFoldersClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }

    }

    public VisitorElementsFoldersClient(String args[]) {
        try {
            //List ans set args
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(VisitorElementsFoldersClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR lookup service @ {0}", serviceUrl);

                //============ Get proxy MAIL_TO_ADDR HelloWorld service ============
                booksRI = (ElementFolderRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return booksRI;
    }

    private void playService() {
        try {
            // Delete Book
            //VisitorFoldersOperationDeleteFile v1 = new VisitorFoldersOperationDeleteFile("nome.txt");
            // Create Book
            VisitorFoldersOperationCreateFile v2 = new VisitorFoldersOperationCreateFile("Books.txt");
            // Accept Visitor
            //booksRI.acceptVisitor(v1);
            booksRI.acceptVisitor(v2);

            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR finish, bye. ;)");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}