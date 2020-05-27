package ru.otus.homework.libraryJpql.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_authors", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private List<Comment> comments;

    public Book(String title) {
        this.title = title;
    }

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book #" + id + " Title=" + title + ", authors=" + authors.stream().map(Author::getName).collect(Collectors.joining(",")).trim()
                + ", comments=" + comments.stream().map(Comment::getText).collect(Collectors.joining(", ")).trim();
    }
}