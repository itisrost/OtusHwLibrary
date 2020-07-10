package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.model.Book;
import ru.otus.homework.libraryJpa.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.libraryJpa.repository.BookRepository;
import ru.otus.homework.libraryJpa.repository.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@DisplayName("Comment Service должен")
class CommentServiceImplTest {

    @Configuration
    private static class Config {

        @Bean
        public CommentService commentService(BookRepository bookRepository, CommentRepository commentRepository) {
            return new CommentServiceImpl(bookRepository, commentRepository);
        }
    }

    public static final long ID_LONG = 3L;
    public static final String ID_STRING = "3";
    public static final String COMMENT_NOT_FOUND = String.format("Comment with id = %d not found :(", ID_LONG);
    public static final String BOOK_NOT_FOUND = String.format("Book with id = %d not found :(", ID_LONG);
    public static final String SAVE_SUCCESSFUL = String.format("Comment with id = %s saved successfully!", ID_STRING);
    public static final String UPDATE_SUCCESSFUL = String.format("Comment with id = %s updated successfully!", ID_STRING);
    public static final String DELETE_SUCCESSFUL = String.format("Comment with id = %s deleted successfully.", ID_STRING);
    public static final String ID_MUST_BE_A_NUMBER = "Comment id must be a number!";
    public static final String WOW = "Wow!";
    public static final Book EXPECTED_BOOK = new Book(ID_LONG, "Book", null, null);
    public static final Comment EXPECTED_COMMENT = new Comment(ID_LONG, EXPECTED_BOOK, WOW);

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("сохранять комментарий")
    void shouldSaveComment() {
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_BOOK));
        when(commentRepository.save(any())).thenReturn(EXPECTED_COMMENT);

        assertThat(commentService.saveComment(ID_STRING, WOW)).isEqualTo(SAVE_SUCCESSFUL);
    }

    @Test
    @DisplayName("не должен сохранять комментарий если книга не найдена")
    void shouldNotSaveCommentIfBookNotFound() {
        assertThat(commentService.saveComment(ID_STRING, WOW)).isEqualTo(BOOK_NOT_FOUND);
    }

    @Test
    @DisplayName("обновлять информацию по комментарию")
    void shouldUpdateComment() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_COMMENT));
        when(bookRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_BOOK));
        assertThat(commentService.updateComment(ID_STRING, ID_STRING, WOW)).isEqualTo(UPDATE_SUCCESSFUL);
    }

    @Test
    @DisplayName("не должен обновлять информацию по комментарию если комментарий не найден")
    void shouldNotUpdateCommentIfCommentNotFound() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.empty());
        assertThat(commentService.updateComment(ID_STRING, ID_STRING, WOW)).isEqualTo(COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("не должен обновлять информацию по комментарию если книга не найдена")
    void shouldNotUpdateCommentIfBookNotFound() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_COMMENT));

        assertThat(commentService.updateComment(ID_STRING, ID_STRING, WOW)).isEqualTo(BOOK_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать комментарий по id, если он есть в БД")
    void shouldReturnExpectedCommentById() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(EXPECTED_COMMENT));

        assertThat(commentService.getComment(ID_STRING)).isEqualTo(EXPECTED_COMMENT.toString());
    }

    @Test
    @DisplayName("возвращать ошибку, если id комментария не является числом")
    void shouldReturnExceptionIfCommentIdIsNotANumber() {
        assertThrows(InvalidParameterException.class, () -> commentService.getComment("abc"), ID_MUST_BE_A_NUMBER);
    }

    @Test
    @DisplayName("сообщать, если комментария с введённым id нет в БД")
    void shouldNoticeIfCommentNotFound() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(commentService.getComment(ID_STRING)).isEqualTo(COMMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать список всех комментариев из БД")
    void shouldReturnAllComments() {
        List<Comment> comments = List.of(new Comment(2, EXPECTED_BOOK, "Jeff Noon"), EXPECTED_COMMENT);
        String expectedString = comments.stream().map(Comment::toString).collect(Collectors.joining("\n"));
        when(commentRepository.findAll()).thenReturn(comments);

        assertThat(commentService.getAllComments()).isEqualTo(expectedString);
    }

    @Test
    @DisplayName("сообщать, если в БД нет комментариев")
    void shouldNoticeIfNoCommentsInDB() {
        when(commentRepository.findAll()).thenReturn(new ArrayList<>());

        assertThat(commentService.getAllComments()).isEqualTo("There is no comments in library :(");
    }

    @Test
    @DisplayName("возвращать верное количество комментариев из БД")
    void shouldReturnCorrectCommentsCount() {
        when(commentRepository.count()).thenReturn(ID_LONG);

        assertThat(commentService.getCommentsCount()).isEqualTo(ID_LONG);
    }

    @Test
    @DisplayName("удалять комментарий, если он есть в БД")
    void shouldDeleteCommentIfExists() {
        when(commentRepository.existsById(ID_LONG)).thenReturn(true);
        assertThat(commentService.deleteComment(ID_STRING)).isEqualTo(DELETE_SUCCESSFUL);
    }

    @Test
    @DisplayName("сообщать, если удаляемого комментария с введённым id нет в БД")
    void shouldNoticeIfCommentToDeleteIsNotFound() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(commentService.deleteComment(ID_STRING)).isEqualTo(COMMENT_NOT_FOUND);
    }
}