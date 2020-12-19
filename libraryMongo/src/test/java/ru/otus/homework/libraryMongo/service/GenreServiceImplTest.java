package ru.otus.homework.libraryMongo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.libraryMongo.model.Genre;
import ru.otus.homework.libraryMongo.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest()
@DisplayName("Genre Service должен")
class GenreServiceImplTest {

    @Configuration
    static class Config {

        @Bean
        public GenreService genreService(GenreRepository genreRepository){
            return new GenreServiceImpl(genreRepository);
        }
    }

    public static final long ID_LONG = 3L;
    public static final String ID_STRING = "3";
    public static final String GENRE_NOT_FOUND = String.format("Genre with id = %s not found :(", ID_STRING);
    public static final String UPDATE_SUCCESSFUL = String.format("Genre with id = %s updated successfully!", ID_STRING);
    public static final String SCIENCE_FICTION = "Science fiction";
    public static final Genre EXPECTED_GENRE = new Genre(ID_STRING, SCIENCE_FICTION);

    @MockBean
    private GenreRepository genreRepository;

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
        when(genreRepository.findById(ID_STRING)).thenReturn(Optional.of(EXPECTED_GENRE));

        assertThat(genreService.getGenre(ID_STRING)).isEqualTo(EXPECTED_GENRE.toString());
    }

    @Test
    @DisplayName("сообщать, если жанра с введённым id нет в БД")
    void shouldNoticeIfGenreNotFound() {
        when(genreRepository.findById(ID_STRING)).thenReturn(Optional.empty());

        assertThat(genreService.getGenre(ID_STRING)).isEqualTo(GENRE_NOT_FOUND);
    }

    @Test
    @DisplayName("возвращать список всех жанров из БД")
    void shouldReturnAllGenres() {
        List<Genre> genres = List.of(new Genre("2", "Cyberpunk"), EXPECTED_GENRE);
        String expectedString = genres.stream().map(Genre::toString).collect(Collectors.joining("\n"));
        when(genreRepository.findAll()).thenReturn(genres);

        assertThat(genreService.getAllGenres()).isEqualTo(expectedString);
    }

    @Test
    @DisplayName("сообщать, если в БД нет жанров")
    void shouldNoticeIfNoGenresInDB() {
        when(genreRepository.findAll()).thenReturn(new ArrayList<>());

        assertThat(genreService.getAllGenres()).isEqualTo("There is no genres in library :(");
    }

    @Test
    @DisplayName("возвращать верное количество жанров из БД")
    void shouldReturnCorrectGenresCount() {
        when(genreRepository.count()).thenReturn(ID_LONG);

        assertThat(genreService.getGenresCount()).isEqualTo(ID_LONG);
    }
}