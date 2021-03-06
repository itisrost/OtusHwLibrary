package ru.otus.homework.libraryJpql.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ru.otus.homework.libraryJpql.model.Genre;
import org.springframework.stereotype.Repository;

@Repository
public class GenreDaoJpql implements GenreDao {

    @PersistenceContext
    private EntityManager em;;

    @Override
    public Long count() {
        return em.createQuery("select count(g.id) from Genre g", Long.class).getSingleResult();
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Optional<Genre> getByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            em.persist(genre);
        } else {
            em.merge(genre);
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select a from Genre a", Genre.class);
        return query.getResultList();
    }

    @Override
    public void deleteGenresWithoutBooks() {
        em.createNativeQuery("delete from genres where id not in (select distinct genre_id from books_genres)")
                .executeUpdate();
    }
}