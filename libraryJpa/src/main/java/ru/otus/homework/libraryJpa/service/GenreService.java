package ru.otus.homework.libraryJpa.service;

public interface GenreService {

    String updateGenre(String id, String name);

    String getGenre(String id);

    String getAllGenres();

    long getGenresCount();
}