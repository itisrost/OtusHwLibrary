package ru.otus.homework.libraryJpa.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpa.model.Genre;

public interface GenreDao {

    Long count();

    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String name);

    Genre save(Genre Genre);

    List<Genre> getAll();

    void deleteGenresWithoutBooks();
}