create table library_schema.books(
    id serial primary key,
    title text not null
)

create table library_schema.authors(
    id serial primary key,
    name text not null,
    surname text not null
)

create table library_schema.users(
     id serial primary key,
     name text not null,
     surname text not null
)

create table library_schema.users_books(
    id serial primary key,
    userId integer,
    bookId integer,
    foreign key (userId) references library_schema.users(id),
    foreign key (bookId) references library_schema.books(id)
)

create table library_schema.authors_books(
    id serial primary key,
    authorId integer,
    bookId integer,
    foreign key (authorId) references library_schema.authors(id),
    foreign key (bookId) references library_schema.books(id)
)