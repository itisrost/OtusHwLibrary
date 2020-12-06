package ru.otus.homework.libraryMongo.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.homework.libraryMongo.config.AppTestConfig;
import ru.otus.homework.libraryMongo.config.MongoTestConfig;
import ru.otus.homework.libraryMongo.model.Book;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Comment Mongo Repository должен")
@DataMongoTest
@Import({AppTestConfig.class, MongoTestConfig.class})
public class CommentRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository repository;

    @Test
    @DisplayName("возвращать список всех комментариев к заданной книге")
    void shouldReturnAllCommentsByBookId() {
        Book book = mongoTemplate.findAll(Book.class).get(0);
        val comments = repository.findAllByBookId(book.getId());

        assertThat(comments).isNotNull()
                .hasSize(1)
                .allMatch(s -> !s.getText().equals(""));
    }
}