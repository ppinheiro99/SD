package edu.ufp.inf.sd.rmi._06_visitor.client;

import edu.ufp.inf.sd.rmi._06_visitor.server.ElementFolderRI;
import edu.ufp.inf.sd.rmi._06_visitor.server.VisitorFoldersOperationsI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectStructureElementsFolders {
    public ArrayList<ElementFolderRI> elements = new ArrayList<>();

    public ObjectStructureElementsFolders(String serviceNames[]){
        for (String serviceName : serviceNames) {
            try {
                ElementFolderRI eRI = (ElementFolderRI) Naming.lookup(serviceName);
                this.addElementFolderRI(eRI);
            }catch (NotBoundException | MalformedURLException | RemoteException ex){
                Logger.getLogger(ObjectStructureElementsFolders.class.getName()).log(Level.WARNING,null,ex);
            }
        }
    }

    private final void addElementFolderRI(ElementFolderRI e) {
        this.elements.add(e);
    }

    public Boolean dispatchVisitorFoldersOperation(VisitorFoldersOperationsI visitor){
        for (ElementFolderRI element:elements) {
            try {
                element.acceptVisitor(visitor);
            }catch (RemoteException ex){
                Logger.getLogger(ObjectStructureElementsFolders.class.getName()).log(Level.WARNING,null,ex);
                return false;
            }
        }
        return true;
    }
}
