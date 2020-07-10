package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.libraryJpa.repository.GenreDao;
import ru.otus.homework.libraryJpa.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@DisplayName("Genre Service должен")
class GenreServiceImplTest {

    @Configuration
    private static class Config {

        @Bean
        public GenreService genreService(GenreDao genreDao){
            return new GenreServiceImpl(genreDao);
        }
    }

    public static final long ID_LONG = 3L;
    public static final String ID_STRING = "3";
    public static final String GENRE_NOT_FOUND = String.format("Genre with id = %d not found :(", ID_LONG);
    public static final String UPDATE_SUCCESSFUL = String.format("Genre with id = %s updated successfully!", ID_STRING);
    public static final String ID_MUST_BE_A_NUMBER = "Genre id must be a number!";
    public static final String SCIENCE_FICTION = "Science fiction";
    public static final Genre EXPECTED_GENRE = new Genre(ID_LONG, SCIENCE_FICTION);

    @MockBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("обновлять информацию по жанру")
    void shouldUpdateGenre() {

        assertThat(genreService.updateGenre(ID_STRING, SCIENCE_FICTION)).isEqualTo(UPDATE_SUCCESSFUL);
    }

    @Test
    @DisplayName("возвращать жанр по id, если он есть в БД")
    void shouldReturnExpectedGenreById() {
        when(genreDao.getById(3)).thenReturn(Optional.of(EXPECTED_GENRE));

        assertThat(genreService.getGenre(ID_STRING)).isEqualTo(EXPECTED_GENRE.toString());
    }

    @Test
    @DisplayName("возвращать ошибку, если id жанра не является числом")
    void shouldReturnExceptionIfGenreIdIsNotANumber() {
        assertThrows(InvalidParameterException.class, () -> genreService.getGenre("abc"), ID_MUST_BE_A_NUMBER);
    }

    @Test
    @DisplayName("сообщать, если жанра с введённым id нет в БД")
    void shouldNoticeIfGenreNotFound() {
        when(genreDao.getById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(genreService.getGenre(ID_STRING)).isEqualTo(GENRE_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать список всех жанров из БД")
    void shouldReturnAllGenres() {
        List<Genre> genres = List.of(new Genre(2, "Cyberpunk"), EXPECTED_GENRE);
        String expectedString = genres.stream().map(Genre::toString).collect(Collectors.joining("\n"));
        when(genreDao.getAll()).thenReturn(genres);

        assertThat(genreService.getAllGenres()).isEqualTo(expectedString);
    }

    @Test
    @DisplayName("сообщать, если в БД нет жанров")
    void shouldNoticeIfNoGenresInDB() {
        when(genreDao.getAll()).thenReturn(new ArrayList<>());

        assertThat(genreService.getAllGenres()).isEqualTo("There is no genres in library :(");
    }

    @Test
    @DisplayName("возвращать верное количество жанров из БД")
    void shouldReturnCorrectGenresCount() {
        when(genreDao.count()).thenReturn(ID_LONG);

        assertThat(genreService.getGenresCount()).isEqualTo(ID_STRING);
    }
}