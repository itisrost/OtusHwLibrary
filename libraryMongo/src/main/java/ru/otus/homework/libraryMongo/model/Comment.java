package ru.otus.homework.libraryMongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Field("text")
    private String text;

    @DBRef
    @Field("book")
    private Book book;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment #" + id +". Text=" + text;
    }
}