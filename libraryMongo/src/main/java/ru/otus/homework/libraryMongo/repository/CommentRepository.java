package ru.otus.homework.libraryMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.libraryMongo.model.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);

}