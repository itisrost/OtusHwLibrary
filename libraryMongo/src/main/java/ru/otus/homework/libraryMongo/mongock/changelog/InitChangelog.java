package ru.otus.homework.libraryMongo.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.homework.libraryMongo.model.Author;
import ru.otus.homework.libraryMongo.model.Book;
import ru.otus.homework.libraryMongo.model.Comment;
import ru.otus.homework.libraryMongo.model.Genre;

import java.util.ArrayList;
import java.util.List;

@ChangeLog(order = "001")
public class InitChangelog {

    private final List<Author> authors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();

    @ChangeSet(order = "000", id = "drop", author = "Morozov Rostislav", runAlways = true)
    public void drop(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "Morozov Rostislav", runAlways = true)
    public void initAuthors(MongoTemplate template) {
        for (int i = 0; i < 6; i++) {
            Author author = new Author("Автор #" + i);
            template.save(author);
            authors.add(author);
        }
    }

    @ChangeSet(order = "002", id = "initGenres", author = "Morozov Rostislav", runAlways = true)
    public void initGenres(MongoTemplate template) {
        for (int i = 0; i < 6; i++) {
            Genre genre = new Genre("Жанр #" + i);
            template.save(genre);
            genres.add(genre);
        }
    }

    @ChangeSet(order = "003", id = "initBooks", author = "Morozov Rostislav", runAlways = true)
    public void initBooks(MongoTemplate template) {
        for (int i = 0; i < 5; i++) {
            List<Author> bookAuthors = List.of(authors.get(i), authors.get(i + 1));
            List<Genre> bookGenres = List.of(genres.get(i), genres.get(i + 1));
            Book book = new Book("Книга #" + i, bookAuthors, bookGenres);
            template.save(book);
            books.add(book);
        }
    }

    @ChangeSet(order = "004", id = "initComments", author = "Morozov Rostislav", runAlways = true)
    public void initComments(MongoTemplate template) {
        for (Book book : books) {
            Comment comment = new Comment(book, "Комментарий к " + book.getTitle());
            template.save(comment);
        }
    }
}
