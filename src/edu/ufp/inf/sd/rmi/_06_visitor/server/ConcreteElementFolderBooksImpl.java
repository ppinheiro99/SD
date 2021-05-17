package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConcreteElementFolderBooksImpl extends UnicastRemoteObject implements ElementFolderRI {

    private final SingletonFolderOperationsBooks stateBooksFolder;

    protected ConcreteElementFolderBooksImpl(String booksFolder) throws RemoteException {
        super();
        this.stateBooksFolder = SingletonFolderOperationsBooks.createSingletonFolderOperationsBooks(booksFolder);
    }

    @Override
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException{
        Object o = visitor.visitConcreteElementStateBooks(this);
        return o;
    }

    public SingletonFolderOperationsBooks getStateBooksFolder() {
        return stateBooksFolder;
    }
}
