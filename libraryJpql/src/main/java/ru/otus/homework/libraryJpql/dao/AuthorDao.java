package ru.otus.homework.libraryJpql.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpql.model.Author;

public interface AuthorDao {

    Long count();

    Optional<Author> getById(long id);

    Optional<Long> getIdByName(String name);

    long save(Author author);

    List<Author> getAll();

    void deleteAuthorsWithoutBooks();
}