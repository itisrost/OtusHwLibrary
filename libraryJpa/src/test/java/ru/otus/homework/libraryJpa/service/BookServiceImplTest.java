package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Author;
import ru.otus.homework.libraryJpa.model.Book;
import ru.otus.homework.libraryJpa.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.libraryJpa.repository.AuthorRepository;
import ru.otus.homework.libraryJpa.repository.BookRepository;
import ru.otus.homework.libraryJpa.repository.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@DisplayName("Book Service должен")
class BookServiceImplTest {

    @Configuration
    private static class Config {

        @Bean
        public BookService bookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
            return new BookServiceImpl(bookRepository, authorRepository, genreRepository);
        }
    }

    public static final long ID_LONG = 1L;
    public static final String ID_STRING = "1";
    public static final String BOOK_NOT_FOUND = String.format("Book with id = %d not found :(", ID_LONG);
    public static final String SAVE_SUCCESSFUL = String.format("Book saved successfully with id = %d.", ID_LONG);
    public static final String UPDATE_SUCCESSFUL = String.format("Book with id = %s updated successfully!", ID_STRING);
    public static final String DELETE_SUCCESSFUL = String.format("Book with id = %s deleted successfully.", ID_STRING);
    public static final String ID_MUST_BE_A_NUMBER = "Book id must be a number!";
    public static final String JEFF_NOON = "Jeff Noon";
    public static final List<Author> EXPECTED_AUTHORS = Arrays.asList(new Author(ID_LONG, JEFF_NOON));
    public static final String CYBERPUNK = "Cyberpunk";
    public static final List<Genre> EXPECTED_GENRES = Arrays.asList(new Genre(ID_LONG, CYBERPUNK));
    public static final String TITLE = "Vurt";
    public static final Book EXPECTED_BOOK = new Book(ID_LONG, TITLE, EXPECTED_AUTHORS, EXPECTED_GENRES);

    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("сохранять книгу")
    void shouldSaveBook() {

        when(authorRepository.findByName(any())).thenReturn(new Author(ID_LONG, JEFF_NOON));
        when(genreRepository.findByName(any())).thenReturn(new Genre(ID_LONG, CYBERPUNK));
        when(bookRepository.save(any())).thenReturn(EXPECTED_BOOK);

        assertThat(bookService.saveBook(TITLE, JEFF_NOON, CYBERPUNK)).isEqualTo(SAVE_SUCCESSFUL);
    }

    @Test
    @DisplayName("обновлять книгу")
    void shouldUpdateBook() {
        when(authorRepository.findByName(any())).thenReturn(new Author(ID_LONG, JEFF_NOON));
        when(genreRepository.findByName(any())).thenReturn(new Genre(ID_LONG, CYBERPUNK));

        assertThat(bookService.updateBook(ID_STRING, TITLE, JEFF_NOON, CYBERPUNK)).isEqualTo(UPDATE_SUCCESSFUL);
    }


    @Test
    @DisplayName("возвращать книгу по id если она есть в БД")
    void shouldReturnExpectedBookById() {
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_BOOK));

        assertThat(bookService.getBook(ID_STRING)).isEqualTo(EXPECTED_BOOK.toString());
    }

    @Test
    @DisplayName("возвращать ошибку, если id книги не является числом")
    void shouldReturnExceptionIfBookIdIsNotANumber() {
        assertThrows(InvalidParameterException.class, () -> bookService.getBook("abc"), ID_MUST_BE_A_NUMBER);
    }

    @Test
    @DisplayName("сообщать, если искомой книги с введённым id нет в БД")
    void shouldNoticeIfBookNotFound() {
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(bookService.getBook(ID_STRING)).isEqualTo(BOOK_NOT_FOUND);
    }

    @Test
    @DisplayName("удалять книгу, если она есть в БД")
    void shouldDeleteBookIfExists() {
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_BOOK));
        assertThat(bookService.deleteBook(ID_STRING)).isEqualTo(DELETE_SUCCESSFUL);
    }

    @Test
    @DisplayName("сообщать, если удаляемой книги с введённым id нет в БД")
    void shouldNoticeIfBookToDeleteIsNotFound() {
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(bookService.getBook(ID_STRING)).isEqualTo(BOOK_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать список всех книг из БД")
    void shouldReturnAllBooks() {
        List<Book> books = List.of(new Book(2, "Pollen", new ArrayList<>(), new ArrayList<>()), EXPECTED_BOOK);
        String expectedString = books.stream().map(Book::toString).collect(Collectors.joining("\n"));
        when(bookRepository.findAll()).thenReturn(books);

        assertThat(bookService.getAllBooks()).isEqualTo(expectedString);
    }

    @Test
    @DisplayName("сообщать, если в БД нет книг")
    void shouldNoticeIfNoBooksInDB() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());

        assertThat(bookService.getAllBooks()).isEqualTo("There is no books in library :(");
    }

    @Test
    @DisplayName("возвращать верное количество книг из БД")
    void shouldReturnCorrectBooksCount() {
        when(bookRepository.count()).thenReturn(ID_LONG);

        assertThat(bookService.getBooksCount()).isEqualTo(ID_LONG);
    }
}