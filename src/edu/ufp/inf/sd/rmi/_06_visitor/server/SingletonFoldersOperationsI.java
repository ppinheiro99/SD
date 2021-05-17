package edu.ufp.inf.sd.rmi._06_visitor.server;

public interface SingletonFoldersOperationsI {
    public Boolean createFile(String fname);
    public Boolean deleteFile(String fname);

    /* Other file system operations here after... */
}
