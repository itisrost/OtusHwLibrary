package ru.otus.homework.libraryJdbc.dao;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJdbc.model.Author;
import ru.otus.homework.libraryJdbc.model.Book;
import ru.otus.homework.libraryJdbc.model.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Book Jdbc Dao должен")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private GenreDao genreDao;

    public static final long NEW_BOOK_ID = 5L;
    public static final long EXPECTED_ID = 2L;
    public static final List<Author> EXPECTED_AUTHORS = Collections.singletonList(new Author(EXPECTED_ID, "Jeff Noon"));
    public static final List<Genre> EXPECTED_GENRES = Collections.singletonList(new Genre(EXPECTED_ID, "Cyberpunk"));
    public static final String CLOCKWORK_ORANGE = "Clockwork orange";
    public static final String EXPECTED_TITLE = "Vurt";

    @Autowired
    private BookDaoJdbc dao;

    @Test
    @DisplayName("верно считать количество книг в БД")
    void shouldReturnExpectedBookCount() {
        assertEquals(4, dao.count());
    }

    @Test
    @DisplayName("возвращать книгу из БД по id")
    void shouldReturnExpectedBookById() {

        when(authorDao.getAllByBookId(EXPECTED_ID)).thenReturn(EXPECTED_AUTHORS);
        when(genreDao.getAllByBookId(EXPECTED_ID)).thenReturn(EXPECTED_GENRES);

        Book expected = dao.getById(EXPECTED_ID);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
        assertThat(expected.getTitle()).isNotNull();
        assertThat(expected.getTitle()).isEqualTo(EXPECTED_TITLE);
        assertThat(expected.getAuthors()).isNotNull();
        assertThat(expected.getAuthors()).isEqualTo(EXPECTED_AUTHORS);
        assertThat(expected.getGenres()).isNotNull();
        assertThat(expected.getGenres()).isEqualTo(EXPECTED_GENRES);
    }

    @Test
    @DisplayName("сохранять книгу в БД")
    void shouldSaveBook() {
        Book expected = new Book(NEW_BOOK_ID, CLOCKWORK_ORANGE, EXPECTED_AUTHORS, EXPECTED_GENRES);

        when(authorDao.getAllByBookId(NEW_BOOK_ID)).thenReturn(EXPECTED_AUTHORS);
        when(genreDao.getAllByBookId(NEW_BOOK_ID)).thenReturn(EXPECTED_GENRES);

        long actualId = dao.save(expected);
        Book actual = dao.getById(actualId);

        assertThat(expected).isNotNull();
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    @DisplayName("обновлять информацию по книге в БД")
    void shouldUpdateBook() {
        Book old = dao.getById(EXPECTED_ID);
        assertThat(old.getTitle()).isEqualTo(EXPECTED_TITLE);

        old.setTitle(CLOCKWORK_ORANGE);
        dao.update(old);
        Book updated = dao.getById(EXPECTED_ID);
        assertThat(updated.getTitle()).isEqualTo(CLOCKWORK_ORANGE);
    }

    @Test
    @DisplayName("удалять информацию по книге из БД")
    void shouldDeleteBook() {
        assertThat(dao.count()).isEqualTo(4);
        assertThat(dao.getById(1)).isNotNull();

        dao.deleteById(1);

        assertThat(dao.count()).isEqualTo(3);
        assertThat(dao.getById(1)).isNull();
    }

    @Test
    @DisplayName("возвращать все книги из БД")
    void shouldReturnAllBooks() {
        List<Book> expected = dao.getAll();
        assertThat(expected.size()).isEqualTo(4);
    }
}