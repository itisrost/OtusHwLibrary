package ru.otus.homework.libraryJpa.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ru.otus.homework.libraryJpa.model.Author;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorDaoJpql implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long count() {
        return em.createQuery("select count(a.id) from Author a", Long.class).getSingleResult();
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Optional<Author> getByName(String name) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            em.persist(author);
        } else {
            em.merge(author);
        }
        return author;
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public void deleteAuthorsWithoutBooks() {
        em.createNativeQuery("delete from authors where id not in (select distinct author_id from books_authors)")
                .executeUpdate();
    }
}