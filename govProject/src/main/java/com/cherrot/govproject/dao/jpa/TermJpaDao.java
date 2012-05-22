/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermTaxonomy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Parameter;
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
public class TermJpaDao implements Serializable, TermDao {

    @PersistenceContext
    private EntityManager em;
//    public TermJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Term term) {
        if (term.getTermTaxonomyList() == null) {
            term.setTermTaxonomyList(new ArrayList<TermTaxonomy>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            List<TermTaxonomy> attachedTermTaxonomyList = new ArrayList<TermTaxonomy>();
            for (TermTaxonomy termTaxonomyListTermTaxonomyToAttach : term.getTermTaxonomyList()) {
                termTaxonomyListTermTaxonomyToAttach = em.getReference(termTaxonomyListTermTaxonomyToAttach.getClass(), termTaxonomyListTermTaxonomyToAttach.getId());
                attachedTermTaxonomyList.add(termTaxonomyListTermTaxonomyToAttach);
            }
            term.setTermTaxonomyList(attachedTermTaxonomyList);
            em.persist(term);
            for (TermTaxonomy termTaxonomyListTermTaxonomy : term.getTermTaxonomyList()) {
                Term oldTermIdOfTermTaxonomyListTermTaxonomy = termTaxonomyListTermTaxonomy.getTermId();
                termTaxonomyListTermTaxonomy.setTermId(term);
                termTaxonomyListTermTaxonomy = em.merge(termTaxonomyListTermTaxonomy);
                if (oldTermIdOfTermTaxonomyListTermTaxonomy != null) {
                    oldTermIdOfTermTaxonomyListTermTaxonomy.getTermTaxonomyList().remove(termTaxonomyListTermTaxonomy);
                    oldTermIdOfTermTaxonomyListTermTaxonomy = em.merge(oldTermIdOfTermTaxonomyListTermTaxonomy);
                }
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
    public void edit(Term term) throws IllegalOrphanException, NonexistentEntityException {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term persistentTerm = em.find(Term.class, term.getId());
            List<TermTaxonomy> termTaxonomyListOld = persistentTerm.getTermTaxonomyList();
            List<TermTaxonomy> termTaxonomyListNew = term.getTermTaxonomyList();
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
            term.setTermTaxonomyList(termTaxonomyListNew);
            term = em.merge(term);
            for (TermTaxonomy termTaxonomyListNewTermTaxonomy : termTaxonomyListNew) {
                if (!termTaxonomyListOld.contains(termTaxonomyListNewTermTaxonomy)) {
                    Term oldTermIdOfTermTaxonomyListNewTermTaxonomy = termTaxonomyListNewTermTaxonomy.getTermId();
                    termTaxonomyListNewTermTaxonomy.setTermId(term);
                    termTaxonomyListNewTermTaxonomy = em.merge(termTaxonomyListNewTermTaxonomy);
                    if (oldTermIdOfTermTaxonomyListNewTermTaxonomy != null && !oldTermIdOfTermTaxonomyListNewTermTaxonomy.equals(term)) {
                        oldTermIdOfTermTaxonomyListNewTermTaxonomy.getTermTaxonomyList().remove(termTaxonomyListNewTermTaxonomy);
                        oldTermIdOfTermTaxonomyListNewTermTaxonomy = em.merge(oldTermIdOfTermTaxonomyListNewTermTaxonomy);
                    }
                }
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = term.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The term with id " + id + " no longer exists.");
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
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term term;
            try {
                term = em.getReference(Term.class, id);
                term.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The term with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TermTaxonomy> termTaxonomyListOrphanCheck = term.getTermTaxonomyList();
            for (TermTaxonomy termTaxonomyListOrphanCheckTermTaxonomy : termTaxonomyListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Term (" + term + ") cannot be destroyed since the TermTaxonomy " + termTaxonomyListOrphanCheckTermTaxonomy + " in its termTaxonomyList field has a non-nullable termId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(term);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Term> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Term> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Term> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Term.class));
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
    public Term find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Term.class, id);
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public Term findBySlug(String slug) {
        return em.createNamedQuery("Term.findBySlug", Term.class).setParameter("slug", slug).getSingleResult();
    }

    @Override
    public int getCount() {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Term> rt = cq.from(Term.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public List<Term> findEntitiesByName(String name) {
        return em.createNamedQuery("Term.findByName",Term.class).setParameter("name", name).getResultList();
    }

    @Override
    public void save(Term model) {
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
