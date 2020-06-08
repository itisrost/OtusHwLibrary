package ru.otus.homework.libraryJpa.dao;

import java.util.List;

import ru.otus.homework.libraryJpa.model.Book;

public interface BookDao {

    Long count();

    Book getById(long id);

    long save(Book book);

    void delete(Book book);

    List<Book> getAll();
}