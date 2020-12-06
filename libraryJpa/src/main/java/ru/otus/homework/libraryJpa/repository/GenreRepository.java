package ru.otus.homework.libraryJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.homework.libraryJpa.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);

    @Query(value = "delete from genres where id not in (select distinct genre_id from books_genres)", nativeQuery = true)
    @Modifying
    void deleteGenresWithoutBooks();
}