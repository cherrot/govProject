/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.UsermetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Usermeta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class UsermetaJpaDao implements UsermetaDao {

    public UsermetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Usermeta usermeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = usermeta.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                usermeta.setUserId(userId);
            }
            em.persist(usermeta);
            if (userId != null) {
                userId.getUsermetaList().add(usermeta);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Usermeta usermeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usermeta persistentUsermeta = em.find(Usermeta.class, usermeta.getId());
            Users userIdOld = persistentUsermeta.getUserId();
            Users userIdNew = usermeta.getUserId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                usermeta.setUserId(userIdNew);
            }
            usermeta = em.merge(usermeta);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getUsermetaList().remove(usermeta);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getUsermetaList().add(usermeta);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usermeta.getId();
                if (findUsermeta(id) == null) {
                    throw new NonexistentEntityException("The usermeta with id " + id + " no longer exists.");
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
            Usermeta usermeta;
            try {
                usermeta = em.getReference(Usermeta.class, id);
                usermeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usermeta with id " + id + " no longer exists.", enfe);
            }
            Users userId = usermeta.getUserId();
            if (userId != null) {
                userId.getUsermetaList().remove(usermeta);
                userId = em.merge(userId);
            }
            em.remove(usermeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Usermeta> findUsermetaEntities() {
        return findUsermetaEntities(true, -1, -1);
    }

    @Override
    public List<Usermeta> findUsermetaEntities(int maxResults, int firstResult) {
        return findUsermetaEntities(false, maxResults, firstResult);
    }

    private List<Usermeta> findUsermetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usermeta.class));
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
    public Usermeta findUsermeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usermeta.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getUsermetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usermeta> rt = cq.from(Usermeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
