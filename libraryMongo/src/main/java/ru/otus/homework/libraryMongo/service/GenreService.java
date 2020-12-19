package ru.otus.homework.libraryMongo.service;

public interface GenreService {

    String updateGenre(String id, String name);

    String getGenre(String id);

    String getAllGenres();

    long getGenresCount();
}