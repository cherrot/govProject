/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Term;
import java.util.ArrayList;
import java.util.List;
import com.cherrot.govproject.model.TermRelationship;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
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
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    @Override
    @Transactional
    public void create(Term term) {
        if (term.getTermList() == null) {
            term.setTermList(new ArrayList<Term>());
        }
        if (term.getTermRelationshipList() == null) {
            term.setTermRelationshipList(new ArrayList<TermRelationship>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term termParent = term.getTermParent();
            if (termParent != null) {
                termParent = em.getReference(termParent.getClass(), termParent.getId());
                term.setTermParent(termParent);
            }
            List<Term> attachedTermList = new ArrayList<Term>();
            for (Term termListTermToAttach : term.getTermList()) {
                termListTermToAttach = em.getReference(termListTermToAttach.getClass(), termListTermToAttach.getId());
                attachedTermList.add(termListTermToAttach);
            }
            term.setTermList(attachedTermList);
            List<TermRelationship> attachedTermRelationshipList = new ArrayList<TermRelationship>();
            for (TermRelationship termRelationshipListTermRelationshipToAttach : term.getTermRelationshipList()) {
                termRelationshipListTermRelationshipToAttach = em.getReference(termRelationshipListTermRelationshipToAttach.getClass(), termRelationshipListTermRelationshipToAttach.getTermRelationshipPK());
                attachedTermRelationshipList.add(termRelationshipListTermRelationshipToAttach);
            }
            term.setTermRelationshipList(attachedTermRelationshipList);
            em.persist(term);
            if (termParent != null) {
                termParent.getTermList().add(term);
                termParent = em.merge(termParent);
            }
            for (Term termListTerm : term.getTermList()) {
                Term oldTermParentOfTermListTerm = termListTerm.getTermParent();
                termListTerm.setTermParent(term);
                termListTerm = em.merge(termListTerm);
                if (oldTermParentOfTermListTerm != null) {
                    oldTermParentOfTermListTerm.getTermList().remove(termListTerm);
                    oldTermParentOfTermListTerm = em.merge(oldTermParentOfTermListTerm);
                }
            }
            for (TermRelationship termRelationshipListTermRelationship : term.getTermRelationshipList()) {
                Term oldTermOfTermRelationshipListTermRelationship = termRelationshipListTermRelationship.getTerm();
                termRelationshipListTermRelationship.setTerm(term);
                termRelationshipListTermRelationship = em.merge(termRelationshipListTermRelationship);
                if (oldTermOfTermRelationshipListTermRelationship != null) {
                    oldTermOfTermRelationshipListTermRelationship.getTermRelationshipList().remove(termRelationshipListTermRelationship);
                    oldTermOfTermRelationshipListTermRelationship = em.merge(oldTermOfTermRelationshipListTermRelationship);
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
    public void edit(Term term) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term persistentTerm = em.find(Term.class, term.getId());
            Term termParentOld = persistentTerm.getTermParent();
            Term termParentNew = term.getTermParent();
            List<Term> termListOld = persistentTerm.getTermList();
            List<Term> termListNew = term.getTermList();
            List<TermRelationship> termRelationshipListOld = persistentTerm.getTermRelationshipList();
            List<TermRelationship> termRelationshipListNew = term.getTermRelationshipList();
            List<String> illegalOrphanMessages = null;
            for (TermRelationship termRelationshipListOldTermRelationship : termRelationshipListOld) {
                if (!termRelationshipListNew.contains(termRelationshipListOldTermRelationship)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermRelationship " + termRelationshipListOldTermRelationship + " since its term field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (termParentNew != null) {
                termParentNew = em.getReference(termParentNew.getClass(), termParentNew.getId());
                term.setTermParent(termParentNew);
            }
            List<Term> attachedTermListNew = new ArrayList<Term>();
            for (Term termListNewTermToAttach : termListNew) {
                termListNewTermToAttach = em.getReference(termListNewTermToAttach.getClass(), termListNewTermToAttach.getId());
                attachedTermListNew.add(termListNewTermToAttach);
            }
            termListNew = attachedTermListNew;
            term.setTermList(termListNew);
            List<TermRelationship> attachedTermRelationshipListNew = new ArrayList<TermRelationship>();
            for (TermRelationship termRelationshipListNewTermRelationshipToAttach : termRelationshipListNew) {
                termRelationshipListNewTermRelationshipToAttach = em.getReference(termRelationshipListNewTermRelationshipToAttach.getClass(), termRelationshipListNewTermRelationshipToAttach.getTermRelationshipPK());
                attachedTermRelationshipListNew.add(termRelationshipListNewTermRelationshipToAttach);
            }
            termRelationshipListNew = attachedTermRelationshipListNew;
            term.setTermRelationshipList(termRelationshipListNew);
            term = em.merge(term);
            if (termParentOld != null && !termParentOld.equals(termParentNew)) {
                termParentOld.getTermList().remove(term);
                termParentOld = em.merge(termParentOld);
            }
            if (termParentNew != null && !termParentNew.equals(termParentOld)) {
                termParentNew.getTermList().add(term);
                termParentNew = em.merge(termParentNew);
            }
            for (Term termListOldTerm : termListOld) {
                if (!termListNew.contains(termListOldTerm)) {
                    termListOldTerm.setTermParent(null);
                    termListOldTerm = em.merge(termListOldTerm);
                }
            }
            for (Term termListNewTerm : termListNew) {
                if (!termListOld.contains(termListNewTerm)) {
                    Term oldTermParentOfTermListNewTerm = termListNewTerm.getTermParent();
                    termListNewTerm.setTermParent(term);
                    termListNewTerm = em.merge(termListNewTerm);
                    if (oldTermParentOfTermListNewTerm != null && !oldTermParentOfTermListNewTerm.equals(term)) {
                        oldTermParentOfTermListNewTerm.getTermList().remove(termListNewTerm);
                        oldTermParentOfTermListNewTerm = em.merge(oldTermParentOfTermListNewTerm);
                    }
                }
            }
            for (TermRelationship termRelationshipListNewTermRelationship : termRelationshipListNew) {
                if (!termRelationshipListOld.contains(termRelationshipListNewTermRelationship)) {
                    Term oldTermOfTermRelationshipListNewTermRelationship = termRelationshipListNewTermRelationship.getTerm();
                    termRelationshipListNewTermRelationship.setTerm(term);
                    termRelationshipListNewTermRelationship = em.merge(termRelationshipListNewTermRelationship);
                    if (oldTermOfTermRelationshipListNewTermRelationship != null && !oldTermOfTermRelationshipListNewTermRelationship.equals(term)) {
                        oldTermOfTermRelationshipListNewTermRelationship.getTermRelationshipList().remove(termRelationshipListNewTermRelationship);
                        oldTermOfTermRelationshipListNewTermRelationship = em.merge(oldTermOfTermRelationshipListNewTermRelationship);
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
            List<TermRelationship> termRelationshipListOrphanCheck = term.getTermRelationshipList();
            for (TermRelationship termRelationshipListOrphanCheckTermRelationship : termRelationshipListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Term (" + term + ") cannot be destroyed since the TermRelationship " + termRelationshipListOrphanCheckTermRelationship + " in its termRelationshipList field has a non-nullable term field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Term termParent = term.getTermParent();
            if (termParent != null) {
                termParent.getTermList().remove(term);
                termParent = em.merge(termParent);
            }
            List<Term> termList = term.getTermList();
            for (Term termListTerm : termList) {
                termListTerm.setTermParent(null);
                termListTerm = em.merge(termListTerm);
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
        return em.createNamedQuery("Term.findByName", Term.class)
                .setParameter("name", name).getResultList();
    }

    @Override
    public Term findByNameAndType(String name, Term.TermType type) {
        return em.createNamedQuery("Term.findByNameAndType", Term.class)
                .setParameter("name", name).setParameter("type", type).getSingleResult();
    }

    @Override
    public Term findBySlug(String slug) {
        return em.createNamedQuery("Term.findBySlug", Term.class)
                .setParameter("slug", slug).getSingleResult();
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
