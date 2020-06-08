package ru.otus.homework.libraryJpql.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpql.model.Genre;

public interface GenreDao {

    Long count();

    Optional<Genre> getById(long id);

    Optional<Genre> getByName(String name);

    Genre save(Genre Genre);

    List<Genre> getAll();

    void deleteGenresWithoutBooks();
}