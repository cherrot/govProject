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
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.User;
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
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Repository
public class PostJpaDao implements PostDao {

    @PersistenceContext
    private EntityManager em;
//    public PostJpaDao(UserTransaction utx, EntityManagerFactory emf) {
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
    public void create(Post post) {
        if (post.getPostmetaList() == null) {
            post.setPostmetaList(new ArrayList<Postmeta>());
        }
        if (post.getPostList() == null) {
            post.setPostList(new ArrayList<Post>());
        }
        if (post.getTermList() == null) {
            post.setTermList(new ArrayList<Term>());
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
            Post postParent = post.getPostParent();
            if (postParent != null) {
                postParent = em.getReference(postParent.getClass(), postParent.getId());
                post.setPostParent(postParent);
            }
            List<Postmeta> attachedPostmetaList = new ArrayList<Postmeta>();
            for (Postmeta postmetaListPostmetaToAttach : post.getPostmetaList()) {
                postmetaListPostmetaToAttach = em.getReference(postmetaListPostmetaToAttach.getClass(), postmetaListPostmetaToAttach.getId());
                attachedPostmetaList.add(postmetaListPostmetaToAttach);
            }
            post.setPostmetaList(attachedPostmetaList);
            List<Post> attachedPostList = new ArrayList<Post>();
            for (Post postListPostToAttach : post.getPostList()) {
                postListPostToAttach = em.getReference(postListPostToAttach.getClass(), postListPostToAttach.getId());
                attachedPostList.add(postListPostToAttach);
            }
            post.setPostList(attachedPostList);
            List<Term> attachedTermList = new ArrayList<Term>();
            for (Term termListTermToAttach : post.getTermList()) {
                termListTermToAttach = em.getReference(termListTermToAttach.getClass(), termListTermToAttach.getId());
                attachedTermList.add(termListTermToAttach);
            }
            post.setTermList(attachedTermList);
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
            if (postParent != null) {
                postParent.getPostList().add(post);
                postParent = em.merge(postParent);
            }
            for (Postmeta postmetaListPostmeta : post.getPostmetaList()) {
                Post oldPostOfPostmetaListPostmeta = postmetaListPostmeta.getPost();
                postmetaListPostmeta.setPost(post);
                postmetaListPostmeta = em.merge(postmetaListPostmeta);
                if (oldPostOfPostmetaListPostmeta != null) {
                    oldPostOfPostmetaListPostmeta.getPostmetaList().remove(postmetaListPostmeta);
                    oldPostOfPostmetaListPostmeta = em.merge(oldPostOfPostmetaListPostmeta);
                }
            }
            for (Post postListPost : post.getPostList()) {
                Post oldPostParentOfPostListPost = postListPost.getPostParent();
                postListPost.setPostParent(post);
                postListPost = em.merge(postListPost);
                if (oldPostParentOfPostListPost != null) {
                    oldPostParentOfPostListPost.getPostList().remove(postListPost);
                    oldPostParentOfPostListPost = em.merge(oldPostParentOfPostListPost);
                }
            }
            for (Term termListTerm : post.getTermList()) {
                termListTerm.getPostList().add(post);
                termListTerm = em.merge(termListTerm);
            }
            for (Comment commentListComment : post.getCommentList()) {
                Post oldPostOfCommentListComment = commentListComment.getPost();
                commentListComment.setPost(post);
                commentListComment = em.merge(commentListComment);
                if (oldPostOfCommentListComment != null) {
                    oldPostOfCommentListComment.getCommentList().remove(commentListComment);
                    oldPostOfCommentListComment = em.merge(oldPostOfCommentListComment);
                }
            }
//            em.getTransaction().commit();
//        } finally {
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
            User userOld = persistentPost.getUser();
            User userNew = post.getUser();
            Post postParentOld = persistentPost.getPostParent();
            Post postParentNew = post.getPostParent();
            List<Postmeta> postmetaListOld = persistentPost.getPostmetaList();
            List<Postmeta> postmetaListNew = post.getPostmetaList();
            List<Post> postListOld = persistentPost.getPostList();
            List<Post> postListNew = post.getPostList();
            List<Term> termListOld = persistentPost.getTermList();
            List<Term> termListNew = post.getTermList();
            List<Comment> commentListOld = persistentPost.getCommentList();
            List<Comment> commentListNew = post.getCommentList();
            List<String> illegalOrphanMessages = null;
            for (Postmeta postmetaListOldPostmeta : postmetaListOld) {
                if (!postmetaListNew.contains(postmetaListOldPostmeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Postmeta " + postmetaListOldPostmeta + " since its post field is not nullable.");
                }
            }
            for (Comment commentListOldComment : commentListOld) {
                if (!commentListNew.contains(commentListOldComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comment " + commentListOldComment + " since its post field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                post.setUser(userNew);
            }
            if (postParentNew != null) {
                postParentNew = em.getReference(postParentNew.getClass(), postParentNew.getId());
                post.setPostParent(postParentNew);
            }
            List<Postmeta> attachedPostmetaListNew = new ArrayList<Postmeta>();
            for (Postmeta postmetaListNewPostmetaToAttach : postmetaListNew) {
                postmetaListNewPostmetaToAttach = em.getReference(postmetaListNewPostmetaToAttach.getClass(), postmetaListNewPostmetaToAttach.getId());
                attachedPostmetaListNew.add(postmetaListNewPostmetaToAttach);
            }
            postmetaListNew = attachedPostmetaListNew;
            post.setPostmetaList(postmetaListNew);
            List<Post> attachedPostListNew = new ArrayList<Post>();
            for (Post postListNewPostToAttach : postListNew) {
                postListNewPostToAttach = em.getReference(postListNewPostToAttach.getClass(), postListNewPostToAttach.getId());
                attachedPostListNew.add(postListNewPostToAttach);
            }
            postListNew = attachedPostListNew;
            post.setPostList(postListNew);
            List<Term> attachedTermListNew = new ArrayList<Term>();
            for (Term termListNewTermToAttach : termListNew) {
                termListNewTermToAttach = em.getReference(termListNewTermToAttach.getClass(), termListNewTermToAttach.getId());
                attachedTermListNew.add(termListNewTermToAttach);
            }
            termListNew = attachedTermListNew;
            post.setTermList(termListNew);
            List<Comment> attachedCommentListNew = new ArrayList<Comment>();
            for (Comment commentListNewCommentToAttach : commentListNew) {
                commentListNewCommentToAttach = em.getReference(commentListNewCommentToAttach.getClass(), commentListNewCommentToAttach.getId());
                attachedCommentListNew.add(commentListNewCommentToAttach);
            }
            commentListNew = attachedCommentListNew;
            post.setCommentList(commentListNew);
            post = em.merge(post);
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getPostList().remove(post);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getPostList().add(post);
                userNew = em.merge(userNew);
            }
            if (postParentOld != null && !postParentOld.equals(postParentNew)) {
                postParentOld.getPostList().remove(post);
                postParentOld = em.merge(postParentOld);
            }
            if (postParentNew != null && !postParentNew.equals(postParentOld)) {
                postParentNew.getPostList().add(post);
                postParentNew = em.merge(postParentNew);
            }
            for (Postmeta postmetaListNewPostmeta : postmetaListNew) {
                if (!postmetaListOld.contains(postmetaListNewPostmeta)) {
                    Post oldPostOfPostmetaListNewPostmeta = postmetaListNewPostmeta.getPost();
                    postmetaListNewPostmeta.setPost(post);
                    postmetaListNewPostmeta = em.merge(postmetaListNewPostmeta);
                    if (oldPostOfPostmetaListNewPostmeta != null && !oldPostOfPostmetaListNewPostmeta.equals(post)) {
                        oldPostOfPostmetaListNewPostmeta.getPostmetaList().remove(postmetaListNewPostmeta);
                        oldPostOfPostmetaListNewPostmeta = em.merge(oldPostOfPostmetaListNewPostmeta);
                    }
                }
            }
            for (Post postListOldPost : postListOld) {
                if (!postListNew.contains(postListOldPost)) {
                    postListOldPost.setPostParent(null);
                    postListOldPost = em.merge(postListOldPost);
                }
            }
            for (Post postListNewPost : postListNew) {
                if (!postListOld.contains(postListNewPost)) {
                    Post oldPostParentOfPostListNewPost = postListNewPost.getPostParent();
                    postListNewPost.setPostParent(post);
                    postListNewPost = em.merge(postListNewPost);
                    if (oldPostParentOfPostListNewPost != null && !oldPostParentOfPostListNewPost.equals(post)) {
                        oldPostParentOfPostListNewPost.getPostList().remove(postListNewPost);
                        oldPostParentOfPostListNewPost = em.merge(oldPostParentOfPostListNewPost);
                    }
                }
            }
            for (Term termListOldTerm : termListOld) {
                if (!termListNew.contains(termListOldTerm)) {
                    termListOldTerm.getPostList().remove(post);
                    termListOldTerm = em.merge(termListOldTerm);
                }
            }
            for (Term termListNewTerm : termListNew) {
                if (!termListOld.contains(termListNewTerm)) {
                    termListNewTerm.getPostList().add(post);
                    termListNewTerm = em.merge(termListNewTerm);
                }
            }
            for (Comment commentListNewComment : commentListNew) {
                if (!commentListOld.contains(commentListNewComment)) {
                    Post oldPostOfCommentListNewComment = commentListNewComment.getPost();
                    commentListNewComment.setPost(post);
                    commentListNewComment = em.merge(commentListNewComment);
                    if (oldPostOfCommentListNewComment != null && !oldPostOfCommentListNewComment.equals(post)) {
                        oldPostOfCommentListNewComment.getCommentList().remove(commentListNewComment);
                        oldPostOfCommentListNewComment = em.merge(oldPostOfCommentListNewComment);
                    }
                }
            }
//            em.getTransaction().commit();
        } catch (Exception ex) {
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
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The post with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Postmeta> postmetaListOrphanCheck = post.getPostmetaList();
            for (Postmeta postmetaListOrphanCheckPostmeta : postmetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Post (" + post + ") cannot be destroyed since the Postmeta " + postmetaListOrphanCheckPostmeta + " in its postmetaList field has a non-nullable post field.");
            }
            List<Comment> commentListOrphanCheck = post.getCommentList();
            for (Comment commentListOrphanCheckComment : commentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Post (" + post + ") cannot be destroyed since the Comment " + commentListOrphanCheckComment + " in its commentList field has a non-nullable post field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User user = post.getUser();
            if (user != null) {
                user.getPostList().remove(post);
                user = em.merge(user);
            }
            Post postParent = post.getPostParent();
            if (postParent != null) {
                postParent.getPostList().remove(post);
                postParent = em.merge(postParent);
            }
            List<Post> postList = post.getPostList();
            for (Post postListPost : postList) {
                postListPost.setPostParent(null);
                postListPost = em.merge(postListPost);
            }
            List<Term> termList = post.getTermList();
            for (Term termListTerm : termList) {
                termListTerm.getPostList().remove(post);
                termListTerm = em.merge(termListTerm);
            }
            em.remove(post);
//            em.getTransaction().commit();
//        } finally {
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
//        } finally {
//            em.close();
//        }
    }

    @Override
    public Post find(Integer id) {
            return em.find(Post.class, id);
    }

    @Override
    public int getCount() {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Post> rt = cq.from(Post.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
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

    @Override
    public List<Post> findEntitiesByTermOrderbyCreateDate(Term term, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findEntitiesByTermOrderbyCreateDate", Post.class);
        q.setParameter("term", term);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByCategoryNameOrderbyCreateDate(String categoryName, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findEntitiesByCategoryNameOrderbyCreateDate", Post.class);
        q.setParameter("category", categoryName);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

}
