package ru.otus.homework.libraryJpql.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpql.model.Author;

public interface AuthorDao {

    Long count();

    Optional<Author> getById(long id);

    Optional<Author> getByName(String name);

    Author save(Author author);

    List<Author> getAll();

    void deleteAuthorsWithoutBooks();
}