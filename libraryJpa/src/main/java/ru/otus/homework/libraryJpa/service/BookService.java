package ru.otus.homework.libraryJpa.service;

public interface BookService {

    String saveBook(String title, String authors, String genres);

    String updateBook(String id, String title, String authors, String genres);

    String getBook(String id);

    String deleteBook(String id);

    String getAllBooks();

    long getBooksCount();
}