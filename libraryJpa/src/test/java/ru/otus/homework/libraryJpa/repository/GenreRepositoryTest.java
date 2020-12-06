package ru.otus.homework.libraryJpa.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Genre Jpa Repository должен")
@DataJpaTest
class GenreRepositoryTest {

    public static final String CYBERPUNK = "Cyberpunk";
    public static final String SCIENCE_FICTION = "Science fiction";
    public static final Genre EXPECTED_GENRE = new Genre(3, SCIENCE_FICTION);
    public static final long EXPECTED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository repository;


    @Test
    @DisplayName("возвращать жанр по имени")
    void shouldReturnExpectedIdByGenreName() {
        val expected = repository.findByName(CYBERPUNK);

        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
    }

    @Test
    @DisplayName("удалять все жанры без книг")
    void shouldDeleteAllGenresWithoutBooks() {
        em.merge(EXPECTED_GENRE);
        assertThat(repository.count()).isEqualTo(3);

        repository.deleteGenresWithoutBooks();
        assertThat(repository.count()).isEqualTo(2);
    }
}