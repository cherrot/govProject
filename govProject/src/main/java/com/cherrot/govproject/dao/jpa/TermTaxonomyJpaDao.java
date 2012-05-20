/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermTaxonomyDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermRelationship;
import com.cherrot.govproject.model.TermTaxonomy;
import java.io.Serializable;
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
public class TermTaxonomyJpaDao implements Serializable, TermTaxonomyDao {

    @PersistenceContext
    private EntityManager em;
//    public TermTaxonomyJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(TermTaxonomy termTaxonomy) {
        if (termTaxonomy.getTermRelationshipList() == null) {
            termTaxonomy.setTermRelationshipList(new ArrayList<TermRelationship>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term termId = termTaxonomy.getTermId();
            if (termId != null) {
                termId = em.getReference(termId.getClass(), termId.getId());
                termTaxonomy.setTermId(termId);
            }
            List<TermRelationship> attachedTermRelationshipList = new ArrayList<TermRelationship>();
            for (TermRelationship termRelationshipListTermRelationshipToAttach : termTaxonomy.getTermRelationshipList()) {
                termRelationshipListTermRelationshipToAttach = em.getReference(termRelationshipListTermRelationshipToAttach.getClass(), termRelationshipListTermRelationshipToAttach.getTermRelationshipPK());
                attachedTermRelationshipList.add(termRelationshipListTermRelationshipToAttach);
            }
            termTaxonomy.setTermRelationshipList(attachedTermRelationshipList);
            em.persist(termTaxonomy);
            if (termId != null) {
                termId.getTermTaxonomyList().add(termTaxonomy);
                termId = em.merge(termId);
            }
            for (TermRelationship termRelationshipListTermRelationship : termTaxonomy.getTermRelationshipList()) {
                TermTaxonomy oldTermTaxonomyOfTermRelationshipListTermRelationship = termRelationshipListTermRelationship.getTermTaxonomy();
                termRelationshipListTermRelationship.setTermTaxonomy(termTaxonomy);
                termRelationshipListTermRelationship = em.merge(termRelationshipListTermRelationship);
                if (oldTermTaxonomyOfTermRelationshipListTermRelationship != null) {
                    oldTermTaxonomyOfTermRelationshipListTermRelationship.getTermRelationshipList().remove(termRelationshipListTermRelationship);
                    oldTermTaxonomyOfTermRelationshipListTermRelationship = em.merge(oldTermTaxonomyOfTermRelationshipListTermRelationship);
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
    public void edit(TermTaxonomy termTaxonomy) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            TermTaxonomy persistentTermTaxonomy = em.find(TermTaxonomy.class, termTaxonomy.getId());
            Term termIdOld = persistentTermTaxonomy.getTermId();
            Term termIdNew = termTaxonomy.getTermId();
            List<TermRelationship> termRelationshipListOld = persistentTermTaxonomy.getTermRelationshipList();
            List<TermRelationship> termRelationshipListNew = termTaxonomy.getTermRelationshipList();
            List<String> illegalOrphanMessages = null;
            for (TermRelationship termRelationshipListOldTermRelationship : termRelationshipListOld) {
                if (!termRelationshipListNew.contains(termRelationshipListOldTermRelationship)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermRelationship " + termRelationshipListOldTermRelationship + " since its termTaxonomy field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (termIdNew != null) {
                termIdNew = em.getReference(termIdNew.getClass(), termIdNew.getId());
                termTaxonomy.setTermId(termIdNew);
            }
            List<TermRelationship> attachedTermRelationshipListNew = new ArrayList<TermRelationship>();
            for (TermRelationship termRelationshipListNewTermRelationshipToAttach : termRelationshipListNew) {
                termRelationshipListNewTermRelationshipToAttach = em.getReference(termRelationshipListNewTermRelationshipToAttach.getClass(), termRelationshipListNewTermRelationshipToAttach.getTermRelationshipPK());
                attachedTermRelationshipListNew.add(termRelationshipListNewTermRelationshipToAttach);
            }
            termRelationshipListNew = attachedTermRelationshipListNew;
            termTaxonomy.setTermRelationshipList(termRelationshipListNew);
            termTaxonomy = em.merge(termTaxonomy);
            if (termIdOld != null && !termIdOld.equals(termIdNew)) {
                termIdOld.getTermTaxonomyList().remove(termTaxonomy);
                termIdOld = em.merge(termIdOld);
            }
            if (termIdNew != null && !termIdNew.equals(termIdOld)) {
                termIdNew.getTermTaxonomyList().add(termTaxonomy);
                termIdNew = em.merge(termIdNew);
            }
            for (TermRelationship termRelationshipListNewTermRelationship : termRelationshipListNew) {
                if (!termRelationshipListOld.contains(termRelationshipListNewTermRelationship)) {
                    TermTaxonomy oldTermTaxonomyOfTermRelationshipListNewTermRelationship = termRelationshipListNewTermRelationship.getTermTaxonomy();
                    termRelationshipListNewTermRelationship.setTermTaxonomy(termTaxonomy);
                    termRelationshipListNewTermRelationship = em.merge(termRelationshipListNewTermRelationship);
                    if (oldTermTaxonomyOfTermRelationshipListNewTermRelationship != null && !oldTermTaxonomyOfTermRelationshipListNewTermRelationship.equals(termTaxonomy)) {
                        oldTermTaxonomyOfTermRelationshipListNewTermRelationship.getTermRelationshipList().remove(termRelationshipListNewTermRelationship);
                        oldTermTaxonomyOfTermRelationshipListNewTermRelationship = em.merge(oldTermTaxonomyOfTermRelationshipListNewTermRelationship);
                    }
                }
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = termTaxonomy.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The termTaxonomy with id " + id + " no longer exists.");
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
            TermTaxonomy termTaxonomy;
            try {
                termTaxonomy = em.getReference(TermTaxonomy.class, id);
                termTaxonomy.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The termTaxonomy with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<TermRelationship> termRelationshipListOrphanCheck = termTaxonomy.getTermRelationshipList();
            for (TermRelationship termRelationshipListOrphanCheckTermRelationship : termRelationshipListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TermTaxonomy (" + termTaxonomy + ") cannot be destroyed since the TermRelationship " + termRelationshipListOrphanCheckTermRelationship + " in its termRelationshipList field has a non-nullable termTaxonomy field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Term termId = termTaxonomy.getTermId();
            if (termId != null) {
                termId.getTermTaxonomyList().remove(termTaxonomy);
                termId = em.merge(termId);
            }
            em.remove(termTaxonomy);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<TermTaxonomy> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<TermTaxonomy> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<TermTaxonomy> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TermTaxonomy.class));
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
    public TermTaxonomy find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(TermTaxonomy.class, id);
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
            Root<TermTaxonomy> rt = cq.from(TermTaxonomy.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

}
