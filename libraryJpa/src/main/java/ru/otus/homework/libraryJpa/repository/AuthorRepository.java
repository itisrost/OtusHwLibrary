package ru.otus.homework.libraryJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.libraryJpa.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);

    @Query(value = "delete from authors where id not in (select distinct author_id from books_authors)", nativeQuery = true)
    @Modifying
    void deleteAuthorsWithoutBooks();
}