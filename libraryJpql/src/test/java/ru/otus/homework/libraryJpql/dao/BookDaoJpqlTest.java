package ru.otus.homework.libraryJpql.dao;

import java.util.Collections;
import java.util.List;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpql.model.Author;
import ru.otus.homework.libraryJpql.model.Book;
import ru.otus.homework.libraryJpql.model.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book Jpql Dao должен")
@DataJpaTest
@Import(BookDaoJpql.class)
class BookDaoJpqlTest {

    public static final long NEW_BOOK_ID = 5L;
    public static final long EXPECTED_ID = 2L;
    public static final List<Author> EXPECTED_AUTHORS = Collections.singletonList(new Author(EXPECTED_ID, "Jeff Noon"));
    public static final List<Genre> EXPECTED_GENRES = Collections.singletonList(new Genre(EXPECTED_ID, "Cyberpunk"));
    public static final String CLOCKWORK_ORANGE = "Clockwork orange";
    public static final String EXPECTED_TITLE = "Vurt";

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookDaoJpql dao;

    @Test
    @DisplayName("верно считать количество книг в БД")
    void shouldReturnExpectedBookCount() {
        assertEquals(4, dao.count());
    }

    @Test
    @DisplayName("возвращать книгу из БД по id")
    void shouldReturnExpectedBookById() {
        val expected = dao.getById(EXPECTED_ID);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
        assertThat(expected.getTitle()).isNotNull().isEqualTo(EXPECTED_TITLE);
        assertThat(expected.getAuthors()).isNotNull().containsAll(EXPECTED_AUTHORS);
        assertThat(expected.getGenres()).isNotNull().containsAll(EXPECTED_GENRES);
    }

   /* @Test
    @DisplayName("сохранять книгу в БД")
    void shouldSaveBook() {
        val expected = new Book(NEW_BOOK_ID, CLOCKWORK_ORANGE, EXPECTED_AUTHORS, EXPECTED_GENRES);

        val actualId = dao.save(expected);
        assertThat(expected).isNotNull();

        val actual = em.find(Book.class, actualId);
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }*/

    @Test
    @DisplayName("обновлять информацию по книге в БД")
    void shouldUpdateBook() {
        val old = em.find(Book.class, EXPECTED_ID);
        assertThat(old.getTitle()).isEqualTo(EXPECTED_TITLE);

        old.setTitle(CLOCKWORK_ORANGE);
        dao.save(old);

        val updated = em.find(Book.class, EXPECTED_ID);
        assertThat(updated.getTitle()).isEqualTo(CLOCKWORK_ORANGE);
    }

    @Test
    @DisplayName("удалять информацию по книге из БД")
    void shouldDeleteBook() {
        val bookToDelete = em.find(Book.class, EXPECTED_ID);
        assertThat(dao.count()).isEqualTo(4);
        assertThat(bookToDelete).isNotNull();

        dao.delete(bookToDelete);

        assertThat(dao.count()).isEqualTo(3);
        assertThat(em.find(Book.class, EXPECTED_ID)).isNull();
    }

    @Test
    @DisplayName("возвращать все книги из БД")
    void shouldReturnAllBooks() {
        val expected = dao.getAll();
        assertThat(expected).isNotNull()
                .hasSize(4)
                .allMatch(s -> !s.getTitle().equals(""));
    }
}