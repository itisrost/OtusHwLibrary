package ru.otus.homework.libraryJpa.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Genre Jpql Dao должен")
@DataJpaTest
@Import(GenreDaoJpql.class)
class GenreDaoJpqlTest {

    public static final String CYBERPUNK = "Cyberpunk";
    public static final String SCIENCE_FICTION = "Science fiction";
    public static final Genre EXPECTED_GENRE = new Genre(3, SCIENCE_FICTION);
    public static final long EXPECTED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreDaoJpql dao;

    @Test
    @DisplayName("верно считать количество жанров в БД")
    void shouldReturnExpectedGenreCount() {
        assertEquals(2, dao.count());
    }

    @Test
    @DisplayName("возвращать жанр по id")
    void shouldReturnExpectedGenreById() {
        val optionalActualGenre = dao.getById(EXPECTED_ID);
        val expectedGenre = em.find(Genre.class, EXPECTED_ID);

        assertThat(optionalActualGenre).isPresent().get()
                .isEqualToComparingFieldByField(expectedGenre);
    }

    @Test
    @DisplayName("возвращать id жанра по имени")
    void shouldReturnExpectedIdByGenreName() {
        val expected = dao.getByName(CYBERPUNK).orElse(null);

        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
    }

    @Test
    @DisplayName("сохранять жанр в БД")
    void shouldSaveGenre() {
        dao.save(EXPECTED_GENRE);

        val expectedGenre = em.find(Genre.class, EXPECTED_GENRE.getId());
        assertThat(expectedGenre).isEqualToComparingFieldByField(EXPECTED_GENRE);
    }

    @Test
    @DisplayName("возвращать список всех жанров из БД")
    void shouldReturnAllGenres() {
        val genres = dao.getAll();

        assertThat(genres).isNotNull()
                .hasSize(2)
                .allMatch(s -> !s.getName().equals(""));
    }

    @Test
    @DisplayName("удалять все жанры без книг")
    void shouldDeleteAllGenresWithoutBooks() {
        em.merge(EXPECTED_GENRE);
        assertThat(dao.count()).isEqualTo(3);

        dao.deleteGenresWithoutBooks();
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("обновлять информацию по жанру")
    void shouldUpdateGenre() {
        val oldGenre = em.find(Genre.class, EXPECTED_ID);
        assertThat(oldGenre.getName()).isEqualTo(CYBERPUNK);

        oldGenre.setName(SCIENCE_FICTION);
        dao.save(oldGenre);
        
        val updatedGenre = em.find(Genre.class, EXPECTED_ID);
        assertThat(updatedGenre.getName()).isEqualTo(SCIENCE_FICTION);
    }
}