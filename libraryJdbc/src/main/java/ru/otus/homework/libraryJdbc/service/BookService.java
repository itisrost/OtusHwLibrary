package ru.otus.homework.libraryJdbc.service;

public interface BookService {

    String saveBook(String title, String authors, String genres);

    String updateBook(String id, String title, String authors, String genres);

    String getBook(String id);

    String deleteBook(String id);

    String getAllBooks();

    String getBooksCount();
}