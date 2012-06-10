/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.PostmetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PostmetaJpaDao implements PostmetaDao {

    @PersistenceContext
    private EntityManager em;
//    public PostmetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    @Override
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    @Override
    @Transactional
    public void create(Postmeta postmeta) {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Post post = postmeta.getPost();
            if (post != null) {
                post = em.getReference(post.getClass(), post.getId());
                postmeta.setPost(post);
            }
            em.persist(postmeta);
            if (post != null) {
                post.getPostmetaList().add(postmeta);
                post = em.merge(post);
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
    public void edit(Postmeta postmeta) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Postmeta persistentPostmeta = em.find(Postmeta.class, postmeta.getId());
            Post postOld = persistentPostmeta.getPost();
            Post postNew = postmeta.getPost();
            if (postNew != null) {
                postNew = em.getReference(postNew.getClass(), postNew.getId());
                postmeta.setPost(postNew);
            }
            postmeta = em.merge(postmeta);
            if (postOld != null && !postOld.equals(postNew)) {
                postOld.getPostmetaList().remove(postmeta);
                postOld = em.merge(postOld);
            }
            if (postNew != null && !postNew.equals(postOld)) {
                postNew.getPostmetaList().add(postmeta);
                postNew = em.merge(postNew);
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = postmeta.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The postmeta with id " + id + " no longer exists.");
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
            Postmeta postmeta;
            try {
                postmeta = em.getReference(Postmeta.class, id);
                postmeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The postmeta with id " + id + " no longer exists.", enfe);
            }
            Post post = postmeta.getPost();
            if (post != null) {
                post.getPostmetaList().remove(postmeta);
                post = em.merge(post);
            }
            em.remove(postmeta);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Postmeta> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Postmeta> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Postmeta> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Postmeta.class));
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
    public Postmeta find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Postmeta.class, id);
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
            Root<Postmeta> rt = cq.from(Postmeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

}
