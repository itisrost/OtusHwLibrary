package ru.otus.homework.libraryJpql.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpql.dao.AuthorDao;
import ru.otus.homework.libraryJpql.model.Author;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    public static final String UPDATED_SUCCESSFULLY = "Author with id = %s updated successfully!";
    public static final String AUTHOR_NOT_FOUND = "Author with id = %d not found :(";
    public static final String NO_AUTHORS_IN_LIBRARY = "There is no authors in library :(";
    public static final String ID_MUST_BE_A_NUMBER = "Author id must be a number!";

    private final AuthorDao authorDao;

    @Override
    public String updateAuthor(String id, String name) {
        long authorId = parseId(id);

        authorDao.save(new Author(authorId, name));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    public String getAuthor(String id) {
        long authorId = parseId(id);

        Optional<Author> author = authorDao.getById(authorId);
        if (author.isPresent()) {
            return author.get().toString();
        } else {
            return String.format(AUTHOR_NOT_FOUND, authorId);
        }
    }

    @Override
    public String getAllAuthors() {
        List<Author> authors = authorDao.getAll();

        if (authors.isEmpty()) {
            return NO_AUTHORS_IN_LIBRARY;
        } else {
            return authors.stream().map(Author::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    public String getAuthorsCount() {
        return authorDao.count().toString();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}