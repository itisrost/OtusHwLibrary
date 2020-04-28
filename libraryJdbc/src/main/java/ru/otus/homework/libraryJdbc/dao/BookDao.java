package ru.otus.homework.libraryJdbc.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJdbc.model.Book;

public interface BookDao {

    Long count();

    Optional<Book> getById(long id);

    long save(Book book);

    void update(Book book);

    void deleteById(long id);

    List<Book> getAll();

    void saveBookRelations(long bookId, List<Long> authorIds, List<Long> genreIds);
}