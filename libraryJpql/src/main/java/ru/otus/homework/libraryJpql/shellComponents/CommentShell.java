package ru.otus.homework.libraryJpql.shellComponents;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.libraryJpql.service.CommentService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class CommentShell {

    private final CommentService commentService;

    @ShellMethod(value = "Get comment", key = {"gc", "getComment"})
    public String getComment(@ShellOption(help = "Comment id") String id) {
        return commentService.getComment(id);
    }

    @ShellMethod(value = "Get all comments", key = {"gac", "getComments"})
    public String getAllComments() {
        return commentService.getAllComments();
    }

    @ShellMethod(value = "Get all comments of specified book", key = {"gabc", "getAllBookComments"})
    public String getAllBookComments(@ShellOption(help = "Book id") String bookId) {
        return commentService.getCommentsByBookId(bookId);
    }

    @ShellMethod(value = "Get comment count", key = {"gcct", "getCommentCount"})
    public String getCommentsCount() {
        return commentService.getCommentsCount();
    }

    @ShellMethod(value = "Add comment", key = {"ac", "addComment"})
    public String addComment(@ShellOption(help = "Book id") String bookId,
                              @ShellOption(help = "Comment text") String text) {
        return commentService.saveComment(bookId, text);
    }

    @ShellMethod(value = "Update comment", key = {"uc", "updateComment"})
    public String addComment(@ShellOption(help = "Comment id") String commentId,
                             @ShellOption(help = "Book id") String bookId,
                              @ShellOption(help = "Comment text") String text) {
        return commentService.updateComment(commentId, bookId, text);
    }
}