package ru.otus.homework.libraryMongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Field("title")
    private String title;

    @DBRef
    @Field("authors")
    private List<Author> authors;

    @DBRef
    @Field("genres")
    private List<Genre> genres;

    @DBRef
    @Field("comments")
    private List<Comment> comments;

    public Book(String title, List<Author> authors, List<Genre> genres) {
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }

    public Book(String id, String title, List<Author> authors, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Book #" + id + " Title=" + title + ", authors=" + authors.stream().map(Author::getName).collect(Collectors.joining(", ")).trim()
                + ", genres=" + genres.stream().map(Genre::getName).collect(Collectors.joining(", ")).trim();
    }
}