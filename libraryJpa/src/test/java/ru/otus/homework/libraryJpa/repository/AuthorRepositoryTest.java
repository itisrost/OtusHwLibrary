package ru.otus.homework.libraryJpa.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Author Jpa Repository должен")
@DataJpaTest
public class AuthorRepositoryTest {

    public static final String JEFF_NOON = "Jeff Noon";
    public static final String ANTHONY_BURGESS = "Anthony Burgess";
    public static final Author EXPECTED_AUTHOR = new Author(3, ANTHONY_BURGESS);
    public static final long EXPECTED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository repository;

    @Test
    @DisplayName("возвращать автора по имени")
    void shouldReturnExpectedIdByAuthorName() {
        val expected = repository.findByName(JEFF_NOON);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
    }

    @Test
    @DisplayName("удалять всех авторов без книг")
    void shouldDeleteAllAuthorsWithoutBooks() {
        em.merge(EXPECTED_AUTHOR);
        assertThat(repository.count()).isEqualTo(3);

        repository.deleteAuthorsWithoutBooks();
        assertThat(repository.count()).isEqualTo(2);
    }
}