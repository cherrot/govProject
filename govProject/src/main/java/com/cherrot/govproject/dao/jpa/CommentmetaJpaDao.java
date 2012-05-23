/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.CommentmetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import java.io.Serializable;
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
public class CommentmetaJpaDao implements Serializable, CommentmetaDao {

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
            Comment commentId = commentmeta.getComment();
            if (commentId != null) {
                commentId = em.getReference(commentId.getClass(), commentId.getId());
                commentmeta.setComment(commentId);
            }
            em.persist(commentmeta);
            if (commentId != null) {
                commentId.getCommentmetaList().add(commentmeta);
                commentId = em.merge(commentId);
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
            Comment commentIdOld = persistentCommentmeta.getComment();
            Comment commentIdNew = commentmeta.getComment();
            if (commentIdNew != null) {
                commentIdNew = em.getReference(commentIdNew.getClass(), commentIdNew.getId());
                commentmeta.setComment(commentIdNew);
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
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
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
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commentmeta with id " + id + " no longer exists.", enfe);
            }
            Comment commentId = commentmeta.getComment();
            if (commentId != null) {
                commentId.getCommentmetaList().remove(commentmeta);
                commentId = em.merge(commentId);
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
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public void save(Commentmeta model) {
        if (model.getId() == null) {
            create(model);
        } else {
            try {
                edit(model);
            }
            catch (Exception ex) {
                Logger.getLogger(CommentmetaJpaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
