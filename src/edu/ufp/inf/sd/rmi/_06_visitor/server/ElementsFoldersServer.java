package edu.ufp.inf.sd.rmi._06_visitor.server;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/* public class ElementsFoldersServer {

    public static final String SERVICE_NAME_ELEMENT_STATE_BOOKS = "rmi://localhost:1098/VisitorBooksService";
    public static final String SERVICE_NAME_ELEMENT_STATE_MAGAZINES = "rmi://localhost:1098/VisitorMagazinesService";
    public ElementFolderRI elementBooksRI;
    public ElementFolderRI elementMagazinesRI;

    public static void main(String[] args) {
      try {
          // Create and install a security manager
          if(System.getSecurityManager() == null){
              System.setSecurityManager(new SecurityManager());
          }
          // String hostIP = InetAddress.getLocalHost().getHostAdress();

          System.out.println("ElementFolderServer - Constructor(): register " + SERVICE_NAME_ELEMENT_STATE_BOOKS);
          System.out.println("ElementFolderServer - Constructor(): register " + SERVICE_NAME_ELEMENT_STATE_MAGAZINES);


          Naming.rebind(SERVICE_NAME_ELEMENT_STATE_BOOKS, elementBooksRI);
          Naming.rebind(SERVICE_NAME_ELEMENT_STATE_MAGAZINES, elementMagazinesRI);

          System.out.println("ElementFolderServer - Constructor(): waiting for visitors...");
      } catch (RemoteException | MalformedURLException e) {
          Logger.getLogger(ElementsFoldersServer.class.getName()).log(Level.WARNING,e.toString());
      }
    }
*/
public class ElementsFoldersServer {

    public static final String SERVICE_NAME_ELEMENT_STATE_BOOKS = "rmi://localhost:1098/VisitorBooksService";
    public static final String SERVICE_NAME_ELEMENT_STATE_MAGAZINES = "rmi://localhost:1098/VisitorMagazinesService";
    public ElementFolderRI elementBooksRI;
    public ElementFolderRI elementMagazinesRI;
    private SetupContextRMI contextRMI;

    public static void main(String[] args) {
        if (args != null && args.length < 3) {
            System.err.println("usage: java [options] edu.ufp.sd._01_helloworld.server.HelloWorldServer <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Create Servant ============
            ElementsFoldersServer hws = new ElementsFoldersServer(args);
            //2. ============ Rebind servant on rmiregistry ============
            hws.rebindService();
        }
    }

    public ElementsFoldersServer(String args[]) {
        try {
            //============ List and Set args ============
            SetupContextRMI.printArgs(this.getClass().getName(), args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //============ Create a context for RMI setup ============
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void rebindService() {
        try {
            //Get proxy MAIL_TO_ADDR rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Bind service on rmiregistry and wait for calls
            if (registry != null) {
                //============ Create Servant ============
                elementBooksRI = new ConcreteElementFolderBooksImpl("C:\\Users\\ppinh\\IdeaProjects\\SD\\test\\Books");
                //elementMagazinesRI = new ConcreteElementFolderMagazinesImpl("C:/Users/ppinh/IdeaProjects/SD/test/Magazines");

                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going MAIL_TO_ADDR rebind service @ {0}", serviceUrl);

                //============ Rebind servant ============
                //Naming.bind(serviceUrl, helloWorldRI);
                registry.rebind(serviceUrl, elementBooksRI);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "service bound and running. :)");
            } else {
                //System.out.println("HelloWorldServer - Constructor(): create registry on port 1099");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void loadProperties() throws IOException {

        Logger.getLogger(Thread.currentThread().getName()).log(Level.INFO, "goig MAIL_TO_ADDR load props...");
        // create and load default properties
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream("defaultproperties.txt");
        defaultProps.load(in);
        in.close();

        BiConsumer<Object, Object> bc = (key, value) ->{
            System.out.println(key.toString()+"="+value.toString());
        };
        defaultProps.forEach(bc);

        // create application properties with default
        Properties props = new Properties(defaultProps);

        FileOutputStream out = new FileOutputStream("defaultproperties2.txt");
        props.store(out, "---No Comment---");
        out.close();
    }
}
