package ru.otus.homework.libraryMongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties
public class LibraryMongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryMongoApplication.class);
    }
}