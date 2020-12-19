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

@DisplayName("Genre Mongo Repository должен")
@DataMongoTest
@Import({AppTestConfig.class, MongoTestConfig.class})
class GenreRepositoryTest {

    public static final String GENRE_NAME = "Жанр #1";

    @Autowired
    private GenreRepository repository;

    @Test
    @DisplayName("возвращать жанр по имени")
    void shouldReturnExpectedIdByGenreName() {
        val expected = repository.findByName(GENRE_NAME);

        assertThat(expected).isNotNull();
    }
}