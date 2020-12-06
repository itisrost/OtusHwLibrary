package ru.otus.homework.libraryMongo.service;

public interface AuthorService {

    String updateAuthor(String id, String name);

    String getAuthor(String id);

    String getAllAuthors();

    long getAuthorsCount();
}