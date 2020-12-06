package ru.otus.homework.libraryMongo.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppTestConfig {
    private static final String TEST_CHANGELOG_PACKAGE = "ru.otus.homework.libraryMongo.mongock.changelog.test";

    @Bean
    public Mongock mongock(MongoTestConfig mongoTestConfig, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoTestConfig.getDatabase(), TEST_CHANGELOG_PACKAGE)
                .build();
    }
}