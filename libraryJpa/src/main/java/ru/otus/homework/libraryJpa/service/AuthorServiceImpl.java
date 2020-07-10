package ru.otus.homework.libraryJpa.service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpa.model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.libraryJpa.repository.AuthorRepository;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    public static final String UPDATED_SUCCESSFULLY = "Author with id = %s updated successfully!";
    public static final String AUTHOR_NOT_FOUND = "Author with id = %d not found :(";
    public static final String NO_AUTHORS_IN_LIBRARY = "There is no authors in library :(";
    public static final String ID_MUST_BE_A_NUMBER = "Author id must be a number!";

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public String updateAuthor(String id, String name) {
        long authorId = parseId(id);

        authorRepository.save(new Author(authorId, name));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getAuthor(String id) {
        long authorId = parseId(id);

        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            return author.get().toString();
        } else {
            return String.format(AUTHOR_NOT_FOUND, authorId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            return NO_AUTHORS_IN_LIBRARY;
        } else {
            return authors.stream().map(Author::toString).collect(Collectors.joining("\n"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getAuthorsCount() {
        return authorRepository.count();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(ID_MUST_BE_A_NUMBER);
        }
    }
}