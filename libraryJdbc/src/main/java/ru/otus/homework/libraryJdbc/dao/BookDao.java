package ru.otus.homework.libraryJdbc.dao;

import java.util.List;

import ru.otus.homework.libraryJdbc.model.Book;

public interface BookDao {

    Long count();

    Book getById(long id);

    long save(Book book);

    void update(Book book);

    void deleteById(long id);

    List<Book> getAll();
}