package ru.otus.homework.libraryMongo.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.otus.homework.libraryMongo.model.Author;
import ru.otus.homework.libraryMongo.model.Book;
import ru.otus.homework.libraryMongo.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.libraryMongo.repository.AuthorRepository;
import ru.otus.homework.libraryMongo.repository.BookRepository;
import ru.otus.homework.libraryMongo.repository.CommentRepository;
import ru.otus.homework.libraryMongo.repository.GenreRepository;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND = "Book with id = %s not found :(";
    public static final String SAVED_SUCCESSFULLY = "Book saved successfully with id = %s.";
    public static final String UPDATED_SUCCESSFULLY = "Book with id = %s updated successfully!";
    public static final String DELETED_SUCCESSFULLY = "Book with id = %s deleted successfully.";
    public static final String NO_BOOKS_IN_LIBRARY = "There is no books in library :(";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public String saveBook(String title, String authorsString, String genresString) {

        Book book = bookRepository.save(new Book(title, saveAuthorsFromString(authorsString), saveGenresFromString(genresString)));

        return String.format(SAVED_SUCCESSFULLY, book.getId());
    }

    @Override
    @Transactional
    public String updateBook(String id, String title, String authorsString, String genresString) {

        bookRepository.save(new Book(id, title, saveAuthorsFromString(authorsString), saveGenresFromString(genresString)));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getBook(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get().toString();
        } else {
            return String.format(BOOK_NOT_FOUND, id);
        }
    }

    @Override
    @Transactional
    public String deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            commentRepository.deleteAllByBookId(id);
            return String.format(DELETED_SUCCESSFULLY, id);
        } else {
            return String.format(BOOK_NOT_FOUND, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            return NO_BOOKS_IN_LIBRARY;
        } else {
            return books.stream().map(Book::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getBooksCount() {
        return bookRepository.count();
    }

    private List<Author> saveAuthorsFromString(String authorsString) {
        if (StringUtils.isNotEmpty(authorsString)) {
            return Arrays.stream(authorsString.split(","))
                    .map(String::trim)
                    .map(authorName -> Objects.requireNonNullElseGet(authorRepository.findByName(authorName), () -> authorRepository.save(new Author(authorName))))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private List<Genre> saveGenresFromString(String genresString) {
        if (StringUtils.isNotEmpty(genresString)) {
            return Arrays.stream(genresString.split(","))
                    .map(String::trim)
                    .map(genreName -> Objects.requireNonNullElseGet(genreRepository.findByName(genreName), () -> genreRepository.save(new Genre(genreName))))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}