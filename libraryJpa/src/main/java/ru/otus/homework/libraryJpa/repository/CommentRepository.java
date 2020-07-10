package ru.otus.homework.libraryJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.libraryJpa.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBookId(long bookId);

}
