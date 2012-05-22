/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.CommentDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import com.cherrot.govproject.model.Post;
import java.io.Serializable;
import java.util.ArrayList;
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
public class CommentJpaDao implements Serializable, CommentDao {

    @PersistenceContext
    private EntityManager em;
//    public CommentJpaDao(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;

//    @Override
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    @Override
    @Transactional
    public void create(Comment comment) {
        if (comment.getCommentmetaList() == null) {
            comment.setCommentmetaList(new ArrayList<Commentmeta>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Post postId = comment.getPostId();
            if (postId != null) {
                postId = em.getReference(postId.getClass(), postId.getId());
                comment.setPostId(postId);
            }
            List<Commentmeta> attachedCommentmetaList = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListCommentmetaToAttach : comment.getCommentmetaList()) {
                commentmetaListCommentmetaToAttach = em.getReference(commentmetaListCommentmetaToAttach.getClass(), commentmetaListCommentmetaToAttach.getId());
                attachedCommentmetaList.add(commentmetaListCommentmetaToAttach);
            }
            comment.setCommentmetaList(attachedCommentmetaList);
            em.persist(comment);
            if (postId != null) {
                postId.getCommentList().add(comment);
                postId = em.merge(postId);
            }
            for (Commentmeta commentmetaListCommentmeta : comment.getCommentmetaList()) {
                Comment oldCommentIdOfCommentmetaListCommentmeta = commentmetaListCommentmeta.getCommentId();
                commentmetaListCommentmeta.setCommentId(comment);
                commentmetaListCommentmeta = em.merge(commentmetaListCommentmeta);
                if (oldCommentIdOfCommentmetaListCommentmeta != null) {
                    oldCommentIdOfCommentmetaListCommentmeta.getCommentmetaList().remove(commentmetaListCommentmeta);
                    oldCommentIdOfCommentmetaListCommentmeta = em.merge(oldCommentIdOfCommentmetaListCommentmeta);
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
    public void edit(Comment comment) throws IllegalOrphanException, NonexistentEntityException {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Comment persistentComment = em.find(Comment.class, comment.getId());
            Post postIdOld = persistentComment.getPostId();
            Post postIdNew = comment.getPostId();
            List<Commentmeta> commentmetaListOld = persistentComment.getCommentmetaList();
            List<Commentmeta> commentmetaListNew = comment.getCommentmetaList();
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
                comment.setPostId(postIdNew);
            }
            List<Commentmeta> attachedCommentmetaListNew = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListNewCommentmetaToAttach : commentmetaListNew) {
                commentmetaListNewCommentmetaToAttach = em.getReference(commentmetaListNewCommentmetaToAttach.getClass(), commentmetaListNewCommentmetaToAttach.getId());
                attachedCommentmetaListNew.add(commentmetaListNewCommentmetaToAttach);
            }
            commentmetaListNew = attachedCommentmetaListNew;
            comment.setCommentmetaList(commentmetaListNew);
            comment = em.merge(comment);
            if (postIdOld != null && !postIdOld.equals(postIdNew)) {
                postIdOld.getCommentList().remove(comment);
                postIdOld = em.merge(postIdOld);
            }
            if (postIdNew != null && !postIdNew.equals(postIdOld)) {
                postIdNew.getCommentList().add(comment);
                postIdNew = em.merge(postIdNew);
            }
            for (Commentmeta commentmetaListNewCommentmeta : commentmetaListNew) {
                if (!commentmetaListOld.contains(commentmetaListNewCommentmeta)) {
                    Comment oldCommentIdOfCommentmetaListNewCommentmeta = commentmetaListNewCommentmeta.getCommentId();
                    commentmetaListNewCommentmeta.setCommentId(comment);
                    commentmetaListNewCommentmeta = em.merge(commentmetaListNewCommentmeta);
                    if (oldCommentIdOfCommentmetaListNewCommentmeta != null && !oldCommentIdOfCommentmetaListNewCommentmeta.equals(comment)) {
                        oldCommentIdOfCommentmetaListNewCommentmeta.getCommentmetaList().remove(commentmetaListNewCommentmeta);
                        oldCommentIdOfCommentmetaListNewCommentmeta = em.merge(oldCommentIdOfCommentmetaListNewCommentmeta);
                    }
                }
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comment.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The comment with id " + id + " no longer exists.");
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
            Comment comment;
            try {
                comment = em.getReference(Comment.class, id);
                comment.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Commentmeta> commentmetaListOrphanCheck = comment.getCommentmetaList();
            for (Commentmeta commentmetaListOrphanCheckCommentmeta : commentmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comment (" + comment + ") cannot be destroyed since the Commentmeta " + commentmetaListOrphanCheckCommentmeta + " in its commentmetaList field has a non-nullable commentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Post postId = comment.getPostId();
            if (postId != null) {
                postId.getCommentList().remove(comment);
                postId = em.merge(postId);
            }
            em.remove(comment);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Comment> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Comment> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Comment> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comment.class));
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
    public Comment find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Comment.class, id);
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
            Root<Comment> rt = cq.from(Comment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public void save(Comment model) {
        if (model.getId() == null)
            create(model);
        else
            try {
            edit(model);
        }
        catch (Exception ex) {
            Logger.getLogger(CommentJpaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
