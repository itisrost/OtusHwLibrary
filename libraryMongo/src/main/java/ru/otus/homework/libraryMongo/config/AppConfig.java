package ru.otus.homework.libraryMongo.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final String CHANGELOG_PACKAGE = "ru.otus.homework.libraryMongo.mongock.changelog";

    @Bean
    public Mongock mongock(MongoConfig mongoConfig, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoConfig.getDatabase(), CHANGELOG_PACKAGE)
                .build();
    }
}