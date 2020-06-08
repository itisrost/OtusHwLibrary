package ru.otus.homework.libraryJpql.service;

public interface AuthorService {

    String updateAuthor(String id, String name);

    String getAuthor(String id);

    String getAllAuthors();

    String getAuthorsCount();
}