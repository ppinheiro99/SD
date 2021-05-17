package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.io.Serializable;

public class VisitorFoldersOperationDeleteFile implements VisitorFoldersOperationsI, Serializable {
    private String fileToDelete;
    private String fileToDeleteWithPrefix;

    public VisitorFoldersOperationDeleteFile(String folder){this.fileToDelete = folder;}

    @Override
    public Object visitConcreteElementStateBooks(ElementFolderRI element){
        SingletonFolderOperationsBooks s = ((ConcreteElementFolderBooksImpl)element).getStateBooksFolder();
        //Specific operation over Books folder
        //return s.deleteFile(fileToDeleteWithPrefix);
        return s.deleteFile(fileToDelete);
    }

    @Override
    public Object visitConcreteElementStateMagazines(ElementFolderRI element){
        SingletonFolderOperationsMagazines s = ((ConcreteElementFolderMagazinesImpl)element).getStateMagazinesFolder();
        //Specific operation over Books folder
        return s.deleteFile(fileToDeleteWithPrefix);

    }

    public String getFileToDelete() {
        return fileToDelete;
    }

    public void setFileToDelete(String fileToDelete) {
        this.fileToDelete = fileToDelete;
    }
}
