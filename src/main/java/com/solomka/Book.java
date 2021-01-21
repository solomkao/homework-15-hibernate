package com.solomka;

import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "authors_books",
            joinColumns = @JoinColumn(name = "bookid"),
            inverseJoinColumns = @JoinColumn(name = "authorid"))
    private List<Author> authorList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "users_books",
            joinColumns = @JoinColumn(name = "bookid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    private List<User> userList = new ArrayList<>();

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public void addAuthor(Author author) {
        this.authorList.add(author);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return '\'' + title + "\' was written by " +
                authorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return title.equals(book.title) && CollectionUtils.isEqualCollection(authorList, book.authorList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorList);
    }
}
