package ru.otus.homework.libraryJdbc.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJdbc.model.Author;

public interface AuthorDao {

    Long count();

    Optional<Author> getById(long id);

    Optional<Long> getIdByName(String name);

    long save(Author author);

    List<Author> getAll();

    List<Author> getAllByBookId(long bookId);

    void deleteAuthorsWithoutBooks();

    void update(Author author);
}
