package ru.otus.homework.libraryJdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.libraryJdbc.dao.util.BookRelation;
import ru.otus.homework.libraryJdbc.model.Author;
import ru.otus.homework.libraryJdbc.model.Book;
import ru.otus.homework.libraryJdbc.model.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public Long count() {
        return jdbc.getJdbcOperations().queryForObject("select count(id) from books", Long.class);
    }

    @Override
    public Book getById(long id) {
        Book book;

        try {
            book = jdbc.queryForObject("select id, title from books where books.id = :id",
                    Map.of("id", id), new BookMapper());
            book.setAuthors(authorDao.getAllByBookId(id));
            book.setGenres(genreDao.getAllByBookId(id));

        } catch (EmptyResultDataAccessException e) {
            book = null;
        }

        return book;
    }

    @Override
    public long save(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource("title", book.getTitle());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update("insert into books (title) values (:title)", params, kh);
        book.setId(kh.getKey().longValue());
        saveBookRelations(book);
        return book.getId();
    }

    @Override
    public void update(Book book) {
        cleanBookRelations(book.getId());

        Map<String, Object> params = Map.of("id", book.getId(), "title", book.getTitle());
        jdbc.update("update books set title = :title where id = :id", params);
        saveBookRelations(book);
        authorDao.deleteAuthorsWithoutBooks();
        genreDao.deleteGenresWithoutBooks();
    }

    @Override
    public void deleteById(long id) {
        cleanBookRelations(id);
        jdbc.update("delete from books where id = :id", Map.of("id", id));
        authorDao.deleteAuthorsWithoutBooks();
        genreDao.deleteGenresWithoutBooks();
    }

    @Override
    public List<Book> getAll() {
        Map<Long, Book> bookMap = jdbc.query("select * from books", new BookMapper()).stream().collect(Collectors.toMap(Book::getId, c -> c));

        setAuthors2Books(bookMap);
        setGenres2Books(bookMap);

        return new ArrayList<>(bookMap.values());
    }

    private void saveBookRelations(Book book) {
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).collect(Collectors.toList());
        for (Long authorId : authorIds) {
            jdbc.update("insert into books_authors (book_id, author_id) values (:book_id, :author_id)",
                    Map.of("book_id", book.getId(), "author_id", authorId));
        }

        List<Long> genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        for (Long genreId : genreIds) {
            jdbc.update("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)",
                    Map.of("book_id", book.getId(), "genre_id", genreId));
        }
    }

    private void cleanBookRelations(long bookId) {
        Map<String, Object> params = Map.of("bookId", bookId);
        jdbc.update("delete from books_authors where book_id = :bookId", params);
        jdbc.update("delete from books_genres where book_id = :bookId", params);
    }

    private void setAuthors2Books(Map<Long, Book> books) {
        List<BookRelation> relations = jdbc.query("select book_id, author_id from books_authors order by book_id, author_id",
                (rs, i) -> new BookRelation(rs.getLong(1), rs.getLong(2)));
        Map<Long, Author> authorsMap = authorDao.getAll().stream().collect(Collectors.toMap(Author::getId, c -> c));

        relations.forEach(r -> {
            if (books.containsKey(r.getBookId()) && authorsMap.containsKey(r.getRelatedId())) {
                books.get(r.getBookId()).getAuthors().add(authorsMap.get(r.getRelatedId()));
            }
        });
    }

    private void setGenres2Books(Map<Long, Book> books) {
        List<BookRelation> relations = jdbc.query("select book_id, genre_id from books_genres order by book_id, genre_id",
                (rs, i) -> new BookRelation(rs.getLong(1), rs.getLong(2)));
        Map<Long, Genre> genresMap = genreDao.getAll().stream().collect(Collectors.toMap(Genre::getId, c -> c));

        relations.forEach(r -> {
            if (books.containsKey(r.getBookId()) && genresMap.containsKey(r.getRelatedId())) {
                books.get(r.getBookId()).getGenres().add(genresMap.get(r.getRelatedId()));
            }
        });
    }

    private class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getNString("title");
            return new Book(id, name, new ArrayList<>(), new ArrayList<>());
        }
    }
}