/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Links;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.TermRelationships;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class LinksJpaDao implements Serializable {

    public LinksJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Links links) {
        if (links.getTermRelationshipsList() == null) {
            links.setTermRelationshipsList(new ArrayList<TermRelationships>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TermRelationships> attachedTermRelationshipsList = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListTermRelationshipsToAttach : links.getTermRelationshipsList()) {
                termRelationshipsListTermRelationshipsToAttach = em.getReference(termRelationshipsListTermRelationshipsToAttach.getClass(), termRelationshipsListTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsList.add(termRelationshipsListTermRelationshipsToAttach);
            }
            links.setTermRelationshipsList(attachedTermRelationshipsList);
            em.persist(links);
            for (TermRelationships termRelationshipsListTermRelationships : links.getTermRelationshipsList()) {
                Links oldLinksOfTermRelationshipsListTermRelationships = termRelationshipsListTermRelationships.getLinks();
                termRelationshipsListTermRelationships.setLinks(links);
                termRelationshipsListTermRelationships = em.merge(termRelationshipsListTermRelationships);
                if (oldLinksOfTermRelationshipsListTermRelationships != null) {
                    oldLinksOfTermRelationshipsListTermRelationships.getTermRelationshipsList().remove(termRelationshipsListTermRelationships);
                    oldLinksOfTermRelationshipsListTermRelationships = em.merge(oldLinksOfTermRelationshipsListTermRelationships);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Links links) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Links persistentLinks = em.find(Links.class, links.getId());
            List<TermRelationships> termRelationshipsListOld = persistentLinks.getTermRelationshipsList();
            List<TermRelationships> termRelationshipsListNew = links.getTermRelationshipsList();
            List<String> illegalOrphanMessages = null;
            for (TermRelationships termRelationshipsListOldTermRelationships : termRelationshipsListOld) {
                if (!termRelationshipsListNew.contains(termRelationshipsListOldTermRelationships)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermRelationships " + termRelationshipsListOldTermRelationships + " since its links field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TermRelationships> attachedTermRelationshipsListNew = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListNewTermRelationshipsToAttach : termRelationshipsListNew) {
                termRelationshipsListNewTermRelationshipsToAttach = em.getReference(termRelationshipsListNewTermRelationshipsToAttach.getClass(), termRelationshipsListNewTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsListNew.add(termRelationshipsListNewTermRelationshipsToAttach);
            }
            termRelationshipsListNew = attachedTermRelationshipsListNew;
            links.setTermRelationshipsList(termRelationshipsListNew);
            links = em.merge(links);
            for (TermRelationships termRelationshipsListNewTermRelationships : termRelationshipsListNew) {
                if (!termRelationshipsListOld.contains(termRelationshipsListNewTermRelationships)) {
                    Links oldLinksOfTermRelationshipsListNewTermRelationships = termRelationshipsListNewTermRelationships.getLinks();
                    termRelationshipsListNewTermRelationships.setLinks(links);
                    termRelationshipsListNewTermRelationships = em.merge(termRelationshipsListNewTermRelationships);
                    if (oldLinksOfTermRelationshipsListNewTermRelationships != null && !oldLinksOfTermRelationshipsListNewTermRelationships.equals(links)) {
                        oldLinksOfTermRelationshipsListNewTermRelationships.getTermRelationshipsList().remove(termRelationshipsListNewTermRelationships);
                        oldLinksOfTermRelationshipsListNewTermRelationships = em.merge(oldLinksOfTermRelationshipsListNewTermRelationships);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = links.getId();
                if (findLinks(id) == null) {
                    throw new NonexistentEntityException("The links with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Links links;
            try {
                links = em.getReference(Links.class, id);
                links.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The links with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TermRelationships> termRelationshipsListOrphanCheck = links.getTermRelationshipsList();
            for (TermRelationships termRelationshipsListOrphanCheckTermRelationships : termRelationshipsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Links (" + links + ") cannot be destroyed since the TermRelationships " + termRelationshipsListOrphanCheckTermRelationships + " in its termRelationshipsList field has a non-nullable links field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(links);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Links> findLinksEntities() {
        return findLinksEntities(true, -1, -1);
    }

    public List<Links> findLinksEntities(int maxResults, int firstResult) {
        return findLinksEntities(false, maxResults, firstResult);
    }

    private List<Links> findLinksEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Links.class));
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

    public Links findLinks(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Links.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinksCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Links> rt = cq.from(Links.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
