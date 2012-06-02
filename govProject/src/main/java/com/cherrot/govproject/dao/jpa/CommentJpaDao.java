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
public class CommentJpaDao implements CommentDao {

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
        if (comment.getCommentList() == null) {
            comment.setCommentList(new ArrayList<Comment>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Post post = comment.getPost();
            if (post != null) {
                post = em.getReference(post.getClass(), post.getId());
                comment.setPost(post);
            }
            Comment commentParent = comment.getCommentParent();
            if (commentParent != null) {
                commentParent = em.getReference(commentParent.getClass(), commentParent.getId());
                comment.setCommentParent(commentParent);
            }
            List<Commentmeta> attachedCommentmetaList = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListCommentmetaToAttach : comment.getCommentmetaList()) {
                commentmetaListCommentmetaToAttach = em.getReference(commentmetaListCommentmetaToAttach.getClass(), commentmetaListCommentmetaToAttach.getId());
                attachedCommentmetaList.add(commentmetaListCommentmetaToAttach);
            }
            comment.setCommentmetaList(attachedCommentmetaList);
            List<Comment> attachedCommentList = new ArrayList<Comment>();
            for (Comment commentListCommentToAttach : comment.getCommentList()) {
                commentListCommentToAttach = em.getReference(commentListCommentToAttach.getClass(), commentListCommentToAttach.getId());
                attachedCommentList.add(commentListCommentToAttach);
            }
            comment.setCommentList(attachedCommentList);
            em.persist(comment);
            if (post != null) {
                post.getCommentList().add(comment);
                post = em.merge(post);
            }
            if (commentParent != null) {
                commentParent.getCommentList().add(comment);
                commentParent = em.merge(commentParent);
            }
            for (Commentmeta commentmetaListCommentmeta : comment.getCommentmetaList()) {
                Comment oldCommentOfCommentmetaListCommentmeta = commentmetaListCommentmeta.getComment();
                commentmetaListCommentmeta.setComment(comment);
                commentmetaListCommentmeta = em.merge(commentmetaListCommentmeta);
                if (oldCommentOfCommentmetaListCommentmeta != null) {
                    oldCommentOfCommentmetaListCommentmeta.getCommentmetaList().remove(commentmetaListCommentmeta);
                    oldCommentOfCommentmetaListCommentmeta = em.merge(oldCommentOfCommentmetaListCommentmeta);
                }
            }
            for (Comment commentListComment : comment.getCommentList()) {
                Comment oldCommentParentOfCommentListComment = commentListComment.getCommentParent();
                commentListComment.setCommentParent(comment);
                commentListComment = em.merge(commentListComment);
                if (oldCommentParentOfCommentListComment != null) {
                    oldCommentParentOfCommentListComment.getCommentList().remove(commentListComment);
                    oldCommentParentOfCommentListComment = em.merge(oldCommentParentOfCommentListComment);
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
    public void edit(Comment comment) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Comment persistentComment = em.find(Comment.class, comment.getId());
            Post postOld = persistentComment.getPost();
            Post postNew = comment.getPost();
            Comment commentParentOld = persistentComment.getCommentParent();
            Comment commentParentNew = comment.getCommentParent();
            List<Commentmeta> commentmetaListOld = persistentComment.getCommentmetaList();
            List<Commentmeta> commentmetaListNew = comment.getCommentmetaList();
            List<Comment> commentListOld = persistentComment.getCommentList();
            List<Comment> commentListNew = comment.getCommentList();
            List<String> illegalOrphanMessages = null;
            for (Commentmeta commentmetaListOldCommentmeta : commentmetaListOld) {
                if (!commentmetaListNew.contains(commentmetaListOldCommentmeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Commentmeta " + commentmetaListOldCommentmeta + " since its comment field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (postNew != null) {
                postNew = em.getReference(postNew.getClass(), postNew.getId());
                comment.setPost(postNew);
            }
            if (commentParentNew != null) {
                commentParentNew = em.getReference(commentParentNew.getClass(), commentParentNew.getId());
                comment.setCommentParent(commentParentNew);
            }
            List<Commentmeta> attachedCommentmetaListNew = new ArrayList<Commentmeta>();
            for (Commentmeta commentmetaListNewCommentmetaToAttach : commentmetaListNew) {
                commentmetaListNewCommentmetaToAttach = em.getReference(commentmetaListNewCommentmetaToAttach.getClass(), commentmetaListNewCommentmetaToAttach.getId());
                attachedCommentmetaListNew.add(commentmetaListNewCommentmetaToAttach);
            }
            commentmetaListNew = attachedCommentmetaListNew;
            comment.setCommentmetaList(commentmetaListNew);
            List<Comment> attachedCommentListNew = new ArrayList<Comment>();
            for (Comment commentListNewCommentToAttach : commentListNew) {
                commentListNewCommentToAttach = em.getReference(commentListNewCommentToAttach.getClass(), commentListNewCommentToAttach.getId());
                attachedCommentListNew.add(commentListNewCommentToAttach);
            }
            commentListNew = attachedCommentListNew;
            comment.setCommentList(commentListNew);
            comment = em.merge(comment);
            if (postOld != null && !postOld.equals(postNew)) {
                postOld.getCommentList().remove(comment);
                postOld = em.merge(postOld);
            }
            if (postNew != null && !postNew.equals(postOld)) {
                postNew.getCommentList().add(comment);
                postNew = em.merge(postNew);
            }
            if (commentParentOld != null && !commentParentOld.equals(commentParentNew)) {
                commentParentOld.getCommentList().remove(comment);
                commentParentOld = em.merge(commentParentOld);
            }
            if (commentParentNew != null && !commentParentNew.equals(commentParentOld)) {
                commentParentNew.getCommentList().add(comment);
                commentParentNew = em.merge(commentParentNew);
            }
            for (Commentmeta commentmetaListNewCommentmeta : commentmetaListNew) {
                if (!commentmetaListOld.contains(commentmetaListNewCommentmeta)) {
                    Comment oldCommentOfCommentmetaListNewCommentmeta = commentmetaListNewCommentmeta.getComment();
                    commentmetaListNewCommentmeta.setComment(comment);
                    commentmetaListNewCommentmeta = em.merge(commentmetaListNewCommentmeta);
                    if (oldCommentOfCommentmetaListNewCommentmeta != null && !oldCommentOfCommentmetaListNewCommentmeta.equals(comment)) {
                        oldCommentOfCommentmetaListNewCommentmeta.getCommentmetaList().remove(commentmetaListNewCommentmeta);
                        oldCommentOfCommentmetaListNewCommentmeta = em.merge(oldCommentOfCommentmetaListNewCommentmeta);
                    }
                }
            }
            for (Comment commentListOldComment : commentListOld) {
                if (!commentListNew.contains(commentListOldComment)) {
                    commentListOldComment.setCommentParent(null);
                    commentListOldComment = em.merge(commentListOldComment);
                }
            }
            for (Comment commentListNewComment : commentListNew) {
                if (!commentListOld.contains(commentListNewComment)) {
                    Comment oldCommentParentOfCommentListNewComment = commentListNewComment.getCommentParent();
                    commentListNewComment.setCommentParent(comment);
                    commentListNewComment = em.merge(commentListNewComment);
                    if (oldCommentParentOfCommentListNewComment != null && !oldCommentParentOfCommentListNewComment.equals(comment)) {
                        oldCommentParentOfCommentListNewComment.getCommentList().remove(commentListNewComment);
                        oldCommentParentOfCommentListNewComment = em.merge(oldCommentParentOfCommentListNewComment);
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
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Commentmeta> commentmetaListOrphanCheck = comment.getCommentmetaList();
            for (Commentmeta commentmetaListOrphanCheckCommentmeta : commentmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comment (" + comment + ") cannot be destroyed since the Commentmeta " + commentmetaListOrphanCheckCommentmeta + " in its commentmetaList field has a non-nullable comment field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Post post = comment.getPost();
            if (post != null) {
                post.getCommentList().remove(comment);
                post = em.merge(post);
            }
            Comment commentParent = comment.getCommentParent();
            if (commentParent != null) {
                commentParent.getCommentList().remove(comment);
                commentParent = em.merge(commentParent);
            }
            List<Comment> commentList = comment.getCommentList();
            for (Comment commentListComment : commentList) {
                commentListComment.setCommentParent(null);
                commentListComment = em.merge(commentListComment);
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
