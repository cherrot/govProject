/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.UsermetaDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.model.Usermeta;
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
public class UsermetaJpaDao implements Serializable, UsermetaDao {

    @PersistenceContext
    private EntityManager em;
//    public UsermetaJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Usermeta usermeta) {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            User userId = usermeta.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                usermeta.setUserId(userId);
            }
            em.persist(usermeta);
            if (userId != null) {
                userId.getUsermetaList().add(usermeta);
                userId = em.merge(userId);
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
    public void edit(Usermeta usermeta) throws NonexistentEntityException {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Usermeta persistentUsermeta = em.find(Usermeta.class, usermeta.getId());
            User userIdOld = persistentUsermeta.getUserId();
            User userIdNew = usermeta.getUserId();
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
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usermeta.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The usermeta with id " + id + " no longer exists.");
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
            Usermeta usermeta;
            try {
                usermeta = em.getReference(Usermeta.class, id);
                usermeta.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usermeta with id " + id + " no longer exists.", enfe);
            }
            User userId = usermeta.getUserId();
            if (userId != null) {
                userId.getUsermetaList().remove(usermeta);
                userId = em.merge(userId);
            }
            em.remove(usermeta);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Usermeta> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Usermeta> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Usermeta> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usermeta.class));
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
    public Usermeta find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Usermeta.class, id);
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
            Root<Usermeta> rt = cq.from(Usermeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public void save(Usermeta model) {
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
