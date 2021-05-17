package edu.ufp.inf.sd.rmi._06_visitor.server;

public interface VisitorFoldersOperationsI {
    Object visitConcreteElementStateBooks(ElementFolderRI element);

    Object visitConcreteElementStateMagazines(ElementFolderRI element);

}
