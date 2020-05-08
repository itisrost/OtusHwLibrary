package ru.otus.homework.libraryJdbc.dao;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJdbc.model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Author Jdbc Dao должен")
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {

    public static final String JEFF_NOON = "Jeff Noon";
    public static final String ANTHONY_BURGESS = "Anthony Burgess";
    public static final Author EXPECTED_AUTHOR = new Author(3, ANTHONY_BURGESS);

    @Autowired
    private AuthorDaoJdbc dao;

    @Test
    @DisplayName("верно считать количество авторов в БД")
    void shouldReturnExpectedAuthorCount() {
        assertEquals(2, dao.count());
    }

    @Test
    @DisplayName("возвращать автора по id")
    void shouldReturnExpectedAuthorById() {
        Author expected = dao.getById(2).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(2);
        assertThat(expected.getName()).isNotNull();
        assertThat(expected.getName()).isEqualTo(JEFF_NOON);
    }

    @Test
    @DisplayName("возвращать id автора по имени")
    void shouldReturnExpectedIdByAuthorName() {
        Long expected = dao.getIdByName(JEFF_NOON).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(2);
    }

    @Test
    @DisplayName("сохранять автора в БД")
    void shouldSaveAuthor() {
        dao.save(EXPECTED_AUTHOR);

        Author actual = dao.getById(EXPECTED_AUTHOR.getId()).orElse(null);
        assertThat(EXPECTED_AUTHOR).isNotNull();
        assertThat(actual).isEqualToComparingFieldByField(EXPECTED_AUTHOR);
    }

    @Test
    @DisplayName("возвращать список всех авторов из БД")
    void shouldReturnAllAuthors() {
        List<Author> expected = dao.getAll();
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("возвращать список всех авторов по id книги")
    void shouldReturnAllAuthorsByBookId() {
        List<Author> expected = dao.getAllByBookId(2);
        assertThat(expected.size()).isEqualTo(1);

        Author expectedAuthor = expected.get(0);
        assertThat(expectedAuthor.getId()).isEqualTo(2);
        assertThat(expectedAuthor.getName()).isEqualTo(JEFF_NOON);
    }

    @Test
    @DisplayName("удалять всех авторов без книг")
    void shouldDeleteAllAuthorsWithoutBooks() {
        dao.save(EXPECTED_AUTHOR);
        assertThat(dao.count()).isEqualTo(3);

        dao.deleteAuthorsWithoutBooks();
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("обновлять информацию по автору")
    void shouldUpdateAuthor() {
        Author old = dao.getById(2).orElse(null);
        assertThat(old.getName()).isEqualTo(JEFF_NOON);

        old.setName(ANTHONY_BURGESS);
        dao.update(old);
        Author updated = dao.getById(2).orElse(null);
        assertThat(updated.getName()).isEqualTo(ANTHONY_BURGESS);
    }
}