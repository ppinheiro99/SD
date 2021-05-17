package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<Book> books  = new ArrayList();
    private final ArrayList<User> users = new ArrayList();


    public DBMockup() {
        //Add 3 books
        books.add(new Book("Distributed Systems: principles and paradigms", "Tanenbaum"));
        books.add(new Book("Distributed Systems: concepts and design", "Colouris"));
        books.add(new Book("Distributed Computing Networks", "Tanenbaum"));
        //Add one user
        users.add(new User("guest", "ufp"));
    }

    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new User(u, p));
        }
    }

    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public void insert(String t, String a) {
        books.add(new Book(t, a));
    }

    public Book[] select(String t, String a) {
        Book[] abooks = null;
        ArrayList<Book> vbooks = new ArrayList();
        // Find books that match
        for (int i = 0; i < books.size(); i++) {
            Book book = (Book) books.get(i);
            System.out.println("DB - select(): book[" + i + "] = " + book.getTitle() + ", " + book.getAuthor());
            if (book.getTitle().toLowerCase().contains(t.toLowerCase()) && book.getAuthor().toLowerCase().contains(a.toLowerCase())) {
                System.out.println("DB - select(): add book[" + i + "] = " + book.getTitle() + ", " + book.getAuthor());
                vbooks.add(book);
            }
        }
        // Copy Vector->Array
        abooks = new Book[vbooks.size()];
        for (int i = 0; i < vbooks.size(); i++) {
            abooks[i] = (Book) vbooks.get(i);
        }
        return abooks;
    }

    public Book findBook(String t, String a) {
        for (int i = 0; i < books.size(); i++) {
            Book book = (Book) books.get(i);
            if (book.getTitle().toLowerCase().contains(t.toLowerCase()) && book.getAuthor().toLowerCase().contains(a.toLowerCase())) {
                return book;
            }
        }
        return null;
    }
}
