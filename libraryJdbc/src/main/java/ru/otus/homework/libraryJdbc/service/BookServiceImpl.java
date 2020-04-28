package ru.otus.homework.libraryJdbc.service;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJdbc.dao.AuthorDao;
import ru.otus.homework.libraryJdbc.dao.BookDao;
import ru.otus.homework.libraryJdbc.dao.GenreDao;
import ru.otus.homework.libraryJdbc.model.Author;
import ru.otus.homework.libraryJdbc.model.Book;
import ru.otus.homework.libraryJdbc.model.Genre;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND = "Book with id = %s not found :(";
    public static final String SAVED_SUCCESSFULLY = "Book saved successfully with id = %d.";
    public static final String UPDATED_SUCCESSFULLY = "Book with id = %s updated successfully!";
    public static final String DELETED_SUCCESSFULLY = "Book with id = %s deleted successfully.";
    public static final String NO_BOOKS_IN_LIBRARY = "There is no books in library :(";
    public static final String ID_MUST_BE_A_NUMBER = "Book id must be a number!";

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public String saveBook(String title, String authors, String genres) {
        long bookId = bookDao.save(new Book(title));

        saveAuthorsAndGenres(bookId, authors, genres);

        return String.format(SAVED_SUCCESSFULLY, bookId);
    }

    @Override
    public String updateBook(String id, String title, String authors, String genres) {
        long bookId = parseId(id);

        bookDao.update(new Book(bookId, title));

        saveAuthorsAndGenres(bookId, authors, genres);

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    public String getBook(String id) {
        long bookId = parseId(id);

        Optional<Book> optionalBook = bookDao.getById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setAuthors(authorDao.getAllByBookId(bookId));
            book.setGenres(genreDao.getAllByBookId(bookId));
            return book.toString();
        } else {
            return String.format(BOOK_NOT_FOUND, id);
        }
    }

    @Override
    public String deleteBook(String id) {
        long bookId = parseId(id);

        if (bookDao.getById(bookId).isPresent()) {
            bookDao.deleteById(bookId);
            authorDao.deleteAuthorsWithoutBooks();
            genreDao.deleteGenresWithoutBooks();
            return String.format(DELETED_SUCCESSFULLY, id);
        } else {
            return String.format(BOOK_NOT_FOUND, id);
        }
    }

    @Override
    public String getAllBooks() {
        List<Book> books = bookDao.getAll();

        if (books.isEmpty()) {
            return NO_BOOKS_IN_LIBRARY;
        } else {
            books.forEach(book -> book.setAuthors(authorDao.getAllByBookId(book.getId())));
            books.forEach(book -> book.setGenres(genreDao.getAllByBookId(book.getId())));
            return books.stream().map(Book::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    public String getBooksCount() {
        return bookDao.count().toString();
    }

    private void saveAuthorsAndGenres(long bookId, String authors, String genres) {
        List<Long> authorIds = Arrays.stream(authors.split(","))
                .map(String::trim)
                .map(authorName -> authorDao.getIdByName(authorName).orElseGet(() -> authorDao.save(new Author(authorName))))
                .collect(Collectors.toList());

        List<Long> genreIds = Arrays.stream(genres.split(","))
                .map(String::trim)
                .map(genreName -> genreDao.getIdByName(genreName).orElseGet(() -> genreDao.save(new Genre(genreName))))
                .collect(Collectors.toList());

        bookDao.saveBookRelations(bookId, authorIds, genreIds);
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}