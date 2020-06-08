package ru.otus.homework.libraryJpa.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpa.model.Comment;

public interface CommentDao {

    Long count();

    Optional<Comment> getById(long id);

    long save(Comment Comment);

    List<Comment> getAll();

    List<Comment> getAllByBookId(long bookId);
}