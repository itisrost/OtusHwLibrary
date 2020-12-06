package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpa.model.Book;
import ru.otus.homework.libraryJpa.model.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.libraryJpa.repository.BookRepository;
import ru.otus.homework.libraryJpa.repository.CommentRepository;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    public static final String SAVED_SUCCESSFULLY = "Comment with id = %d saved successfully!";
    public static final String UPDATED_SUCCESSFULLY = "Comment with id = %d updated successfully!";
    public static final String DELETED_SUCCESSFULLY = "Comment with id = %d deleted successfully.";
    public static final String COMMENT_NOT_FOUND = "Comment with id = %d not found :(";
    public static final String BOOK_NOT_FOUND = "Book with id = %d not found :(";
    public static final String NO_COMMENTS_IN_LIBRARY = "There is no comments in library :(";
    public static final String BOOK_HAS_NO_COMMENTS = "Book with id = %d has no comments.";
    public static final String ID_MUST_BE_A_NUMBER = "Comment id must be a number!";

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public String saveComment(String bookIdString, String text) {
        long bookId = parseId(bookIdString);

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Comment comment = commentRepository.save(new Comment(book.get(), text));

            return String.format(SAVED_SUCCESSFULLY, comment.getId());
        } else {
            return String.format(BOOK_NOT_FOUND, bookId);
        }
    }

    @Override
    @Transactional
    public String updateComment(String commentIdString, String bookIdString, String text) {
        long commentId = parseId(commentIdString);
        long bookId = parseId(bookIdString);

        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (comment.isPresent()) {
            if (book.isPresent()) {
                commentRepository.save(new Comment(commentId, book.get(), text));

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

        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            return comment.get().toString();
        } else {
            return String.format(COMMENT_NOT_FOUND, commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllComments() {
        List<Comment> comments = commentRepository.findAll();

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
        List<Comment> comments = commentRepository.findAllByBookId(bookId);

        if (comments.isEmpty()) {
            return String.format(BOOK_HAS_NO_COMMENTS, bookId);
        } else {
            return comments.stream().map(Comment::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional
    public String deleteComment(String id) {
        long commentId = parseId(id);
        if (commentRepository.existsById(commentId)){
            commentRepository.deleteById(commentId);
            return String.format(DELETED_SUCCESSFULLY, commentId);
        } else {
            return String.format(COMMENT_NOT_FOUND, commentId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getCommentsCount() {
        return commentRepository.count();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}