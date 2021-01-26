create table books(
    id serial primary key,
    title text not null
)

create table authors(
    id serial primary key,
    name text not null,
    surname text not null
)

create table users(
     id serial primary key,
     name text not null,
     surname text not null
)

create table users_books(
   id serial primary key,
   userId integer,
   bookId integer,
   foreign key (userId) references users(id),
   foreign key (bookId) references books(id),
   unique (bookId,userId)
)

create table authors_books(
     id serial primary key,
     authorId integer,
     bookId integer,
     foreign key (authorId) references authors(id),
     foreign key (bookId) references books(id),
     unique (authorId, bookId)
)

SELECT *
FROM books
         join authors_books ab on books.id = ab.bookid
         join authors a on a.id = ab.authorid
         join users_books ub on books.id = ub.bookid
where ub.userid = 1;

select books.title, authors.name, authors.surname from books, authors, users, authors_books, users_books where
        userid = 1 and users_books.userid = userid and users_books.bookid
        = books.id and authors_books.authorid = authors.id and authors_books.bookid = books.id