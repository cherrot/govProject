/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.PostmetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Postmeta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Posts;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class PostmetaJpaDao implements PostmetaDao {

    public PostmetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Postmeta postmeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posts postId = postmeta.getPostId();
            if (postId != null) {
                postId = em.getReference(postId.getClass(), postId.getId());
                postmeta.setPostId(postId);
            }
            em.persist(postmeta);
            if (postId != null) {
                postId.getPostmetaList().add(postmeta);
                postId = em.merge(postId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Postmeta postmeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Postmeta persistentPostmeta = em.find(Postmeta.class, postmeta.getId());
            Posts postIdOld = persistentPostmeta.getPostId();
            Posts postIdNew = postmeta.getPostId();
            if (postIdNew != null) {
                postIdNew = em.getReference(postIdNew.getClass(), postIdNew.getId());
                postmeta.setPostId(postIdNew);
            }
            postmeta = em.merge(postmeta);
            if (postIdOld != null && !postIdOld.equals(postIdNew)) {
                postIdOld.getPostmetaList().remove(postmeta);
                postIdOld = em.merge(postIdOld);
            }
            if (postIdNew != null && !postIdNew.equals(postIdOld)) {
                postIdNew.getPostmetaList().add(postmeta);
                postIdNew = em.merge(postIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = postmeta.getId();
                if (findPostmeta(id) == null) {
                    throw new NonexistentEntityException("The postmeta with id " + id + " no longer exists.");
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
            Postmeta postmeta;
            try {
                postmeta = em.getReference(Postmeta.class, id);
                postmeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The postmeta with id " + id + " no longer exists.", enfe);
            }
            Posts postId = postmeta.getPostId();
            if (postId != null) {
                postId.getPostmetaList().remove(postmeta);
                postId = em.merge(postId);
            }
            em.remove(postmeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Postmeta> findPostmetaEntities() {
        return findPostmetaEntities(true, -1, -1);
    }

    @Override
    public List<Postmeta> findPostmetaEntities(int maxResults, int firstResult) {
        return findPostmetaEntities(false, maxResults, firstResult);
    }

    private List<Postmeta> findPostmetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Postmeta.class));
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
    public Postmeta findPostmeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Postmeta.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getPostmetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Postmeta> rt = cq.from(Postmeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
