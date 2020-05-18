package ru.otus.homework.libraryJpql.shellComponents;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.libraryJpql.service.BookService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class BookShell {

    private final BookService bookService;

    @ShellMethod(value = "Get book", key = {"gb", "getBook"})
    public String getBook(@ShellOption(help = "Book id") String id) {
        return bookService.getBook(id);
    }

    @ShellMethod(value = "Get all books", key = {"gab", "getBooks"})
    public String getAllBooks() {
        return bookService.getAllBooks();
    }

    @ShellMethod(value = "Get book count", key = {"gbc", "getBookCount"})
    public String getBooksCount() {
        return bookService.getBooksCount();
    }

    @ShellMethod(value = "Create new book", key = {"cb", "createBook"})
    public String createBook(@ShellOption(help = "Book title") String title,
                             @ShellOption(help = "Authors divided by \",\" and take all authors in quotes, if some of names are containing spaces.") String authors,
                             @ShellOption(help = "Genres divided by \",\" and take all genres in quotes, if some of them are containing spaces.") String genres) {
        return bookService.saveBook(title, authors, genres);
    }

    @ShellMethod(value = "Update book", key = {"ub", "updateBook"})
    public String createBook(@ShellOption(help = "Book id") String id,
                             @ShellOption(help = "Book title") String title,
                             @ShellOption(help = "Authors divided by \",\" and take all authors in quotes, if some of names are containing spaces.") String authors,
                             @ShellOption(help = "Genres divided by \",\" and take all genres in quotes, if some of them are containing spaces.") String genres) {
        return bookService.updateBook(id, title, authors, genres);
    }

    @ShellMethod(value = "Delete book", key = {"db", "deleteBook"})
    public String deleteBook(@ShellOption(help = "Book id") String id) {
        return bookService.deleteBook(id);
    }
}