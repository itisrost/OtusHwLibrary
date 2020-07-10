package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.repository.AuthorDao;
import ru.otus.homework.libraryJpa.model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@DisplayName("Author Service должен")
class AuthorServiceImplTest {

    @Configuration
    private static class Config {

        @Bean
        public AuthorService authorService(AuthorDao authorDao){
            return new AuthorServiceImpl(authorDao);
        }
    }

    public static final long ID_LONG = 3L;
    public static final String ID_STRING = "3";
    public static final String AUTHOR_NOT_FOUND = String.format("Author with id = %d not found :(", ID_LONG);
    public static final String UPDATE_SUCCESSFUL = String.format("Author with id = %s updated successfully!", ID_STRING);
    public static final String ID_MUST_BE_A_NUMBER = "Author id must be a number!";
    public static final String ANTHONY_BURGESS = "Anthony Burgess";
    public static final Author EXPECTED_AUTHOR = new Author(ID_LONG, ANTHONY_BURGESS);

    @MockBean
    private AuthorDao authorDao;

    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("обновлять информацию по автору")
    void shouldUpdateAuthor() {

        assertThat(authorService.updateAuthor(ID_STRING, ANTHONY_BURGESS)).isEqualTo(UPDATE_SUCCESSFUL);
    }

    @Test
    @DisplayName("возвращать автора по id, если он есть в БД")
    void shouldReturnExpectedAuthorById() {
        when(authorDao.getById(3)).thenReturn(Optional.of(EXPECTED_AUTHOR));

        assertThat(authorService.getAuthor(ID_STRING)).isEqualTo(EXPECTED_AUTHOR.toString());
    }

    @Test
    @DisplayName("возвращать ошибку, если id автора не является числом")
    void shouldReturnExceptionIfAuthorIdIsNotANumber() {
        assertThrows(InvalidParameterException.class, () -> authorService.getAuthor("abc"), ID_MUST_BE_A_NUMBER);
    }

    @Test
    @DisplayName("сообщать, если автора с введённым id нет в БД")
    void shouldNoticeIfAuthorNotFound() {
        when(authorDao.getById(ID_LONG)).thenReturn(null);

        assertThat(authorService.getAuthor(ID_STRING)).isEqualTo(AUTHOR_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать список всех авторов из БД")
    void shouldReturnAllAuthors() {
        List<Author> authors = List.of(new Author(2, "Jeff Noon"), EXPECTED_AUTHOR);
        String expectedString = authors.stream().map(Author::toString).collect(Collectors.joining("\n"));
        when(authorDao.getAll()).thenReturn(authors);

        assertThat(authorService.getAllAuthors()).isEqualTo(expectedString);
    }

    @Test
    @DisplayName("сообщать, если в БД нет авторов")
    void shouldNoticeIfNoAuthorsInDB() {
        when(authorDao.getAll()).thenReturn(new ArrayList<>());

        assertThat(authorService.getAllAuthors()).isEqualTo("There is no authors in library :(");
    }

    @Test
    @DisplayName("возвращать верное количество авторов из БД")
    void shouldReturnCorrectAuthorsCount() {
        when(authorDao.count()).thenReturn(ID_LONG);

        assertThat(authorService.getAuthorsCount()).isEqualTo(ID_STRING);
    }
}