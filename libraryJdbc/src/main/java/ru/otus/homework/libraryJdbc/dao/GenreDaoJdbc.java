package ru.otus.homework.libraryJdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJdbc.model.Genre;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Long count() {
        return jdbc.getJdbcOperations().queryForObject("select count(id) from genres", Long.class);
    }

    @Override
    public Optional<Genre> getById(long id) {
        Optional<Genre> genre;

        try {
            genre = Optional.ofNullable(jdbc.queryForObject("select * from genres where id = :id",
                    Map.of("id", id), new GenreMapper()));
        } catch (EmptyResultDataAccessException e) {
            genre = Optional.empty();
        }

        return genre;
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        Optional<Long> genreId;

        try {
            genreId = Optional.ofNullable(jdbc.queryForObject("select id from genres where name = :name",
                    Map.of("name", name), Long.class));
        } catch (EmptyResultDataAccessException e) {
            genreId = Optional.empty();
        }

        return genreId;
    }

    @Override
    public long save(Genre genre) {
        MapSqlParameterSource params = new MapSqlParameterSource("name", genre.getName());
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update("insert into genres (name) values (:name)", params, kh, new String[]{"id"});
        return kh.getKey().longValue();
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genres", new GenreMapper());
    }

    @Override
    public List<Genre> getAllByBookId(long bookId) {
        return jdbc.query("select genres.* from books_genres " +
                "left join genres on genres.id = books_genres.genre_id " +
                "where books_genres.book_id = :bookId", Map.of("bookId", bookId), new GenreMapper());
    }

    @Override
    public void deleteGenresWithoutBooks() {
        jdbc.getJdbcOperations().update("delete from genres " +
                "where id not in (select distinct genre_id from books_genres)");
    }

    @Override
    public void update(Genre genre) {
        Map<String, Object> params = Map.of("id", genre.getId(), "name", genre.getName());
        jdbc.update("update genres set name = :name where id = :id", params);
    }

    private class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getNString("name");
            return new Genre(id, name);
        }
    }
}