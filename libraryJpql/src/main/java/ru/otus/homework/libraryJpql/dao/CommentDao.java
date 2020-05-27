package ru.otus.homework.libraryJpql.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.homework.libraryJpql.model.Comment;

public interface CommentDao {

    Long count();

    Optional<Comment> getById(long id);

    long save(Comment Comment);

    List<Comment> getAll();
}