package ru.otus.homework.libraryMongo.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.homework.libraryMongo.config.AppTestConfig;
import ru.otus.homework.libraryMongo.config.MongoTestConfig;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Author Mongo Repository должен")
@DataMongoTest
@Import({AppTestConfig.class, MongoTestConfig.class})
public class AuthorRepositoryTest {

    public static final String AUTHOR_NAME = "Автор #1";

    @Autowired
    private AuthorRepository repository;

    @Test
    @DisplayName("возвращать автора по имени")
    void shouldReturnExpectedIdByAuthorName() {
        val expected = repository.findByName(AUTHOR_NAME);
        assertThat(expected).isNotNull();
    }
}