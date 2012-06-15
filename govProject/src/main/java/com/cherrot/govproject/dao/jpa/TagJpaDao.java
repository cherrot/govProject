/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.jpa.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
public class TagJpaDao implements Serializable {

    public TagJpaDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tag tag) {
        if (tag.getPostList() == null) {
            tag.setPostList(new ArrayList<Post>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Post> attachedPostList = new ArrayList<Post>();
            for (Post postListPostToAttach : tag.getPostList()) {
                postListPostToAttach = em.getReference(postListPostToAttach.getClass(), postListPostToAttach.getId());
                attachedPostList.add(postListPostToAttach);
            }
            tag.setPostList(attachedPostList);
            em.persist(tag);
            for (Post postListPost : tag.getPostList()) {
                postListPost.getTagList().add(tag);
                postListPost = em.merge(postListPost);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tag tag) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tag persistentTag = em.find(Tag.class, tag.getId());
            List<Post> postListOld = persistentTag.getPostList();
            List<Post> postListNew = tag.getPostList();
            List<Post> attachedPostListNew = new ArrayList<Post>();
            for (Post postListNewPostToAttach : postListNew) {
                postListNewPostToAttach = em.getReference(postListNewPostToAttach.getClass(), postListNewPostToAttach.getId());
                attachedPostListNew.add(postListNewPostToAttach);
            }
            postListNew = attachedPostListNew;
            tag.setPostList(postListNew);
            tag = em.merge(tag);
            for (Post postListOldPost : postListOld) {
                if (!postListNew.contains(postListOldPost)) {
                    postListOldPost.getTagList().remove(tag);
                    postListOldPost = em.merge(postListOldPost);
                }
            }
            for (Post postListNewPost : postListNew) {
                if (!postListOld.contains(postListNewPost)) {
                    postListNewPost.getTagList().add(tag);
                    postListNewPost = em.merge(postListNewPost);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tag.getId();
                if (findTag(id) == null) {
                    throw new NonexistentEntityException("The tag with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tag tag;
            try {
                tag = em.getReference(Tag.class, id);
                tag.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tag with id " + id + " no longer exists.", enfe);
            }
            List<Post> postList = tag.getPostList();
            for (Post postListPost : postList) {
                postListPost.getTagList().remove(tag);
                postListPost = em.merge(postListPost);
            }
            em.remove(tag);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tag> findTagEntities() {
        return findTagEntities(true, -1, -1);
    }

    public List<Tag> findTagEntities(int maxResults, int firstResult) {
        return findTagEntities(false, maxResults, firstResult);
    }

    private List<Tag> findTagEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tag.class));
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

    public Tag findTag(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tag.class, id);
        } finally {
            em.close();
        }
    }

    public int getTagCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tag> rt = cq.from(Tag.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
