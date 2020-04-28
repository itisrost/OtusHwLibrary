package ru.otus.homework.libraryJdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJdbc.model.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Long count() {
        return jdbc.getJdbcOperations().queryForObject("select count(id) from books", Long.class);
    }

    @Override
    public Optional<Book> getById(long id) {
        Optional<Book> book;

        try {
            book = Optional.ofNullable(jdbc.queryForObject("select id, title from books where books.id = :id",
                    Map.of("id", id), new BookMapper()));
        } catch (EmptyResultDataAccessException e) {
            book = Optional.empty();
        }

        return book;
    }

    @Override
    public long save(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource("title", book.getTitle());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update("insert into books (title) values (:title)", params, kh);
        return kh.getKey().longValue();

    }

    @Override
    public void update(Book book) {
        cleanBookRelations(book.getId());

        Map<String, Object> params = Map.of("id", book.getId(), "title", book.getTitle());
        jdbc.update("update books set title = :title where id = :id", params);
    }

    @Override
    public void deleteById(long id) {
        cleanBookRelations(id);
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from books", new BookMapper());
    }

    @Override
    public void saveBookRelations(long bookId, List<Long> authorIds, List<Long> genreIds) {
        for (Long authorId : authorIds) {
            jdbc.update("insert into books_authors (book_id, author_id) values (:book_id, :author_id)",
                    Map.of("book_id", bookId, "author_id", authorId));
        }
        for (Long genreId : genreIds) {
            jdbc.update("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)",
                    Map.of("book_id", bookId, "genre_id", genreId));
        }
    }

    private void cleanBookRelations(long bookId) {
        Map<String, Object> params = Map.of("bookId", bookId);
        jdbc.update("delete from books_authors where book_id = :bookId", params);
        jdbc.update("delete from books_genres where book_id = :bookId", params);
    }

    private class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getNString("title");
            return new Book(id, name);
        }
    }
}