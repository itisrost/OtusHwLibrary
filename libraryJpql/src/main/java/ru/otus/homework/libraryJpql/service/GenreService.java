package ru.otus.homework.libraryJpql.service;

public interface GenreService {

    String updateGenre(String id, String name);

    String getGenre(String id);

    String getAllGenres();

    String getGenresCount();
}