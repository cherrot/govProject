/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.CommentmetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Repository
public class CommentmetaJpaDao implements CommentmetaDao {

    @PersistenceContext
    private EntityManager em;
//    public CommentmetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;

//    @Override
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    @Override
    @Transactional
    public void create(Commentmeta commentmeta) {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
        Comment comment = commentmeta.getComment();
        if (comment != null) {
            comment = em.getReference(Comment.class, comment.getId());
            commentmeta.setComment(comment);
        }
        em.persist(commentmeta);
        if (comment != null) {
            comment.getCommentmetaList().add(commentmeta);
            comment = em.merge(comment);
        }
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    @Transactional
    public void edit(Commentmeta commentmeta) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Commentmeta persistentCommentmeta = em.find(Commentmeta.class, commentmeta.getId());
            Comment commentOld = persistentCommentmeta.getComment();
            Comment commentNew = commentmeta.getComment();
            if (commentNew != null) {
                commentNew = em.getReference(Comment.class, commentNew.getId());
                commentmeta.setComment(commentNew);
            }
            commentmeta = em.merge(commentmeta);
            if (commentOld != null && !commentOld.equals(commentNew)) {
                commentOld.getCommentmetaList().remove(commentmeta);
                commentOld = em.merge(commentOld);
            }
            if (commentNew != null && !commentNew.equals(commentOld)) {
                commentNew.getCommentmetaList().add(commentmeta);
                commentNew = em.merge(commentNew);
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = commentmeta.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The commentmeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    @Transactional
    public void destroy(Integer id) throws NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
        Commentmeta commentmeta;
        try {
            commentmeta = em.getReference(Commentmeta.class, id);
            commentmeta.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The commentmeta with id " + id + " no longer exists.", enfe);
        }
        Comment comment = commentmeta.getComment();
        if (comment != null) {
            comment.getCommentmetaList().remove(commentmeta);
            comment = em.merge(comment);
        }
        em.remove(commentmeta);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Commentmeta> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Commentmeta> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Commentmeta> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Commentmeta.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public Commentmeta find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
        return em.find(Commentmeta.class, id);
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public int getCount() {
//        EntityManager em = getEntityManager();
//        try {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Commentmeta> rt = cq.from(Commentmeta.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public Commentmeta getReference(Integer id) {
        return em.getReference(Commentmeta.class, id);
    }
}
