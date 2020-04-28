package ru.otus.homework.libraryJdbc.service;

public interface GenreService {

    String updateGenre(String id, String name);

    String getGenre(String id);

    String getAllGenres();

    String getGenresCount();
}