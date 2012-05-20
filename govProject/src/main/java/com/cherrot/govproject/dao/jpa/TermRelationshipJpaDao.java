/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermRelationshipDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.dao.exceptions.PreexistingEntityException;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.TermRelationship;
import com.cherrot.govproject.model.TermRelationshipPK;
import com.cherrot.govproject.model.TermTaxonomy;
import java.io.Serializable;
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
public class TermRelationshipJpaDao implements Serializable, TermRelationshipDao {

    @PersistenceContext
    private EntityManager em;
//    public TermRelationshipJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(TermRelationship termRelationship) throws PreexistingEntityException{
        if (termRelationship.getTermRelationshipPK() == null) {
            termRelationship.setTermRelationshipPK(new TermRelationshipPK());
        }
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            TermTaxonomy termTaxonomy = termRelationship.getTermTaxonomy();
            if (termTaxonomy != null) {
                termTaxonomy = em.getReference(termTaxonomy.getClass(), termTaxonomy.getId());
                termRelationship.setTermTaxonomy(termTaxonomy);
            }
            Post post = termRelationship.getPost();
            if (post != null) {
                post = em.getReference(post.getClass(), post.getId());
                termRelationship.setPost(post);
            }
            Link link = termRelationship.getLink();
            if (link != null) {
                link = em.getReference(link.getClass(), link.getId());
                termRelationship.setLink(link);
            }
            em.persist(termRelationship);
            if (termTaxonomy != null) {
                termTaxonomy.getTermRelationshipList().add(termRelationship);
                termTaxonomy = em.merge(termTaxonomy);
            }
            if (post != null) {
                post.getTermRelationshipList().add(termRelationship);
                post = em.merge(post);
            }
            if (link != null) {
                link.getTermRelationshipList().add(termRelationship);
                link = em.merge(link);
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            if (find(termRelationship.getTermRelationshipPK()) != null) {
                throw new PreexistingEntityException("TermRelationship " + termRelationship + " already exists.", ex);
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
    public void edit(TermRelationship termRelationship) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            TermRelationship persistentTermRelationship = em.find(TermRelationship.class, termRelationship.getTermRelationshipPK());
            TermTaxonomy termTaxonomyOld = persistentTermRelationship.getTermTaxonomy();
            TermTaxonomy termTaxonomyNew = termRelationship.getTermTaxonomy();
            Post postOld = persistentTermRelationship.getPost();
            Post postNew = termRelationship.getPost();
            Link linkOld = persistentTermRelationship.getLink();
            Link linkNew = termRelationship.getLink();
            if (termTaxonomyNew != null) {
                termTaxonomyNew = em.getReference(termTaxonomyNew.getClass(), termTaxonomyNew.getId());
                termRelationship.setTermTaxonomy(termTaxonomyNew);
            }
            if (postNew != null) {
                postNew = em.getReference(postNew.getClass(), postNew.getId());
                termRelationship.setPost(postNew);
            }
            if (linkNew != null) {
                linkNew = em.getReference(linkNew.getClass(), linkNew.getId());
                termRelationship.setLink(linkNew);
            }
            termRelationship = em.merge(termRelationship);
            if (termTaxonomyOld != null && !termTaxonomyOld.equals(termTaxonomyNew)) {
                termTaxonomyOld.getTermRelationshipList().remove(termRelationship);
                termTaxonomyOld = em.merge(termTaxonomyOld);
            }
            if (termTaxonomyNew != null && !termTaxonomyNew.equals(termTaxonomyOld)) {
                termTaxonomyNew.getTermRelationshipList().add(termRelationship);
                termTaxonomyNew = em.merge(termTaxonomyNew);
            }
            if (postOld != null && !postOld.equals(postNew)) {
                postOld.getTermRelationshipList().remove(termRelationship);
                postOld = em.merge(postOld);
            }
            if (postNew != null && !postNew.equals(postOld)) {
                postNew.getTermRelationshipList().add(termRelationship);
                postNew = em.merge(postNew);
            }
            if (linkOld != null && !linkOld.equals(linkNew)) {
                linkOld.getTermRelationshipList().remove(termRelationship);
                linkOld = em.merge(linkOld);
            }
            if (linkNew != null && !linkNew.equals(linkOld)) {
                linkNew.getTermRelationshipList().add(termRelationship);
                linkNew = em.merge(linkNew);
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
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
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The termRelationship with id " + id + " no longer exists.", enfe);
            }
            TermTaxonomy termTaxonomy = termRelationship.getTermTaxonomy();
            if (termTaxonomy != null) {
                termTaxonomy.getTermRelationshipList().remove(termRelationship);
                termTaxonomy = em.merge(termTaxonomy);
            }
            Post post = termRelationship.getPost();
            if (post != null) {
                post.getTermRelationshipList().remove(termRelationship);
                post = em.merge(post);
            }
            Link link = termRelationship.getLink();
            if (link != null) {
                link.getTermRelationshipList().remove(termRelationship);
                link = em.merge(link);
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

}
