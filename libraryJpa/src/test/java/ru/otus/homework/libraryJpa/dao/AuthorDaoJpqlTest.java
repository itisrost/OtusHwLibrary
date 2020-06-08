package ru.otus.homework.libraryJpa.dao;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Author Jpql Dao должен")
@DataJpaTest
@Import(AuthorDaoJpql.class)
public class AuthorDaoJpqlTest {

    public static final String JEFF_NOON = "Jeff Noon";
    public static final String ANTHONY_BURGESS = "Anthony Burgess";
    public static final Author EXPECTED_AUTHOR = new Author(3, ANTHONY_BURGESS);
    public static final long EXPECTED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorDaoJpql dao;

    @Test
    @DisplayName("верно считать количество авторов в БД")
    void shouldReturnExpectedAuthorCount() {
        assertEquals(2, dao.count());
    }

    @Test
    @DisplayName("возвращать автора по id")
    void shouldReturnExpectedAuthorById() {
        val optionalActualAuthor = dao.getById(EXPECTED_ID);
        val expectedAuthor = em.find(Author.class, EXPECTED_ID);
        assertThat(optionalActualAuthor).isPresent().get()
                .isEqualToComparingFieldByField(expectedAuthor);
    }

    @Test
    @DisplayName("возвращать id автора по имени")
    void shouldReturnExpectedIdByAuthorName() {
        val expected = dao.getByName(JEFF_NOON).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(EXPECTED_ID);
    }

    @Test
    @DisplayName("сохранять автора в БД")
    void shouldSaveAuthor() {
        dao.save(EXPECTED_AUTHOR);

        val expectedAuthor = em.find(Author.class, EXPECTED_AUTHOR.getId());
        assertThat(expectedAuthor).isEqualToComparingFieldByField(EXPECTED_AUTHOR);
    }

    @Test
    @DisplayName("возвращать список всех авторов из БД")
    void shouldReturnAllAuthors() {
        val authors = dao.getAll();

        assertThat(authors).isNotNull()
                .hasSize(2)
                .allMatch(s -> !s.getName().equals(""));
    }

    @Test
    @DisplayName("удалять всех авторов без книг")
    void shouldDeleteAllAuthorsWithoutBooks() {
        em.merge(EXPECTED_AUTHOR);
        assertThat(dao.count()).isEqualTo(3);

        dao.deleteAuthorsWithoutBooks();
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("обновлять информацию по автору")
    void shouldUpdateAuthor() {
        val oldAuthor = em.find(Author.class, EXPECTED_ID);
        assertThat(oldAuthor.getName()).isEqualTo(JEFF_NOON);

        oldAuthor.setName(ANTHONY_BURGESS);
        dao.save(oldAuthor);

        val updatedAuthor = em.find(Author.class, EXPECTED_ID);
        assertThat(updatedAuthor.getName()).isEqualTo(ANTHONY_BURGESS);
    }
}