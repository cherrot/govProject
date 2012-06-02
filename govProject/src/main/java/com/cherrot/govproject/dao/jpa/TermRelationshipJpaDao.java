/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermRelationshipDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.dao.exceptions.PreexistingEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermRelationship;
import com.cherrot.govproject.model.TermRelationshipPK;
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
public class TermRelationshipJpaDao implements TermRelationshipDao {

    @PersistenceContext
    private EntityManager em;
//    public TermRelationshipJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(TermRelationship termRelationship) {
        if (termRelationship.getTermRelationshipPK() == null) {
            termRelationship.setTermRelationshipPK(new TermRelationshipPK());
        }
        termRelationship.getTermRelationshipPK().setPostId(termRelationship.getPost().getId());
        termRelationship.getTermRelationshipPK().setTermId(termRelationship.getTerm().getId());
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Term term = termRelationship.getTerm();
            if (term != null) {
                term = em.getReference(term.getClass(), term.getId());
                termRelationship.setTerm(term);
            }
            Post post = termRelationship.getPost();
            if (post != null) {
                post = em.getReference(post.getClass(), post.getId());
                termRelationship.setPost(post);
            }
            em.persist(termRelationship);
            if (term != null) {
                term.getTermRelationshipList().add(termRelationship);
                term = em.merge(term);
            }
            if (post != null) {
                post.getTermRelationshipList().add(termRelationship);
                post = em.merge(post);
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
            if (find(termRelationship.getTermRelationshipPK()) != null) {
                Logger.getLogger(CommentmetaJpaDao.class.getName()).log(Level.SEVERE, "TermRelationship " + termRelationship + " already exists.", ex);
//                throw new PreexistingEntityException("TermRelationship " + termRelationship + " already exists.", ex);
            }

        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    @Transactional
    public void edit(TermRelationship termRelationship) throws NonexistentEntityException, Exception {
        termRelationship.getTermRelationshipPK().setPostId(termRelationship.getPost().getId());
        termRelationship.getTermRelationshipPK().setTermId(termRelationship.getTerm().getId());
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            TermRelationship persistentTermRelationship = em.find(TermRelationship.class, termRelationship.getTermRelationshipPK());
            Term termOld = persistentTermRelationship.getTerm();
            Term termNew = termRelationship.getTerm();
            Post postOld = persistentTermRelationship.getPost();
            Post postNew = termRelationship.getPost();
            if (termNew != null) {
                termNew = em.getReference(termNew.getClass(), termNew.getId());
                termRelationship.setTerm(termNew);
            }
            if (postNew != null) {
                postNew = em.getReference(postNew.getClass(), postNew.getId());
                termRelationship.setPost(postNew);
            }
            termRelationship = em.merge(termRelationship);
            if (termOld != null && !termOld.equals(termNew)) {
                termOld.getTermRelationshipList().remove(termRelationship);
                termOld = em.merge(termOld);
            }
            if (termNew != null && !termNew.equals(termOld)) {
                termNew.getTermRelationshipList().add(termRelationship);
                termNew = em.merge(termNew);
            }
            if (postOld != null && !postOld.equals(postNew)) {
                postOld.getTermRelationshipList().remove(termRelationship);
                postOld = em.merge(postOld);
            }
            if (postNew != null && !postNew.equals(postOld)) {
                postNew.getTermRelationshipList().add(termRelationship);
                postNew = em.merge(postNew);
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TermRelationshipPK id = termRelationship.getTermRelationshipPK();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The termRelationship with id " + id + " no longer exists.");
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
    public void destroy(TermRelationshipPK id) throws NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            TermRelationship termRelationship;
            try {
                termRelationship = em.getReference(TermRelationship.class, id);
                termRelationship.getTermRelationshipPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The termRelationship with id " + id + " no longer exists.", enfe);
            }
            Term term = termRelationship.getTerm();
            if (term != null) {
                term.getTermRelationshipList().remove(termRelationship);
                term = em.merge(term);
            }
            Post post = termRelationship.getPost();
            if (post != null) {
                post.getTermRelationshipList().remove(termRelationship);
                post = em.merge(post);
            }
            em.remove(termRelationship);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<TermRelationship> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<TermRelationship> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<TermRelationship> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TermRelationship.class));
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
    public TermRelationship find(TermRelationshipPK id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(TermRelationship.class, id);
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
            Root<TermRelationship> rt = cq.from(TermRelationship.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public void save(TermRelationship model) {
        if (model.getTermRelationshipPK() == null) {
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
