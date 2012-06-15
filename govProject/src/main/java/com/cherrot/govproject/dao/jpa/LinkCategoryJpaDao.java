/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.LinkCategoryDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
import java.util.ArrayList;
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
public class LinkCategoryJpaDao implements LinkCategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void create(LinkCategory linkCategory) {
        if (linkCategory.getLinkList() == null) {
            linkCategory.setLinkList(new ArrayList<Link>());
        }
        List<Link> attachedLinkList = new ArrayList<Link>();
        for (Link linkListLinkToAttach : linkCategory.getLinkList()) {
            linkListLinkToAttach = em.getReference(linkListLinkToAttach.getClass(), linkListLinkToAttach.getId());
            attachedLinkList.add(linkListLinkToAttach);
        }
        linkCategory.setLinkList(attachedLinkList);
        em.persist(linkCategory);
        for (Link linkListLink : linkCategory.getLinkList()) {
            LinkCategory oldLinkCategoryOfLinkListLink = linkListLink.getLinkCategory();
            linkListLink.setLinkCategory(linkCategory);
            linkListLink = em.merge(linkListLink);
            if (oldLinkCategoryOfLinkListLink != null) {
                oldLinkCategoryOfLinkListLink.getLinkList().remove(linkListLink);
                oldLinkCategoryOfLinkListLink = em.merge(oldLinkCategoryOfLinkListLink);
            }
        }
    }

    @Transactional
    @Override
    public void edit(LinkCategory linkCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        try {
            LinkCategory persistentLinkCategory = em.find(LinkCategory.class, linkCategory.getId());
            List<Link> linkListOld = persistentLinkCategory.getLinkList();
            List<Link> linkListNew = linkCategory.getLinkList();

            //FIXME 临时方案
            if (linkListNew == null) linkListNew = linkListOld;

            List<String> illegalOrphanMessages = null;
            for (Link linkListOldLink : linkListOld) {
                if (!linkListNew.contains(linkListOldLink)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Link " + linkListOldLink + " since its linkCategory field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Link> attachedLinkListNew = new ArrayList<Link>();
            for (Link linkListNewLinkToAttach : linkListNew) {
                linkListNewLinkToAttach = em.getReference(linkListNewLinkToAttach.getClass(), linkListNewLinkToAttach.getId());
                attachedLinkListNew.add(linkListNewLinkToAttach);
            }
            linkListNew = attachedLinkListNew;
            linkCategory.setLinkList(linkListNew);
            linkCategory = em.merge(linkCategory);
            for (Link linkListNewLink : linkListNew) {
                if (!linkListOld.contains(linkListNewLink)) {
                    LinkCategory oldLinkCategoryOfLinkListNewLink = linkListNewLink.getLinkCategory();
                    linkListNewLink.setLinkCategory(linkCategory);
                    linkListNewLink = em.merge(linkListNewLink);
                    if (oldLinkCategoryOfLinkListNewLink != null && !oldLinkCategoryOfLinkListNewLink.equals(linkCategory)) {
                        oldLinkCategoryOfLinkListNewLink.getLinkList().remove(linkListNewLink);
                        oldLinkCategoryOfLinkListNewLink = em.merge(oldLinkCategoryOfLinkListNewLink);
                    }
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = linkCategory.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The linkCategory with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Transactional
    @Override
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        LinkCategory linkCategory;
        try {
            linkCategory = em.getReference(LinkCategory.class, id);
            linkCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The linkCategory with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        List<Link> linkListOrphanCheck = linkCategory.getLinkList();
        for (Link linkListOrphanCheckLink : linkListOrphanCheck) {
            if (illegalOrphanMessages == null) {
                illegalOrphanMessages = new ArrayList<String>();
            }
            illegalOrphanMessages.add("This LinkCategory (" + linkCategory + ") cannot be destroyed since the Link " + linkListOrphanCheckLink + " in its linkList field has a non-nullable linkCategory field.");
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        em.remove(linkCategory);
    }

    @Override
    public List<LinkCategory> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<LinkCategory> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<LinkCategory> findEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(LinkCategory.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public LinkCategory find(Integer id) {
        return em.find(LinkCategory.class, id);
    }

    @Override
    public int getCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<LinkCategory> rt = cq.from(LinkCategory.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public LinkCategory getReference(Integer id) {
        return em.getReference(LinkCategory.class, id);
    }
}
