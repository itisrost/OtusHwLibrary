package ru.otus.homework.libraryJdbc.dao;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJdbc.model.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Genre Jdbc Dao должен")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    public static final String CYBERPUNK = "Cyberpunk";
    public static final String SCIENCE_FICTION = "Science fiction";
    public static final Genre EXPECTED_GENRE = new Genre(3, SCIENCE_FICTION);

    @Autowired
    private GenreDaoJdbc dao;

    @Test
    @DisplayName("верно считать количество жанров в БД")
    void shouldReturnExpectedGenreCount() {
        assertEquals(2, dao.count());
    }

    @Test
    @DisplayName("возвращать жанр по id")
    void shouldReturnExpectedGenreById() {
        Genre expected = dao.getById(2).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(2);
        assertThat(expected.getName()).isNotNull();
        assertThat(expected.getName()).isEqualTo(CYBERPUNK);
    }

    @Test
    @DisplayName("возвращать id жанра по имени")
    void shouldReturnExpectedIdByGenreName() {
        Long expected = dao.getIdByName(CYBERPUNK).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(2);
    }

    @Test
    @DisplayName("сохранять жанр в БД")
    void shouldSaveGenre() {
        dao.save(EXPECTED_GENRE);

        Genre actual = dao.getById(EXPECTED_GENRE.getId()).orElse(null);
        assertThat(EXPECTED_GENRE).isNotNull();
        assertThat(actual).isEqualToComparingFieldByField(EXPECTED_GENRE);
    }

    @Test
    @DisplayName("возвращать список всех жанров из БД")
    void shouldReturnAllGenres() {
        List<Genre> expected = dao.getAll();
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("возвращать список всех жанров по id книги")
    void shouldReturnAllGenresByBookId() {
        List<Genre> expected = dao.getAllByBookId(4);
        assertThat(expected.size()).isEqualTo(1);

        Genre expectedGenre = expected.get(0);
        assertThat(expectedGenre.getId()).isEqualTo(2);
        assertThat(expectedGenre.getName()).isEqualTo(CYBERPUNK);
    }

    @Test
    @DisplayName("удалять все жанры без книг")
    void shouldDeleteAllGenresWithoutBooks() {
        Genre authorWithoutBook = new Genre(3, SCIENCE_FICTION);

        dao.save(authorWithoutBook);
        assertThat(dao.count()).isEqualTo(3);

        dao.deleteGenresWithoutBooks();
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("обновлять информацию по жанру")
    void shouldUpdateGenre() {
        Genre old = dao.getById(2).orElse(null);
        assertThat(old.getName()).isEqualTo(CYBERPUNK);

        old.setName(SCIENCE_FICTION);
        dao.update(old);
        Genre updated = dao.getById(2).orElse(null);
        assertThat(updated.getName()).isEqualTo(SCIENCE_FICTION);
    }
}