package ru.otus.homework.libraryJpa.repository;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpa.model.Author;

public interface AuthorDao {

    Long count();

    Optional<Author> getById(long id);

    Optional<Author> getByName(String name);

    Author save(Author author);

    List<Author> getAll();

    void deleteAuthorsWithoutBooks();
}