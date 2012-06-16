/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.SiteLogDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.SiteLog;
import com.cherrot.govproject.model.User;
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
public class SiteLogJpaDao implements SiteLogDao {

    @PersistenceContext
    private EntityManager em;
//    public SiteLogJpaDao(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    @Transactional
    @Override
    public void create(SiteLog siteLog) {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            User user = siteLog.getUser();
            if (user != null) {
//                user = em.getReference(user.getClass(), user.getId());
                //TODO 怀疑是延时加载导致user.getClass() 不等于 User.class。
                //显示结果：
                //getClass:::class com.cherrot.govproject.model.User_$$_javassist_4
                //getClass:::class com.cherrot.govproject.model.User
//                if (!user.getClass().equals(User.class)){
//                    System.err.println("getClass:::"+user.getClass());
//                    System.err.println("getClass:::"+User.class);
//                }
                user = em.getReference(User.class, user.getId());
                siteLog.setUser(user);
            }
            em.persist(siteLog);
            if (user != null) {
                user.getSiteLogList().add(siteLog);
                user = em.merge(user);
            }
//            em.getTransaction().commit();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Transactional
    @Override
    public void edit(SiteLog siteLog) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            SiteLog persistentSiteLog = em.find(SiteLog.class, siteLog.getId());
            User userOld = persistentSiteLog.getUser();
            User userNew = siteLog.getUser();
            if (userNew != null) {
                userNew = em.getReference(User.class, userNew.getId());
                siteLog.setUser(userNew);
            }
            siteLog = em.merge(siteLog);
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getSiteLogList().remove(siteLog);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getSiteLogList().add(siteLog);
                userNew = em.merge(userNew);
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = siteLog.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The siteLog with id " + id + " no longer exists.");
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

    @Transactional
    @Override
    public void destroy(Integer id) throws NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            SiteLog siteLog;
            try {
                siteLog = em.getReference(SiteLog.class, id);
                siteLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The siteLog with id " + id + " no longer exists.", enfe);
            }
            User user = siteLog.getUser();
            if (user != null) {
                user.getSiteLogList().remove(siteLog);
                user = em.merge(user);
            }
            em.remove(siteLog);
            em.getTransaction().commit();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<SiteLog> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<SiteLog> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<SiteLog> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SiteLog.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
//        } finally {
//            em.close();
//        }
    }

    @Override
    public SiteLog find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(SiteLog.class, id);
//        } finally {
//            em.close();
//        }
    }

    @Override
    public int getCount() {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SiteLog> rt = cq.from(SiteLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
//        } finally {
//            em.close();
//        }
    }

    @Override
    public SiteLog getReference(Integer id) {
        return em.getReference(SiteLog.class, id);
    }

}
