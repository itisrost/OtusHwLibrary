package ru.otus.homework.libraryJpql.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpql.repository.GenreDao;
import ru.otus.homework.libraryJpql.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    public static final String UPDATED_SUCCESSFULLY = "Genre with id = %s updated successfully!";
    public static final String GENRE_NOT_FOUND = "Genre with id = %d not found :(";
    public static final String NO_GENRES_IN_LIBRARY = "There is no genres in library :(";
    public static final String ID_MUST_BE_A_NUMBER = "Genre id must be a number!";

    private final GenreDao genreDao;

    @Override
    @Transactional
    public String updateGenre(String id, String name) {
        long genreId = parseId(id);

        genreDao.save(new Genre(genreId, name));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getGenre(String id) {
        long genreId = parseId(id);

        Optional<Genre> genre = genreDao.getById(genreId);
        if (genre.isPresent()) {
            return genre.get().toString();
        } else {
            return String.format(GENRE_NOT_FOUND, genreId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllGenres() {
        List<Genre> genres = genreDao.getAll();

        if (genres.isEmpty()) {
            return NO_GENRES_IN_LIBRARY;
        } else {
            return genres.stream().map(Genre::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getGenresCount() {
        return genreDao.count().toString();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}