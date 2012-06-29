/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.LinkDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
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
public class LinkJpaDao implements LinkDao {

    @PersistenceContext
    private EntityManager em;
//    public LinkJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Link link) {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
        LinkCategory linkCategory = link.getLinkCategory();
        if (linkCategory != null) {
            linkCategory = em.getReference(LinkCategory.class, linkCategory.getId());
            link.setLinkCategory(linkCategory);
        }
        em.persist(link);
        if (linkCategory != null) {
            linkCategory.getLinkList().add(link);
            //设置count字段
            linkCategory.setCount(linkCategory.getCount() + 1);
            linkCategory = em.merge(linkCategory);
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
    public void edit(Link link) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Link persistentLink = em.find(Link.class, link.getId());
            LinkCategory linkCategoryOld = persistentLink.getLinkCategory();
            LinkCategory linkCategoryNew = link.getLinkCategory();
            if (linkCategoryNew != null) {
                linkCategoryNew = em.getReference(LinkCategory.class, linkCategoryNew.getId());
                link.setLinkCategory(linkCategoryNew);
            }
            link = em.merge(link);
            if (linkCategoryOld != null && !linkCategoryOld.equals(linkCategoryNew)) {
                linkCategoryOld.getLinkList().remove(link);
                //设置count字段
                linkCategoryOld.setCount(linkCategoryOld.getCount() - 1);
                linkCategoryOld = em.merge(linkCategoryOld);
            }
            if (linkCategoryNew != null && !linkCategoryNew.equals(linkCategoryOld)) {
                linkCategoryNew.getLinkList().add(link);
                //设置count字段
                linkCategoryNew.setCount(linkCategoryNew.getCount() + 1);
                linkCategoryNew = em.merge(linkCategoryNew);
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = link.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The link with id " + id + " no longer exists.");
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
        Link link;
        try {
            link = em.getReference(Link.class, id);
            link.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The link with id " + id + " no longer exists.", enfe);
        }
        LinkCategory linkCategory = link.getLinkCategory();
        if (linkCategory != null) {
            linkCategory.getLinkList().remove(link);
            //设置count字段
            linkCategory.setCount(linkCategory.getCount() - 1);
            linkCategory = em.merge(linkCategory);
        }
        em.remove(link);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Link> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Link> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Link> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Link.class));
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
    public Link find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
        return em.find(Link.class, id);
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
        Root<Link> rt = cq.from(Link.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public Link getReference(Integer id) {
        return em.getReference(Link.class, id);
    }
}
