package ru.otus.homework.libraryMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.libraryMongo.model.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Genre findByName(String name);

//    @Query(value = "delete from genres where id not in (select distinct genre_id from books_genres)", nativeQuery = true)
//    @Modifying
//    void deleteGenresWithoutBooks();
}