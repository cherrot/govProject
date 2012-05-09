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
import com.cherrot.govproject.model.Users;
import com.cherrot.govproject.model.Postmeta;
import java.util.ArrayList;
import java.util.List;
import com.cherrot.govproject.model.TermRelationships;
import com.cherrot.govproject.model.Comments;
import com.cherrot.govproject.model.Posts;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class PostsJpaDao implements Serializable {

    public PostsJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Posts posts) {
        if (posts.getPostmetaList() == null) {
            posts.setPostmetaList(new ArrayList<Postmeta>());
        }
        if (posts.getTermRelationshipsList() == null) {
            posts.setTermRelationshipsList(new ArrayList<TermRelationships>());
        }
        if (posts.getCommentsList() == null) {
            posts.setCommentsList(new ArrayList<Comments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = posts.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                posts.setUserId(userId);
            }
            List<Postmeta> attachedPostmetaList = new ArrayList<Postmeta>();
            for (Postmeta postmetaListPostmetaToAttach : posts.getPostmetaList()) {
                postmetaListPostmetaToAttach = em.getReference(postmetaListPostmetaToAttach.getClass(), postmetaListPostmetaToAttach.getId());
                attachedPostmetaList.add(postmetaListPostmetaToAttach);
            }
            posts.setPostmetaList(attachedPostmetaList);
            List<TermRelationships> attachedTermRelationshipsList = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListTermRelationshipsToAttach : posts.getTermRelationshipsList()) {
                termRelationshipsListTermRelationshipsToAttach = em.getReference(termRelationshipsListTermRelationshipsToAttach.getClass(), termRelationshipsListTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsList.add(termRelationshipsListTermRelationshipsToAttach);
            }
            posts.setTermRelationshipsList(attachedTermRelationshipsList);
            List<Comments> attachedCommentsList = new ArrayList<Comments>();
            for (Comments commentsListCommentsToAttach : posts.getCommentsList()) {
                commentsListCommentsToAttach = em.getReference(commentsListCommentsToAttach.getClass(), commentsListCommentsToAttach.getId());
                attachedCommentsList.add(commentsListCommentsToAttach);
            }
            posts.setCommentsList(attachedCommentsList);
            em.persist(posts);
            if (userId != null) {
                userId.getPostsList().add(posts);
                userId = em.merge(userId);
            }
            for (Postmeta postmetaListPostmeta : posts.getPostmetaList()) {
                Posts oldPostIdOfPostmetaListPostmeta = postmetaListPostmeta.getPostId();
                postmetaListPostmeta.setPostId(posts);
                postmetaListPostmeta = em.merge(postmetaListPostmeta);
                if (oldPostIdOfPostmetaListPostmeta != null) {
                    oldPostIdOfPostmetaListPostmeta.getPostmetaList().remove(postmetaListPostmeta);
                    oldPostIdOfPostmetaListPostmeta = em.merge(oldPostIdOfPostmetaListPostmeta);
                }
            }
            for (TermRelationships termRelationshipsListTermRelationships : posts.getTermRelationshipsList()) {
                Posts oldPostsOfTermRelationshipsListTermRelationships = termRelationshipsListTermRelationships.getPosts();
                termRelationshipsListTermRelationships.setPosts(posts);
                termRelationshipsListTermRelationships = em.merge(termRelationshipsListTermRelationships);
                if (oldPostsOfTermRelationshipsListTermRelationships != null) {
                    oldPostsOfTermRelationshipsListTermRelationships.getTermRelationshipsList().remove(termRelationshipsListTermRelationships);
                    oldPostsOfTermRelationshipsListTermRelationships = em.merge(oldPostsOfTermRelationshipsListTermRelationships);
                }
            }
            for (Comments commentsListComments : posts.getCommentsList()) {
                Posts oldPostIdOfCommentsListComments = commentsListComments.getPostId();
                commentsListComments.setPostId(posts);
                commentsListComments = em.merge(commentsListComments);
                if (oldPostIdOfCommentsListComments != null) {
                    oldPostIdOfCommentsListComments.getCommentsList().remove(commentsListComments);
                    oldPostIdOfCommentsListComments = em.merge(oldPostIdOfCommentsListComments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Posts posts) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posts persistentPosts = em.find(Posts.class, posts.getId());
            Users userIdOld = persistentPosts.getUserId();
            Users userIdNew = posts.getUserId();
            List<Postmeta> postmetaListOld = persistentPosts.getPostmetaList();
            List<Postmeta> postmetaListNew = posts.getPostmetaList();
            List<TermRelationships> termRelationshipsListOld = persistentPosts.getTermRelationshipsList();
            List<TermRelationships> termRelationshipsListNew = posts.getTermRelationshipsList();
            List<Comments> commentsListOld = persistentPosts.getCommentsList();
            List<Comments> commentsListNew = posts.getCommentsList();
            List<String> illegalOrphanMessages = null;
            for (Postmeta postmetaListOldPostmeta : postmetaListOld) {
                if (!postmetaListNew.contains(postmetaListOldPostmeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Postmeta " + postmetaListOldPostmeta + " since its postId field is not nullable.");
                }
            }
            for (TermRelationships termRelationshipsListOldTermRelationships : termRelationshipsListOld) {
                if (!termRelationshipsListNew.contains(termRelationshipsListOldTermRelationships)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TermRelationships " + termRelationshipsListOldTermRelationships + " since its posts field is not nullable.");
                }
            }
            for (Comments commentsListOldComments : commentsListOld) {
                if (!commentsListNew.contains(commentsListOldComments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comments " + commentsListOldComments + " since its postId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                posts.setUserId(userIdNew);
            }
            List<Postmeta> attachedPostmetaListNew = new ArrayList<Postmeta>();
            for (Postmeta postmetaListNewPostmetaToAttach : postmetaListNew) {
                postmetaListNewPostmetaToAttach = em.getReference(postmetaListNewPostmetaToAttach.getClass(), postmetaListNewPostmetaToAttach.getId());
                attachedPostmetaListNew.add(postmetaListNewPostmetaToAttach);
            }
            postmetaListNew = attachedPostmetaListNew;
            posts.setPostmetaList(postmetaListNew);
            List<TermRelationships> attachedTermRelationshipsListNew = new ArrayList<TermRelationships>();
            for (TermRelationships termRelationshipsListNewTermRelationshipsToAttach : termRelationshipsListNew) {
                termRelationshipsListNewTermRelationshipsToAttach = em.getReference(termRelationshipsListNewTermRelationshipsToAttach.getClass(), termRelationshipsListNewTermRelationshipsToAttach.getTermRelationshipsPK());
                attachedTermRelationshipsListNew.add(termRelationshipsListNewTermRelationshipsToAttach);
            }
            termRelationshipsListNew = attachedTermRelationshipsListNew;
            posts.setTermRelationshipsList(termRelationshipsListNew);
            List<Comments> attachedCommentsListNew = new ArrayList<Comments>();
            for (Comments commentsListNewCommentsToAttach : commentsListNew) {
                commentsListNewCommentsToAttach = em.getReference(commentsListNewCommentsToAttach.getClass(), commentsListNewCommentsToAttach.getId());
                attachedCommentsListNew.add(commentsListNewCommentsToAttach);
            }
            commentsListNew = attachedCommentsListNew;
            posts.setCommentsList(commentsListNew);
            posts = em.merge(posts);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getPostsList().remove(posts);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getPostsList().add(posts);
                userIdNew = em.merge(userIdNew);
            }
            for (Postmeta postmetaListNewPostmeta : postmetaListNew) {
                if (!postmetaListOld.contains(postmetaListNewPostmeta)) {
                    Posts oldPostIdOfPostmetaListNewPostmeta = postmetaListNewPostmeta.getPostId();
                    postmetaListNewPostmeta.setPostId(posts);
                    postmetaListNewPostmeta = em.merge(postmetaListNewPostmeta);
                    if (oldPostIdOfPostmetaListNewPostmeta != null && !oldPostIdOfPostmetaListNewPostmeta.equals(posts)) {
                        oldPostIdOfPostmetaListNewPostmeta.getPostmetaList().remove(postmetaListNewPostmeta);
                        oldPostIdOfPostmetaListNewPostmeta = em.merge(oldPostIdOfPostmetaListNewPostmeta);
                    }
                }
            }
            for (TermRelationships termRelationshipsListNewTermRelationships : termRelationshipsListNew) {
                if (!termRelationshipsListOld.contains(termRelationshipsListNewTermRelationships)) {
                    Posts oldPostsOfTermRelationshipsListNewTermRelationships = termRelationshipsListNewTermRelationships.getPosts();
                    termRelationshipsListNewTermRelationships.setPosts(posts);
                    termRelationshipsListNewTermRelationships = em.merge(termRelationshipsListNewTermRelationships);
                    if (oldPostsOfTermRelationshipsListNewTermRelationships != null && !oldPostsOfTermRelationshipsListNewTermRelationships.equals(posts)) {
                        oldPostsOfTermRelationshipsListNewTermRelationships.getTermRelationshipsList().remove(termRelationshipsListNewTermRelationships);
                        oldPostsOfTermRelationshipsListNewTermRelationships = em.merge(oldPostsOfTermRelationshipsListNewTermRelationships);
                    }
                }
            }
            for (Comments commentsListNewComments : commentsListNew) {
                if (!commentsListOld.contains(commentsListNewComments)) {
                    Posts oldPostIdOfCommentsListNewComments = commentsListNewComments.getPostId();
                    commentsListNewComments.setPostId(posts);
                    commentsListNewComments = em.merge(commentsListNewComments);
                    if (oldPostIdOfCommentsListNewComments != null && !oldPostIdOfCommentsListNewComments.equals(posts)) {
                        oldPostIdOfCommentsListNewComments.getCommentsList().remove(commentsListNewComments);
                        oldPostIdOfCommentsListNewComments = em.merge(oldPostIdOfCommentsListNewComments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = posts.getId();
                if (findPosts(id) == null) {
                    throw new NonexistentEntityException("The posts with id " + id + " no longer exists.");
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
            Posts posts;
            try {
                posts = em.getReference(Posts.class, id);
                posts.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The posts with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Postmeta> postmetaListOrphanCheck = posts.getPostmetaList();
            for (Postmeta postmetaListOrphanCheckPostmeta : postmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Posts (" + posts + ") cannot be destroyed since the Postmeta " + postmetaListOrphanCheckPostmeta + " in its postmetaList field has a non-nullable postId field.");
            }
            List<TermRelationships> termRelationshipsListOrphanCheck = posts.getTermRelationshipsList();
            for (TermRelationships termRelationshipsListOrphanCheckTermRelationships : termRelationshipsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Posts (" + posts + ") cannot be destroyed since the TermRelationships " + termRelationshipsListOrphanCheckTermRelationships + " in its termRelationshipsList field has a non-nullable posts field.");
            }
            List<Comments> commentsListOrphanCheck = posts.getCommentsList();
            for (Comments commentsListOrphanCheckComments : commentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Posts (" + posts + ") cannot be destroyed since the Comments " + commentsListOrphanCheckComments + " in its commentsList field has a non-nullable postId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userId = posts.getUserId();
            if (userId != null) {
                userId.getPostsList().remove(posts);
                userId = em.merge(userId);
            }
            em.remove(posts);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Posts> findPostsEntities() {
        return findPostsEntities(true, -1, -1);
    }

    public List<Posts> findPostsEntities(int maxResults, int firstResult) {
        return findPostsEntities(false, maxResults, firstResult);
    }

    private List<Posts> findPostsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Posts.class));
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

    public Posts findPosts(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Posts.class, id);
        } finally {
            em.close();
        }
    }

    public int getPostsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Posts> rt = cq.from(Posts.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
