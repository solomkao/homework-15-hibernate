package com.solomka;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.List;

/*
Console app: Library
Entities : Book , Author, User
Important: 1 book may have more than 1 Author. User may have list of Books

Write simple CRUD (create/read/update/delete) functionality for Author /Book/ User
getAll/getById for Author and Book
Add new Book for User
Show all User books

DB: any relational DB
No need to make menu as in Jira Project, u can test all functions in main method

 */

public class Main {
    static SessionFactory sf;

    public static void main(String[] args) {
        sf = buildSessionFactory();

//        Create User, Book, Author
        User biden = new User("Joseph", "Biden");
        addUser(biden);

        Book oneHundredYearsOfSolitude = new Book("One Hundred Years of Solitude");
        Author gabrielGM = new Author("Gabriel", "Garcia Marquez");
        oneHundredYearsOfSolitude.addAuthor(gabrielGM);
        addBook(oneHundredYearsOfSolitude);

//        Update user's booklist
        updateUsersBookList("Oksana", "Solomka", oneHundredYearsOfSolitude);
        updateUsersBookList("Joseph", "Biden", oneHundredYearsOfSolitude);

        Book littleGoldenCalf = new Book("Золотой теленок");
        littleGoldenCalf.addAuthor(new Author("Илья", "Ильф"));
        littleGoldenCalf.addAuthor(new Author("Евгений", "Петров"));
        updateUsersBookList("Oksana", "Solomka", littleGoldenCalf);
        updateUsersBookList("Joseph", "Biden", littleGoldenCalf);

//        Show user's info
        User oksanaSolomka = getUser("Oksana", "Solomka");
        System.out.println("\tFOUND -> " + oksanaSolomka);

//        Delete book
        Book deletedBook = deleteBook("One Hundred Years of Solitude");
        System.out.println("\tDELETED BOOK -> " + deletedBook);

//        Show user's info after removing book
        oksanaSolomka = getUser("Oksana", "Solomka");
        System.out.println("\tFOUND -> " + oksanaSolomka);

//        Delete author
        Author author = deleteAuthor("Илья", "Ильф");
        System.out.println("author = " + author);

//        Delete user
        User deletedUser = deleteUser("Oksana", "Solomka");
        System.out.println("\tDELETED USER -> " + deletedUser);

//        Update
        updateUser("Joseph", "Mary", "Biden", "Closer");
        updateBook("Voice of America", "Voice of Ukraine");
        updateAuthor("Евгений", "Илья", "Петров", "Ильф");
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Author.class);
        configuration.addAnnotatedClass(User.class);
        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    public static void addAllEntities() {
        Session session = sf.openSession();
        session.beginTransaction();
        Book book = new Book("Test Book");
        Author author1 = new Author("Ivan", "Pugovkin");
        book.addAuthor(author1);
        User newUser = new User("Svitlana", "Kolesnikova");
        newUser.addBook(book);
        session.save(book);
        session.save(author1);
        session.save(newUser);
        session.getTransaction().commit();
        session.close();
    }

    public static void addUser(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }

    public static void addBook(Book book) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(book);
        for (Author author : book.getAuthorList()) {
            session.save(author);
        }
        session.getTransaction().commit();
        session.close();
    }


    public static void updateUsersBookList(String name, String surname, Book book) {
        User user = getUser(name, surname);
        if (user != null) {
            if (user.getBookList().contains(book)) {
                return;
            }
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            Book foundBook = getBook(book.getTitle());
            if (foundBook != null && foundBook.equals(book)) {
                user.addBook(foundBook);
            } else {
                session.save(book);
                for (Author author : book.getAuthorList()) {
                    session.save(author);
                }
                user.addBook(book);
            }
            session.update(user);
            transaction.commit();
            session.close();
        }
    }

    public static User deleteUser(String name, String surname) {
        User user = getUser(name, surname);
        if (user != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
            session.close();
        }
        return user;
    }

    public static User getUser(String name, String surname) {
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("name"), name);
        predicates[1] = cb.equal(root.get("surname"), surname);
        cr.select(root).where(predicates);
        Query<User> query = session.createQuery(cr);
        List<User> users = query.getResultList();
        transaction.commit();
        session.close();
        return users.isEmpty() ? null : users.get(users.size() - 1);
    }

    public static Book deleteBook(String title) {
        Book book = getBook(title);
        if (book != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(book);
            transaction.commit();
            session.close();
        }
        return book;
    }

    public static Book getBook(String title) {
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> root = cr.from(Book.class);
        cr.select(root).where(cb.equal(root.get("title"), title));
        Query<Book> query = session.createQuery(cr);
        List<Book> books = query.getResultList();
        transaction.commit();
        session.close();
        return books.isEmpty() ? null : books.get(books.size() - 1);
    }

    public static Author deleteAuthor(String name, String surname) {
        Author author = getAuthor(name, surname);
        if (author != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            session.delete(author);
            transaction.commit();
            session.close();
        }
        return author;
    }

    public static Author getAuthor(String name, String surname) {
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> cr = cb.createQuery(Author.class);
        Root<Author> root = cr.from(Author.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("name"), name);
        predicates[1] = cb.equal(root.get("surname"), surname);
        cr.select(root).where(predicates);
        Query<Author> query = session.createQuery(cr);
        List<Author> authors = query.getResultList();
        transaction.commit();
        session.close();
        return authors.isEmpty() ? null : authors.get(authors.size() - 1);
    }

    public static void updateUser(String oldName, String newName,
                                  String oldSurname, String newSurname) {
        User user = getUser(oldName, oldSurname);
        if (user != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            user.setName(newName);
            user.setSurname(newSurname);
            session.update(user);
            transaction.commit();
            session.close();
        }
    }

    public static void updateAuthor(String oldName, String newName,
                                    String oldSurname, String newSurname) {
        Author author = getAuthor(oldName, oldSurname);
        if (author != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            author.setName(newName);
            author.setSurname(newSurname);
            session.update(author);
            transaction.commit();
            session.close();
        }
    }

    public static void updateBook(String oldTitle, String newTitle) {
        Book book = getBook(oldTitle);
        if (book != null) {
            Session session = sf.openSession();
            Transaction transaction = session.beginTransaction();
            book.setTitle(newTitle);
            session.update(book);
            transaction.commit();
            session.close();
        }
    }
}
