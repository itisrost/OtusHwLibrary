package ru.otus.homework.libraryMongo.shellComponents;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.libraryMongo.service.GenreService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class GenreShell {

    private final GenreService genreService;

    @ShellMethod(value = "Get genre", key = {"gg", "getGenre"})
    public String getGenre(@ShellOption(help = "Genre id") String id) {
        return genreService.getGenre(id);
    }

    @ShellMethod(value = "Get all genres", key = {"gag", "getGenres"})
    public String getAllGenres() {
        return genreService.getAllGenres();
    }

    @ShellMethod(value = "Get genre count", key = {"ggct", "getGenreCount"})
    public long getGenresCount() {
        return genreService.getGenresCount();
    }

    @ShellMethod(value = "Update genre", key = {"ug", "updateGenre"})
    public String updateGenre(@ShellOption(help = "Genre id") String id,
                              @ShellOption(help = "Genre name") String name) {
        return genreService.updateGenre(id, name);
    }
}