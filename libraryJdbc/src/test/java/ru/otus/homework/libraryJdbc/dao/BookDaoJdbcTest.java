package ru.otus.homework.libraryJdbc.dao;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJdbc.model.Book;
import ru.otus.homework.libraryJdbc.model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book Jdbc Dao должен")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    public static final String CLOCKWORK_ORANGE = "Clockwork orange";
    public static final String VURT = "Vurt";

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
        Book expected = dao.getById(2).orElse(null);
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(2);
        assertThat(expected.getTitle()).isNotNull();
        assertThat(expected.getTitle()).isEqualTo(VURT);
    }

    @Test
    @DisplayName("сохранять автора в БД")
    void shouldSaveBook() {
        Book expected = new Book(5, CLOCKWORK_ORANGE);

        long actualId = dao.save(expected);
        Book actual = dao.getById(actualId).orElse(null);

        assertThat(expected).isNotNull();
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    @DisplayName("обновлять информацию по книге в БД")
    void shouldUpdateBook() {
        Book old = dao.getById(2).orElse(null);
        assertThat(old.getTitle()).isEqualTo(VURT);

        old.setTitle(CLOCKWORK_ORANGE);
        dao.update(old);
        Book updated = dao.getById(2).orElse(null);
        assertThat(updated.getTitle()).isEqualTo(CLOCKWORK_ORANGE);
    }

    @Test
    @DisplayName("удалять информацию по книге из БД")
    void shouldDelete1Book() {
        assertThat(dao.count()).isEqualTo(4);
        assertThat(dao.getById(1)).isPresent();

        dao.deleteById(1);

        assertThat(dao.count()).isEqualTo(3);
        assertThat(dao.getById(1)).isNotPresent();
    }

    @Test
    @DisplayName("возвращать все книги из БД")
    void shouldReturnAllBooks() {
        List<Book> expected = dao.getAll();
        assertThat(expected.size()).isEqualTo(4);
    }
}