/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermsDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.TermTaxonomy;
import com.cherrot.govproject.model.Terms;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class TermsJpaDao implements TermsDao {

    public TermsJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Terms terms) {
        if (terms.getTermTaxonomyList() == null) {
            terms.setTermTaxonomyList(new ArrayList<TermTaxonomy>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<TermTaxonomy> attachedTermTaxonomyList = new ArrayList<TermTaxonomy>();
            for (TermTaxonomy termTaxonomyListTermTaxonomyToAttach : terms.getTermTaxonomyList()) {
                termTaxonomyListTermTaxonomyToAttach = em.getReference(termTaxonomyListTermTaxonomyToAttach.getClass(), termTaxonomyListTermTaxonomyToAttach.getId());
                attachedTermTaxonomyList.add(termTaxonomyListTermTaxonomyToAttach);
            }
            terms.setTermTaxonomyList(attachedTermTaxonomyList);
            em.persist(terms);
            for (TermTaxonomy termTaxonomyListTermTaxonomy : terms.getTermTaxonomyList()) {
                Terms oldTermIdOfTermTaxonomyListTermTaxonomy = termTaxonomyListTermTaxonomy.getTermId();
                termTaxonomyListTermTaxonomy.setTermId(terms);
                termTaxonomyListTermTaxonomy = em.merge(termTaxonomyListTermTaxonomy);
                if (oldTermIdOfTermTaxonomyListTermTaxonomy != null) {
                    oldTermIdOfTermTaxonomyListTermTaxonomy.getTermTaxonomyList().remove(termTaxonomyListTermTaxonomy);
                    oldTermIdOfTermTaxonomyListTermTaxonomy = em.merge(oldTermIdOfTermTaxonomyListTermTaxonomy);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void edit(Terms terms) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terms persistentTerms = em.find(Terms.class, terms.getId());
            List<TermTaxonomy> termTaxonomyListOld = persistentTerms.getTermTaxonomyList();
            List<TermTaxonomy> termTaxonomyListNew = terms.getTermTaxonomyList();
            List<String> illegalOrphanMessages = null;
            for (TermTaxonomy termTaxonomyListOldTermTaxonomy : termTaxonomyListOld) {
                if (!termTaxonomyListNew.contains(termTaxonomyListOldTermTaxonomy)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermTaxonomy " + termTaxonomyListOldTermTaxonomy + " since its termId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<TermTaxonomy> attachedTermTaxonomyListNew = new ArrayList<TermTaxonomy>();
            for (TermTaxonomy termTaxonomyListNewTermTaxonomyToAttach : termTaxonomyListNew) {
                termTaxonomyListNewTermTaxonomyToAttach = em.getReference(termTaxonomyListNewTermTaxonomyToAttach.getClass(), termTaxonomyListNewTermTaxonomyToAttach.getId());
                attachedTermTaxonomyListNew.add(termTaxonomyListNewTermTaxonomyToAttach);
            }
            termTaxonomyListNew = attachedTermTaxonomyListNew;
            terms.setTermTaxonomyList(termTaxonomyListNew);
            terms = em.merge(terms);
            for (TermTaxonomy termTaxonomyListNewTermTaxonomy : termTaxonomyListNew) {
                if (!termTaxonomyListOld.contains(termTaxonomyListNewTermTaxonomy)) {
                    Terms oldTermIdOfTermTaxonomyListNewTermTaxonomy = termTaxonomyListNewTermTaxonomy.getTermId();
                    termTaxonomyListNewTermTaxonomy.setTermId(terms);
                    termTaxonomyListNewTermTaxonomy = em.merge(termTaxonomyListNewTermTaxonomy);
                    if (oldTermIdOfTermTaxonomyListNewTermTaxonomy != null && !oldTermIdOfTermTaxonomyListNewTermTaxonomy.equals(terms)) {
                        oldTermIdOfTermTaxonomyListNewTermTaxonomy.getTermTaxonomyList().remove(termTaxonomyListNewTermTaxonomy);
                        oldTermIdOfTermTaxonomyListNewTermTaxonomy = em.merge(oldTermIdOfTermTaxonomyListNewTermTaxonomy);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = terms.getId();
                if (findTerms(id) == null) {
                    throw new NonexistentEntityException("The terms with id " + id + " no longer exists.");
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
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Terms terms;
            try {
                terms = em.getReference(Terms.class, id);
                terms.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The terms with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TermTaxonomy> termTaxonomyListOrphanCheck = terms.getTermTaxonomyList();
            for (TermTaxonomy termTaxonomyListOrphanCheckTermTaxonomy : termTaxonomyListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Terms (" + terms + ") cannot be destroyed since the TermTaxonomy " + termTaxonomyListOrphanCheckTermTaxonomy + " in its termTaxonomyList field has a non-nullable termId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(terms);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Terms> findTermsEntities() {
        return findTermsEntities(true, -1, -1);
    }

    @Override
    public List<Terms> findTermsEntities(int maxResults, int firstResult) {
        return findTermsEntities(false, maxResults, firstResult);
    }

    private List<Terms> findTermsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Terms.class));
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
    public Terms findTerms(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Terms.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public int getTermsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Terms> rt = cq.from(Terms.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
