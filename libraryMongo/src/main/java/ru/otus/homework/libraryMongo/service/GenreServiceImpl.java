package ru.otus.homework.libraryMongo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryMongo.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.libraryMongo.repository.GenreRepository;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    public static final String UPDATED_SUCCESSFULLY = "Genre with id = %s updated successfully!";
    public static final String GENRE_NOT_FOUND = "Genre with id = %s not found :(";
    public static final String NO_GENRES_IN_LIBRARY = "There is no genres in library :(";

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public String updateGenre(String id, String name) {

        genreRepository.save(new Genre(id, name));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getGenre(String id) {

        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return genre.get().toString();
        } else {
            return String.format(GENRE_NOT_FOUND, id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllGenres() {
        List<Genre> genres = genreRepository.findAll();

        if (genres.isEmpty()) {
            return NO_GENRES_IN_LIBRARY;
        } else {
            return genres.stream().map(Genre::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getGenresCount() {
        return genreRepository.count();
    }
}