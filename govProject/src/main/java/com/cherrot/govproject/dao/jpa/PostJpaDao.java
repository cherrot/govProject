/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.User;
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
public class PostJpaDao implements Serializable, PostDao {

    @PersistenceContext
    private EntityManager em;
//    public PostJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Post post) {
        if (post.getPostmetaList() == null) {
            post.setPostmetaList(new ArrayList<Postmeta>());
        }
        if (post.getCommentList() == null) {
            post.setCommentList(new ArrayList<Comment>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            User user = post.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getId());
                post.setUser(user);
            }
            List<Postmeta> attachedPostmetaList = new ArrayList<Postmeta>();
            for (Postmeta postmetaListPostmetaToAttach : post.getPostmetaList()) {
                postmetaListPostmetaToAttach = em.getReference(postmetaListPostmetaToAttach.getClass(), postmetaListPostmetaToAttach.getId());
                attachedPostmetaList.add(postmetaListPostmetaToAttach);
            }
            post.setPostmetaList(attachedPostmetaList);
            List<Comment> attachedCommentList = new ArrayList<Comment>();
            for (Comment commentListCommentToAttach : post.getCommentList()) {
                commentListCommentToAttach = em.getReference(commentListCommentToAttach.getClass(), commentListCommentToAttach.getId());
                attachedCommentList.add(commentListCommentToAttach);
            }
            post.setCommentList(attachedCommentList);
            em.persist(post);
            if (user != null) {
                user.getPostList().add(post);
                user = em.merge(user);
            }
            for (Postmeta postmetaListPostmeta : post.getPostmetaList()) {
                Post oldPostIdOfPostmetaListPostmeta = postmetaListPostmeta.getPost();
                postmetaListPostmeta.setPost(post);
                postmetaListPostmeta = em.merge(postmetaListPostmeta);
                if (oldPostIdOfPostmetaListPostmeta != null) {
                    oldPostIdOfPostmetaListPostmeta.getPostmetaList().remove(postmetaListPostmeta);
                    oldPostIdOfPostmetaListPostmeta = em.merge(oldPostIdOfPostmetaListPostmeta);
                }
            }
            for (Comment commentListComment : post.getCommentList()) {
                Post oldPostIdOfCommentListComment = commentListComment.getPost();
                commentListComment.setPost(post);
                commentListComment = em.merge(commentListComment);
                if (oldPostIdOfCommentListComment != null) {
                    oldPostIdOfCommentListComment.getCommentList().remove(commentListComment);
                    oldPostIdOfCommentListComment = em.merge(oldPostIdOfCommentListComment);
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
    public void edit(Post post) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Post persistentPost = em.find(Post.class, post.getId());
            User userIdOld = persistentPost.getUser();
            User userIdNew = post.getUser();
            List<Postmeta> postmetaListOld = persistentPost.getPostmetaList();
            List<Postmeta> postmetaListNew = post.getPostmetaList();
            List<Comment> commentListOld = persistentPost.getCommentList();
            List<Comment> commentListNew = post.getCommentList();
            List<String> illegalOrphanMessages = null;
            for (Postmeta postmetaListOldPostmeta : postmetaListOld) {
                if (!postmetaListNew.contains(postmetaListOldPostmeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Postmeta " + postmetaListOldPostmeta + " since its postId field is not nullable.");
                }
            }
            for (Comment commentListOldComment : commentListOld) {
                if (!commentListNew.contains(commentListOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentListOldComment + " since its postId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                post.setUser(userIdNew);
            }
            List<Postmeta> attachedPostmetaListNew = new ArrayList<Postmeta>();
            for (Postmeta postmetaListNewPostmetaToAttach : postmetaListNew) {
                postmetaListNewPostmetaToAttach = em.getReference(postmetaListNewPostmetaToAttach.getClass(), postmetaListNewPostmetaToAttach.getId());
                attachedPostmetaListNew.add(postmetaListNewPostmetaToAttach);
            }
            postmetaListNew = attachedPostmetaListNew;
            post.setPostmetaList(postmetaListNew);
            List<Comment> attachedCommentListNew = new ArrayList<Comment>();
            for (Comment commentListNewCommentToAttach : commentListNew) {
                commentListNewCommentToAttach = em.getReference(commentListNewCommentToAttach.getClass(), commentListNewCommentToAttach.getId());
                attachedCommentListNew.add(commentListNewCommentToAttach);
            }
            commentListNew = attachedCommentListNew;
            post.setCommentList(commentListNew);
            post = em.merge(post);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPostList().remove(post);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPostList().add(post);
                userIdNew = em.merge(userIdNew);
            }
            for (Postmeta postmetaListNewPostmeta : postmetaListNew) {
                if (!postmetaListOld.contains(postmetaListNewPostmeta)) {
                    Post oldPostIdOfPostmetaListNewPostmeta = postmetaListNewPostmeta.getPost();
                    postmetaListNewPostmeta.setPost(post);
                    postmetaListNewPostmeta = em.merge(postmetaListNewPostmeta);
                    if (oldPostIdOfPostmetaListNewPostmeta != null && !oldPostIdOfPostmetaListNewPostmeta.equals(post)) {
                        oldPostIdOfPostmetaListNewPostmeta.getPostmetaList().remove(postmetaListNewPostmeta);
                        oldPostIdOfPostmetaListNewPostmeta = em.merge(oldPostIdOfPostmetaListNewPostmeta);
                    }
                }
            }
            for (Comment commentListNewComment : commentListNew) {
                if (!commentListOld.contains(commentListNewComment)) {
                    Post oldPostIdOfCommentListNewComment = commentListNewComment.getPost();
                    commentListNewComment.setPost(post);
                    commentListNewComment = em.merge(commentListNewComment);
                    if (oldPostIdOfCommentListNewComment != null && !oldPostIdOfCommentListNewComment.equals(post)) {
                        oldPostIdOfCommentListNewComment.getCommentList().remove(commentListNewComment);
                        oldPostIdOfCommentListNewComment = em.merge(oldPostIdOfCommentListNewComment);
                    }
                }
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = post.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The post with id " + id + " no longer exists.");
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
            Post post;
            try {
                post = em.getReference(Post.class, id);
                post.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The post with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Postmeta> postmetaListOrphanCheck = post.getPostmetaList();
            for (Postmeta postmetaListOrphanCheckPostmeta : postmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Post (" + post + ") cannot be destroyed since the Postmeta " + postmetaListOrphanCheckPostmeta + " in its postmetaList field has a non-nullable postId field.");
            }
            List<Comment> commentListOrphanCheck = post.getCommentList();
            for (Comment commentListOrphanCheckComment : commentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Post (" + post + ") cannot be destroyed since the Comment " + commentListOrphanCheckComment + " in its commentList field has a non-nullable postId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User user = post.getUser();
            if (user != null) {
                user.getPostList().remove(post);
                user = em.merge(user);
            }
            em.remove(post);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<Post> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Post> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Post> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Post.class));
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
    public Post find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Post.class, id);
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
            Root<Post> rt = cq.from(Post.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public void save(Post model) {
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
