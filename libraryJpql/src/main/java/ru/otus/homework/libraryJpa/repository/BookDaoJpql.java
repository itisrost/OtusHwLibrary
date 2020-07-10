package ru.otus.homework.libraryJpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ru.otus.homework.libraryJpa.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoJpql implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long count() {
        return em.createQuery("select count(b.id) from Book b", Long.class).getSingleResult();
    }

    @Override
    public Book getById(long id) {
        return em.find(Book.class, id);
    }

    @Override
    public long save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
        } else {
            em.merge(book);
        }
        return book.getId();
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> typedQuery = em.createQuery("select b from Book b", Book.class);
        return typedQuery.getResultList();
    }
}