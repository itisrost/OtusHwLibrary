package ru.otus.homework.libraryMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.libraryMongo.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {

}