package ru.otus.homework.libraryMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.libraryMongo.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

    Author findByName(String name);
}