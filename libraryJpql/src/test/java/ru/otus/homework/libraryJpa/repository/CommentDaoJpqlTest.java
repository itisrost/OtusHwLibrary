package ru.otus.homework.libraryJpa.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpql.model.Book;
import ru.otus.homework.libraryJpql.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.libraryJpql.repository.CommentDaoJpql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Comment Jpql Dao должен")
@DataJpaTest
@Import(CommentDaoJpql.class)
public class CommentDaoJpqlTest {

    public static final String COMMENT_TEXT = "Wooow!";
    public static final long EXPECTED_ID = 2L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentDaoJpql dao;

    @Test
    @DisplayName("верно считать количество комментариев в БД")
    void shouldReturnExpectedCommentCount() {
        assertEquals(4, dao.count());
    }

    @Test
    @DisplayName("возвращать комментарий по id")
    void shouldReturnExpectedCommentById() {
        val optionalActualComment = dao.getById(EXPECTED_ID);
        val expectedComment = em.find(Comment.class, EXPECTED_ID);
        assertThat(optionalActualComment).isPresent().get()
                .isEqualToComparingFieldByField(expectedComment);
    }

    @Test
    @DisplayName("сохранять комментарий в БД")
    void shouldSaveComment() {
        val book = em.find(Book.class, EXPECTED_ID);
        val expectedComment = new Comment(EXPECTED_ID, book, COMMENT_TEXT);

        dao.save(expectedComment);

        val actualComment = em.find(Comment.class, EXPECTED_ID);
        assertThat(actualComment).isEqualToComparingFieldByField(expectedComment);
    }

    @Test
    @DisplayName("обновлять информацию по комментарию")
    void shouldUpdateComment() {
        val oldComment = em.find(Comment.class, EXPECTED_ID);
        assertThat(oldComment.getText()).isEqualTo("Very nice.");

        oldComment.setText(COMMENT_TEXT);
        dao.save(oldComment);

        val updatedComment = em.find(Comment.class, EXPECTED_ID);
        assertThat(updatedComment.getText()).isEqualTo(COMMENT_TEXT);
    }

    @Test
    @DisplayName("возвращать список всех комментариев из БД")
    void shouldReturnAllComments() {
        val comments = dao.getAll();

        assertThat(comments).isNotNull()
                .hasSize(4)
                .allMatch(s -> !s.getText().equals(""));
    }
    @Test
    @DisplayName("возвращать список всех комментариев к заданной книге")
    void shouldReturnAllCommentsByBookId() {
        val comments = dao.getAllByBookId(EXPECTED_ID);

        assertThat(comments).isNotNull()
                .hasSize(2)
                .allMatch(s -> !s.getText().equals(""));
    }
}