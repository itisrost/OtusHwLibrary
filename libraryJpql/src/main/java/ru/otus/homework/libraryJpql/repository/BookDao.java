package ru.otus.homework.libraryJpql.repository;

import java.util.List;

import ru.otus.homework.libraryJpql.model.Book;

public interface BookDao {

    Long count();

    Book getById(long id);

    long save(Book book);

    void delete(Book book);

    List<Book> getAll();
}