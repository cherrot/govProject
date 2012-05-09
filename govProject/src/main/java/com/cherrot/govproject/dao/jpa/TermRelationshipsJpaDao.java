/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.TermTaxonomy;
import com.cherrot.govproject.model.Posts;
import com.cherrot.govproject.model.Links;
import com.cherrot.govproject.model.TermRelationships;
import com.cherrot.govproject.model.TermRelationshipsPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class TermRelationshipsJpaDao implements Serializable {

    public TermRelationshipsJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TermRelationships termRelationships) throws PreexistingEntityException, Exception {
        if (termRelationships.getTermRelationshipsPK() == null) {
            termRelationships.setTermRelationshipsPK(new TermRelationshipsPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TermTaxonomy termTaxonomy = termRelationships.getTermTaxonomy();
            if (termTaxonomy != null) {
                termTaxonomy = em.getReference(termTaxonomy.getClass(), termTaxonomy.getId());
                termRelationships.setTermTaxonomy(termTaxonomy);
            }
            Posts posts = termRelationships.getPosts();
            if (posts != null) {
                posts = em.getReference(posts.getClass(), posts.getId());
                termRelationships.setPosts(posts);
            }
            Links links = termRelationships.getLinks();
            if (links != null) {
                links = em.getReference(links.getClass(), links.getId());
                termRelationships.setLinks(links);
            }
            em.persist(termRelationships);
            if (termTaxonomy != null) {
                termTaxonomy.getTermRelationshipsList().add(termRelationships);
                termTaxonomy = em.merge(termTaxonomy);
            }
            if (posts != null) {
                posts.getTermRelationshipsList().add(termRelationships);
                posts = em.merge(posts);
            }
            if (links != null) {
                links.getTermRelationshipsList().add(termRelationships);
                links = em.merge(links);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTermRelationships(termRelationships.getTermRelationshipsPK()) != null) {
                throw new PreexistingEntityException("TermRelationships " + termRelationships + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TermRelationships termRelationships) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TermRelationships persistentTermRelationships = em.find(TermRelationships.class, termRelationships.getTermRelationshipsPK());
            TermTaxonomy termTaxonomyOld = persistentTermRelationships.getTermTaxonomy();
            TermTaxonomy termTaxonomyNew = termRelationships.getTermTaxonomy();
            Posts postsOld = persistentTermRelationships.getPosts();
            Posts postsNew = termRelationships.getPosts();
            Links linksOld = persistentTermRelationships.getLinks();
            Links linksNew = termRelationships.getLinks();
            if (termTaxonomyNew != null) {
                termTaxonomyNew = em.getReference(termTaxonomyNew.getClass(), termTaxonomyNew.getId());
                termRelationships.setTermTaxonomy(termTaxonomyNew);
            }
            if (postsNew != null) {
                postsNew = em.getReference(postsNew.getClass(), postsNew.getId());
                termRelationships.setPosts(postsNew);
            }
            if (linksNew != null) {
                linksNew = em.getReference(linksNew.getClass(), linksNew.getId());
                termRelationships.setLinks(linksNew);
            }
            termRelationships = em.merge(termRelationships);
            if (termTaxonomyOld != null && !termTaxonomyOld.equals(termTaxonomyNew)) {
                termTaxonomyOld.getTermRelationshipsList().remove(termRelationships);
                termTaxonomyOld = em.merge(termTaxonomyOld);
            }
            if (termTaxonomyNew != null && !termTaxonomyNew.equals(termTaxonomyOld)) {
                termTaxonomyNew.getTermRelationshipsList().add(termRelationships);
                termTaxonomyNew = em.merge(termTaxonomyNew);
            }
            if (postsOld != null && !postsOld.equals(postsNew)) {
                postsOld.getTermRelationshipsList().remove(termRelationships);
                postsOld = em.merge(postsOld);
            }
            if (postsNew != null && !postsNew.equals(postsOld)) {
                postsNew.getTermRelationshipsList().add(termRelationships);
                postsNew = em.merge(postsNew);
            }
            if (linksOld != null && !linksOld.equals(linksNew)) {
                linksOld.getTermRelationshipsList().remove(termRelationships);
                linksOld = em.merge(linksOld);
            }
            if (linksNew != null && !linksNew.equals(linksOld)) {
                linksNew.getTermRelationshipsList().add(termRelationships);
                linksNew = em.merge(linksNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TermRelationshipsPK id = termRelationships.getTermRelationshipsPK();
                if (findTermRelationships(id) == null) {
                    throw new NonexistentEntityException("The termRelationships with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TermRelationshipsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TermRelationships termRelationships;
            try {
                termRelationships = em.getReference(TermRelationships.class, id);
                termRelationships.getTermRelationshipsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The termRelationships with id " + id + " no longer exists.", enfe);
            }
            TermTaxonomy termTaxonomy = termRelationships.getTermTaxonomy();
            if (termTaxonomy != null) {
                termTaxonomy.getTermRelationshipsList().remove(termRelationships);
                termTaxonomy = em.merge(termTaxonomy);
            }
            Posts posts = termRelationships.getPosts();
            if (posts != null) {
                posts.getTermRelationshipsList().remove(termRelationships);
                posts = em.merge(posts);
            }
            Links links = termRelationships.getLinks();
            if (links != null) {
                links.getTermRelationshipsList().remove(termRelationships);
                links = em.merge(links);
            }
            em.remove(termRelationships);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TermRelationships> findTermRelationshipsEntities() {
        return findTermRelationshipsEntities(true, -1, -1);
    }

    public List<TermRelationships> findTermRelationshipsEntities(int maxResults, int firstResult) {
        return findTermRelationshipsEntities(false, maxResults, firstResult);
    }

    private List<TermRelationships> findTermRelationshipsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TermRelationships.class));
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

    public TermRelationships findTermRelationships(TermRelationshipsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TermRelationships.class, id);
        } finally {
            em.close();
        }
    }

    public int getTermRelationshipsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TermRelationships> rt = cq.from(TermRelationships.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
