package ru.otus.homework.libraryJdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJdbc.model.Author;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Long count() {
        return jdbc.getJdbcOperations().queryForObject("select count(id) from authors", Long.class);
    }

    @Override
    public Optional<Author> getById(long id) {
        Optional<Author> author;

        try {
            author = Optional.ofNullable(jdbc.queryForObject("select * from authors where id = :id",
                    Map.of("id", id), new AuthorMapper()));
        } catch (EmptyResultDataAccessException e) {
            author = Optional.empty();
        }

        return author;
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        Optional<Long> authorId;

        try {
            authorId = Optional.ofNullable(jdbc.queryForObject("select id from authors where name = :name",
                    Map.of("name", name), Long.class));
        } catch (EmptyResultDataAccessException e) {
            authorId = Optional.empty();
        }

        return authorId;
    }

    @Override
    public long save(Author author) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", author.getName());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update("insert into authors (name) values (:name)", params, kh, new String[]{"id"});
        return kh.getKey().longValue();
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from authors", new AuthorMapper());
    }

    @Override
    public List<Author> getAllByBookId(long bookId) {
        return jdbc.query("select authors.* from books_authors " +
                "left join authors on authors.id = books_authors.author_id " +
                "where books_authors.book_id = :bookId", Map.of("bookId", bookId), new AuthorMapper());
    }

    @Override
    public void deleteAuthorsWithoutBooks() {
        jdbc.getJdbcOperations().update("delete from authors " +
                "where id not in (select distinct author_id from books_authors)");
    }

    @Override
    public void update(Author author) {
        Map<String, Object> params = Map.of("id", author.getId(), "name", author.getName());
        jdbc.update("update authors set name = :name where id = :id", params);
    }

    private class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getNString("name");
            return new Author(id, name);
        }
    }
}