package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.io.Serializable;

public class VisitorFoldersOperationCreateFile implements VisitorFoldersOperationsI, Serializable {
    private String fileToCreate;
    private String fileToCreateWithPrefix;

    public VisitorFoldersOperationCreateFile(String folder){this.fileToCreate = folder;}

    @Override
    public Object visitConcreteElementStateBooks(ElementFolderRI element){
        SingletonFolderOperationsBooks s = ((ConcreteElementFolderBooksImpl)element).getStateBooksFolder();
        fileToCreateWithPrefix = "VisitorBook_"+fileToCreate;
        //Specific operation over Books folder
        return s.createFile(fileToCreateWithPrefix);
        //return s.createFile(fileToCreate);
    }

    @Override
    public Object visitConcreteElementStateMagazines(ElementFolderRI element){
        SingletonFolderOperationsMagazines s = ((ConcreteElementFolderMagazinesImpl)element).getStateMagazinesFolder();
        fileToCreateWithPrefix = "VisitorMagazine_"+fileToCreate;
        //Specific operation over Books folder
        return s.createFile(fileToCreateWithPrefix);

    }

    public String getFileToCreate() {
        return fileToCreate;
    }

    public void setFileToCreate(String fileToCreate) {
        this.fileToCreate = fileToCreate;
    }
}
