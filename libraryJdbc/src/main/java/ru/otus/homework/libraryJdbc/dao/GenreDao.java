package ru.otus.homework.libraryJdbc.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJdbc.model.Genre;

public interface GenreDao {

    Long count();

    Optional<Genre> getById(long id);

    Optional<Long> getIdByName(String name);

    long save(Genre Genre);

    List<Genre> getAll();

    List<Genre> getAllByBookId(long bookId);

    void deleteGenresWithoutBooks();

    void update(Genre genre);
}