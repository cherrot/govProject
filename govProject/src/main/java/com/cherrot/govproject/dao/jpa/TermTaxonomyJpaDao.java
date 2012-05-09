/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Terms;
import com.cherrot.govproject.model.TermRelationships;
import com.cherrot.govproject.model.TermTaxonomy;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class TermTaxonomyJpaDao implements Serializable {

    public TermTaxonomyJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TermTaxonomy termTaxonomy) {
        if (termTaxonomy.getTermRelationshipsList() == null) {
            termTaxonomy.setTermRelationshipsList(new ArrayList<TermRelationships>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terms termId = termTaxonomy.getTermId();
            if (termId != null) {
                termId = em.getReference(termId.getClass(), termId.getId());
                termTaxonomy.setTermId(termId);
            }
            List<TermRelationships> attachedTermRelationshipsList = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListTermRelationshipsToAttach : termTaxonomy.getTermRelationshipsList()) {
                termRelationshipsListTermRelationshipsToAttach = em.getReference(termRelationshipsListTermRelationshipsToAttach.getClass(), termRelationshipsListTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsList.add(termRelationshipsListTermRelationshipsToAttach);
            }
            termTaxonomy.setTermRelationshipsList(attachedTermRelationshipsList);
            em.persist(termTaxonomy);
            if (termId != null) {
                termId.getTermTaxonomyList().add(termTaxonomy);
                termId = em.merge(termId);
            }
            for (TermRelationships termRelationshipsListTermRelationships : termTaxonomy.getTermRelationshipsList()) {
                TermTaxonomy oldTermTaxonomyOfTermRelationshipsListTermRelationships = termRelationshipsListTermRelationships.getTermTaxonomy();
                termRelationshipsListTermRelationships.setTermTaxonomy(termTaxonomy);
                termRelationshipsListTermRelationships = em.merge(termRelationshipsListTermRelationships);
                if (oldTermTaxonomyOfTermRelationshipsListTermRelationships != null) {
                    oldTermTaxonomyOfTermRelationshipsListTermRelationships.getTermRelationshipsList().remove(termRelationshipsListTermRelationships);
                    oldTermTaxonomyOfTermRelationshipsListTermRelationships = em.merge(oldTermTaxonomyOfTermRelationshipsListTermRelationships);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TermTaxonomy termTaxonomy) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TermTaxonomy persistentTermTaxonomy = em.find(TermTaxonomy.class, termTaxonomy.getId());
            Terms termIdOld = persistentTermTaxonomy.getTermId();
            Terms termIdNew = termTaxonomy.getTermId();
            List<TermRelationships> termRelationshipsListOld = persistentTermTaxonomy.getTermRelationshipsList();
            List<TermRelationships> termRelationshipsListNew = termTaxonomy.getTermRelationshipsList();
            List<String> illegalOrphanMessages = null;
            for (TermRelationships termRelationshipsListOldTermRelationships : termRelationshipsListOld) {
                if (!termRelationshipsListNew.contains(termRelationshipsListOldTermRelationships)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermRelationships " + termRelationshipsListOldTermRelationships + " since its termTaxonomy field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (termIdNew != null) {
                termIdNew = em.getReference(termIdNew.getClass(), termIdNew.getId());
                termTaxonomy.setTermId(termIdNew);
            }
            List<TermRelationships> attachedTermRelationshipsListNew = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListNewTermRelationshipsToAttach : termRelationshipsListNew) {
                termRelationshipsListNewTermRelationshipsToAttach = em.getReference(termRelationshipsListNewTermRelationshipsToAttach.getClass(), termRelationshipsListNewTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsListNew.add(termRelationshipsListNewTermRelationshipsToAttach);
            }
            termRelationshipsListNew = attachedTermRelationshipsListNew;
            termTaxonomy.setTermRelationshipsList(termRelationshipsListNew);
            termTaxonomy = em.merge(termTaxonomy);
            if (termIdOld != null && !termIdOld.equals(termIdNew)) {
                termIdOld.getTermTaxonomyList().remove(termTaxonomy);
                termIdOld = em.merge(termIdOld);
            }
            if (termIdNew != null && !termIdNew.equals(termIdOld)) {
                termIdNew.getTermTaxonomyList().add(termTaxonomy);
                termIdNew = em.merge(termIdNew);
            }
            for (TermRelationships termRelationshipsListNewTermRelationships : termRelationshipsListNew) {
                if (!termRelationshipsListOld.contains(termRelationshipsListNewTermRelationships)) {
                    TermTaxonomy oldTermTaxonomyOfTermRelationshipsListNewTermRelationships = termRelationshipsListNewTermRelationships.getTermTaxonomy();
                    termRelationshipsListNewTermRelationships.setTermTaxonomy(termTaxonomy);
                    termRelationshipsListNewTermRelationships = em.merge(termRelationshipsListNewTermRelationships);
                    if (oldTermTaxonomyOfTermRelationshipsListNewTermRelationships != null && !oldTermTaxonomyOfTermRelationshipsListNewTermRelationships.equals(termTaxonomy)) {
                        oldTermTaxonomyOfTermRelationshipsListNewTermRelationships.getTermRelationshipsList().remove(termRelationshipsListNewTermRelationships);
                        oldTermTaxonomyOfTermRelationshipsListNewTermRelationships = em.merge(oldTermTaxonomyOfTermRelationshipsListNewTermRelationships);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = termTaxonomy.getId();
                if (findTermTaxonomy(id) == null) {
                    throw new NonexistentEntityException("The termTaxonomy with id " + id + " no longer exists.");
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
            TermTaxonomy termTaxonomy;
            try {
                termTaxonomy = em.getReference(TermTaxonomy.class, id);
                termTaxonomy.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The termTaxonomy with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TermRelationships> termRelationshipsListOrphanCheck = termTaxonomy.getTermRelationshipsList();
            for (TermRelationships termRelationshipsListOrphanCheckTermRelationships : termRelationshipsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TermTaxonomy (" + termTaxonomy + ") cannot be destroyed since the TermRelationships " + termRelationshipsListOrphanCheckTermRelationships + " in its termRelationshipsList field has a non-nullable termTaxonomy field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Terms termId = termTaxonomy.getTermId();
            if (termId != null) {
                termId.getTermTaxonomyList().remove(termTaxonomy);
                termId = em.merge(termId);
            }
            em.remove(termTaxonomy);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TermTaxonomy> findTermTaxonomyEntities() {
        return findTermTaxonomyEntities(true, -1, -1);
    }

    public List<TermTaxonomy> findTermTaxonomyEntities(int maxResults, int firstResult) {
        return findTermTaxonomyEntities(false, maxResults, firstResult);
    }

    private List<TermTaxonomy> findTermTaxonomyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TermTaxonomy.class));
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

    public TermTaxonomy findTermTaxonomy(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TermTaxonomy.class, id);
        } finally {
            em.close();
        }
    }

    public int getTermTaxonomyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TermTaxonomy> rt = cq.from(TermTaxonomy.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
