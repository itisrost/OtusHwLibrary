package ru.otus.homework.libraryJpa.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comment Jpa Repository должен")
@DataJpaTest
public class CommentRepositoryTest {

    public static final long EXPECTED_ID = 2L;

    @Autowired
    private CommentRepository repository;

    @Test
    @DisplayName("возвращать список всех комментариев к заданной книге")
    void shouldReturnAllCommentsByBookId() {
        val comments = repository.findAllByBookId(EXPECTED_ID);

        assertThat(comments).isNotNull()
                .hasSize(2)
                .allMatch(s -> !s.getText().equals(""));
    }
}