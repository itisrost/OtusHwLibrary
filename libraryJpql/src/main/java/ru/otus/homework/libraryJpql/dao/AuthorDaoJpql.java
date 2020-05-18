package ru.otus.homework.libraryJpql.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import ru.otus.homework.libraryJpql.model.Author;
import org.springframework.stereotype.Repository;

@Transactional
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
    public Optional<Long> getIdByName(String name) {
        TypedQuery<Long> query = em.createQuery("select a.id from Author a where a.name = :name", Long.class);
        query.setParameter("name", name);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public long save(Author author) {
        if (author.getId() == 0) {
            em.persist(author);
        } else {
            em.merge(author);
        }
        return author.getId();
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