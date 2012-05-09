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
import com.cherrot.govproject.model.Posts;
import com.cherrot.govproject.model.Commentmeta;
import com.cherrot.govproject.model.Comments;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class CommentsJpaDao implements Serializable {

    public CommentsJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comments comments) {
        if (comments.getCommentmetaList() == null) {
            comments.setCommentmetaList(new ArrayList<Commentmeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posts postId = comments.getPostId();
            if (postId != null) {
                postId = em.getReference(postId.getClass(), postId.getId());
                comments.setPostId(postId);
            }
            List<Commentmeta> attachedCommentmetaList = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListCommentmetaToAttach : comments.getCommentmetaList()) {
                commentmetaListCommentmetaToAttach = em.getReference(commentmetaListCommentmetaToAttach.getClass(), commentmetaListCommentmetaToAttach.getId());
                attachedCommentmetaList.add(commentmetaListCommentmetaToAttach);
            }
            comments.setCommentmetaList(attachedCommentmetaList);
            em.persist(comments);
            if (postId != null) {
                postId.getCommentsList().add(comments);
                postId = em.merge(postId);
            }
            for (Commentmeta commentmetaListCommentmeta : comments.getCommentmetaList()) {
                Comments oldCommentIdOfCommentmetaListCommentmeta = commentmetaListCommentmeta.getCommentId();
                commentmetaListCommentmeta.setCommentId(comments);
                commentmetaListCommentmeta = em.merge(commentmetaListCommentmeta);
                if (oldCommentIdOfCommentmetaListCommentmeta != null) {
                    oldCommentIdOfCommentmetaListCommentmeta.getCommentmetaList().remove(commentmetaListCommentmeta);
                    oldCommentIdOfCommentmetaListCommentmeta = em.merge(oldCommentIdOfCommentmetaListCommentmeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comments comments) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments persistentComments = em.find(Comments.class, comments.getId());
            Posts postIdOld = persistentComments.getPostId();
            Posts postIdNew = comments.getPostId();
            List<Commentmeta> commentmetaListOld = persistentComments.getCommentmetaList();
            List<Commentmeta> commentmetaListNew = comments.getCommentmetaList();
            List<String> illegalOrphanMessages = null;
            for (Commentmeta commentmetaListOldCommentmeta : commentmetaListOld) {
                if (!commentmetaListNew.contains(commentmetaListOldCommentmeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Commentmeta " + commentmetaListOldCommentmeta + " since its commentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (postIdNew != null) {
                postIdNew = em.getReference(postIdNew.getClass(), postIdNew.getId());
                comments.setPostId(postIdNew);
            }
            List<Commentmeta> attachedCommentmetaListNew = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListNewCommentmetaToAttach : commentmetaListNew) {
                commentmetaListNewCommentmetaToAttach = em.getReference(commentmetaListNewCommentmetaToAttach.getClass(), commentmetaListNewCommentmetaToAttach.getId());
                attachedCommentmetaListNew.add(commentmetaListNewCommentmetaToAttach);
            }
            commentmetaListNew = attachedCommentmetaListNew;
            comments.setCommentmetaList(commentmetaListNew);
            comments = em.merge(comments);
            if (postIdOld != null && !postIdOld.equals(postIdNew)) {
                postIdOld.getCommentsList().remove(comments);
                postIdOld = em.merge(postIdOld);
            }
            if (postIdNew != null && !postIdNew.equals(postIdOld)) {
                postIdNew.getCommentsList().add(comments);
                postIdNew = em.merge(postIdNew);
            }
            for (Commentmeta commentmetaListNewCommentmeta : commentmetaListNew) {
                if (!commentmetaListOld.contains(commentmetaListNewCommentmeta)) {
                    Comments oldCommentIdOfCommentmetaListNewCommentmeta = commentmetaListNewCommentmeta.getCommentId();
                    commentmetaListNewCommentmeta.setCommentId(comments);
                    commentmetaListNewCommentmeta = em.merge(commentmetaListNewCommentmeta);
                    if (oldCommentIdOfCommentmetaListNewCommentmeta != null && !oldCommentIdOfCommentmetaListNewCommentmeta.equals(comments)) {
                        oldCommentIdOfCommentmetaListNewCommentmeta.getCommentmetaList().remove(commentmetaListNewCommentmeta);
                        oldCommentIdOfCommentmetaListNewCommentmeta = em.merge(oldCommentIdOfCommentmetaListNewCommentmeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comments.getId();
                if (findComments(id) == null) {
                    throw new NonexistentEntityException("The comments with id " + id + " no longer exists.");
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
            Comments comments;
            try {
                comments = em.getReference(Comments.class, id);
                comments.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comments with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Commentmeta> commentmetaListOrphanCheck = comments.getCommentmetaList();
            for (Commentmeta commentmetaListOrphanCheckCommentmeta : commentmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comments (" + comments + ") cannot be destroyed since the Commentmeta " + commentmetaListOrphanCheckCommentmeta + " in its commentmetaList field has a non-nullable commentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Posts postId = comments.getPostId();
            if (postId != null) {
                postId.getCommentsList().remove(comments);
                postId = em.merge(postId);
            }
            em.remove(comments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comments> findCommentsEntities() {
        return findCommentsEntities(true, -1, -1);
    }

    public List<Comments> findCommentsEntities(int maxResults, int firstResult) {
        return findCommentsEntities(false, maxResults, firstResult);
    }

    private List<Comments> findCommentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comments.class));
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

    public Comments findComments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comments.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comments> rt = cq.from(Comments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
