package ru.otus.homework.libraryJpql.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpql.dao.BookDao;
import ru.otus.homework.libraryJpql.dao.CommentDao;
import ru.otus.homework.libraryJpql.model.Book;
import ru.otus.homework.libraryJpql.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    public static final String SAVED_SUCCESSFULLY = "Comment with id = %d saved successfully!";
    public static final String UPDATED_SUCCESSFULLY = "Comment with id = %d updated successfully!";
    public static final String COMMENT_NOT_FOUND = "Comment with id = %d not found :(";
    public static final String BOOK_NOT_FOUND = "Book with id = %d not found :(";
    public static final String NO_COMMENTS_IN_LIBRARY = "There is no comments in library :(";
    public static final String BOOK_HAS_NO_COMMENTS = "Book with id = %d has no comments.";
    public static final String ID_MUST_BE_A_NUMBER = "Comment id must be a number!";

    private final BookDao bookDao;
    private final CommentDao commentDao;

    @Override
    @Transactional
    public String saveComment(String bookIdString, String text) {
        long bookId = parseId(bookIdString);

        Book book = bookDao.getById(bookId);
        if (book != null) {
            long commentId = commentDao.save(new Comment(book, text));

            return String.format(SAVED_SUCCESSFULLY, commentId);
        } else {
            return String.format(BOOK_NOT_FOUND, bookId);
        }
    }

    @Override
    @Transactional
    public String updateComment(String commentIdString, String bookIdString, String text) {
        long commentId = parseId(commentIdString);
        long bookId = parseId(bookIdString);

        Optional<Comment> comment = commentDao.getById(commentId);
        Book book = bookDao.getById(bookId);
        if (comment.isPresent()) {
            if (book != null) {
                commentDao.save(new Comment(commentId, book, text));

                return String.format(UPDATED_SUCCESSFULLY, commentId);
            } else {
                return String.format(BOOK_NOT_FOUND, bookId);
            }
        } else {
            return String.format(COMMENT_NOT_FOUND, commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getComment(String id) {
        long commentId = parseId(id);

        Optional<Comment> comment = commentDao.getById(commentId);
        if (comment.isPresent()) {
            return comment.get().toString();
        } else {
            return String.format(COMMENT_NOT_FOUND, commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllComments() {
        List<Comment> comments = commentDao.getAll();

        if (comments.isEmpty()) {
            return NO_COMMENTS_IN_LIBRARY;
        } else {
            return comments.stream().map(Comment::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getCommentsByBookId(String id) {
        long bookId = parseId(id);
        List<Comment> comments = commentDao.getAllByBookId(bookId);

        if (comments.isEmpty()) {
            return String.format(BOOK_HAS_NO_COMMENTS, bookId);
        } else {
            return comments.stream().map(Comment::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getCommentsCount() {
        return commentDao.count().toString();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}