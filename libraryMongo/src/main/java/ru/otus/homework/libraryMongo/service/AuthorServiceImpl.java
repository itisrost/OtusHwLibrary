package ru.otus.homework.libraryMongo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryMongo.model.Author;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.libraryMongo.repository.AuthorRepository;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    public static final String UPDATED_SUCCESSFULLY = "Author with id = %s updated successfully!";
    public static final String AUTHOR_NOT_FOUND = "Author with id = %s not found :(";
    public static final String NO_AUTHORS_IN_LIBRARY = "There is no authors in library :(";

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public String updateAuthor(String id, String name) {

        authorRepository.save(new Author(id, name));

        return String.format(UPDATED_SUCCESSFULLY, id);
    }

    @Override
    @Transactional(readOnly = true)
    public String getAuthor(String id) {

        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return author.get().toString();
        } else {
            return String.format(AUTHOR_NOT_FOUND, id);
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
}