package ru.otus.homework.libraryJpa.shellComponents;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.libraryJpa.service.AuthorService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShell {

    private final AuthorService authorService;

    @ShellMethod(value = "Get author", key = {"ga", "getAuthor"})
    public String getAuthor(@ShellOption(help = "Author id") String id) {
        return authorService.getAuthor(id);
    }

    @ShellMethod(value = "Get all authors", key = {"gaa", "getAuthors"})
    public String getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @ShellMethod(value = "Get author count", key = {"gact", "getAuthorCount"})
    public long getAuthorsCount() {
        return authorService.getAuthorsCount();
    }

    @ShellMethod(value = "Update author", key = {"ua", "updateAuthor"})
    public String updateAuthor(@ShellOption(help = "Author id") String id,
                               @ShellOption(help = "Author name") String name) {
        return authorService.updateAuthor(id, name);
    }
}