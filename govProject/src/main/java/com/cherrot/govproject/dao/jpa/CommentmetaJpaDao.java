/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.CommentmetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Commentmeta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Comments;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class CommentmetaJpaDao implements CommentmetaDao {

    public CommentmetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void create(Commentmeta commentmeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments commentId = commentmeta.getCommentId();
            if (commentId != null) {
                commentId = em.getReference(commentId.getClass(), commentId.getId());
                commentmeta.setCommentId(commentId);
            }
            em.persist(commentmeta);
            if (commentId != null) {
                commentId.getCommentmetaList().add(commentmeta);
                commentId = em.merge(commentId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Commentmeta commentmeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commentmeta persistentCommentmeta = em.find(Commentmeta.class, commentmeta.getId());
            Comments commentIdOld = persistentCommentmeta.getCommentId();
            Comments commentIdNew = commentmeta.getCommentId();
            if (commentIdNew != null) {
                commentIdNew = em.getReference(commentIdNew.getClass(), commentIdNew.getId());
                commentmeta.setCommentId(commentIdNew);
            }
            commentmeta = em.merge(commentmeta);
            if (commentIdOld != null && !commentIdOld.equals(commentIdNew)) {
                commentIdOld.getCommentmetaList().remove(commentmeta);
                commentIdOld = em.merge(commentIdOld);
            }
            if (commentIdNew != null && !commentIdNew.equals(commentIdOld)) {
                commentIdNew.getCommentmetaList().add(commentmeta);
                commentIdNew = em.merge(commentIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = commentmeta.getId();
                if (findCommentmeta(id) == null) {
                    throw new NonexistentEntityException("The commentmeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commentmeta commentmeta;
            try {
                commentmeta = em.getReference(Commentmeta.class, id);
                commentmeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commentmeta with id " + id + " no longer exists.", enfe);
            }
            Comments commentId = commentmeta.getCommentId();
            if (commentId != null) {
                commentId.getCommentmetaList().remove(commentmeta);
                commentId = em.merge(commentId);
            }
            em.remove(commentmeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Commentmeta> findCommentmetaEntities() {
        return findCommentmetaEntities(true, -1, -1);
    }

    @Override
    public List<Commentmeta> findCommentmetaEntities(int maxResults, int firstResult) {
        return findCommentmetaEntities(false, maxResults, firstResult);
    }

    private List<Commentmeta> findCommentmetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Commentmeta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Commentmeta findCommentmeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Commentmeta.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getCommentmetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Commentmeta> rt = cq.from(Commentmeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
