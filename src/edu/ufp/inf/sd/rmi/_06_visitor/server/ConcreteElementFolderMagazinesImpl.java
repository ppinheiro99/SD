package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConcreteElementFolderMagazinesImpl extends UnicastRemoteObject implements ElementFolderRI {
    private final SingletonFolderOperationsMagazines stateMagazinesFolder;

    protected ConcreteElementFolderMagazinesImpl(String MagazinesFolder) throws RemoteException {
        super();
        this.stateMagazinesFolder = SingletonFolderOperationsMagazines.createSingletonFolderOperationsMagazines(MagazinesFolder);
    }

    @Override
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException{
        Object o = visitor.visitConcreteElementStateMagazines(this);
        return o;
    }

    public SingletonFolderOperationsMagazines getStateMagazinesFolder() {
        return stateMagazinesFolder;
    }
}
