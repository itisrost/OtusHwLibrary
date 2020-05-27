package ru.otus.homework.libraryJpql.dao;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import ru.otus.homework.libraryJpql.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CommentDaoJpql implements CommentDao {

    @PersistenceContext
    private EntityManager em;;

    @Override
    public Long count() {
        return em.createQuery("select count(c.id) from Comment c", Long.class).getSingleResult();
    }

    @Override
    public Optional<Comment> getById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public long save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
        } else {
            em.merge(comment);
        }
        return comment.getId();
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }
}