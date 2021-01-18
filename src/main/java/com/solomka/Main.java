package com.solomka;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

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

    public static void main(String[] args) {
        SessionFactory sf = buildSessionFactory();
        //CRUD
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();
//        Book book = new Book("Moby Dick");
//        Author author = new Author("Herman", "Melville");
//        User user = new User("Oksana", "Solomka");
//        session.save(user);
//        transaction.commit();
//        session.close();
        User user = session.find(User.class, 1);
        System.out.println(user);
        transaction.commit();


        session.close();
    }

    public static SessionFactory buildSessionFactory(){
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Book.class);
        configuration.addAnnotatedClass(Author.class);
        configuration.addAnnotatedClass(User.class);
        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }
}
