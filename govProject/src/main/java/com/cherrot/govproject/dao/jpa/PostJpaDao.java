/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import java.util.ArrayList;
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
public class PostJpaDao implements PostDao {

//    public PostJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Post post) {
        /**
         * 初始化一对多关系集合
         */
        if (post.getPostmetaList() == null) {
            post.setPostmetaList(new ArrayList<Postmeta>());
        }
        if (post.getPostList() == null) {
            post.setPostList(new ArrayList<Post>());
        }
        if (post.getCategoryList() == null) {
            post.setCategoryList(new ArrayList<Category>());
        }
        if (post.getTagList() == null) {
            post.setTagList(new ArrayList<Tag>());
        }
        if (post.getCommentList() == null) {
            post.setCommentList(new ArrayList<Comment>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
        
        /**
         * 设置多对一关系映射，确保引用在数据库中存在
         */
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
        /**
         * 设置一对多关系映射，确保一对多的每个引用都在数据库中存在
         */
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
            List<Category> attachedCategoryList = new ArrayList<Category>();
            for (Category categoryListCategoryToAttach : post.getCategoryList()) {
                categoryListCategoryToAttach = em.getReference(categoryListCategoryToAttach.getClass(), categoryListCategoryToAttach.getId());
                attachedCategoryList.add(categoryListCategoryToAttach);
            }
            post.setCategoryList(attachedCategoryList);
            List<Tag> attachedTagList = new ArrayList<Tag>();
            for (Tag tagListTagToAttach : post.getTagList()) {
                tagListTagToAttach = em.getReference(tagListTagToAttach.getClass(), tagListTagToAttach.getId());
                attachedTagList.add(tagListTagToAttach);
            }
            post.setTagList(attachedTagList);
            List<Comment> attachedCommentList = new ArrayList<Comment>();
            for (Comment commentListCommentToAttach : post.getCommentList()) {
                commentListCommentToAttach = em.getReference(commentListCommentToAttach.getClass(), commentListCommentToAttach.getId());
                attachedCommentList.add(commentListCommentToAttach);
            }
            post.setCommentList(attachedCommentList);
        /**
         * 关系确保无误后，持久化该实体
         */
            em.persist(post);
            
        /**
         * 设置多对一关系映射的被维护端（一方）
         */
            if (user != null) {
                user.getPostList().add(post);
                user = em.merge(user);
            }
            if (postParent != null) {
                postParent.getPostList().add(post);
                postParent = em.merge(postParent);
            }
        /**
         * 设置一对多关系映射的维护端（多方）。
         */
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
            for (Category categoryListCategory : post.getCategoryList()) {
                categoryListCategory.getPostList().add(post);
                categoryListCategory = em.merge(categoryListCategory);
            }
            for (Tag tagListTag : post.getTagList()) {
                tagListTag.getPostList().add(post);
                tagListTag = em.merge(tagListTag);
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
            
            /**
             * 取出新旧实体的 一对多关系 和 多对一关系所关联的实体
             */
            Post persistentPost = em.find(Post.class, post.getId());
            User userOld = persistentPost.getUser();
            User userNew = post.getUser();
            Post postParentOld = persistentPost.getPostParent();
            Post postParentNew = post.getPostParent();
            List<Postmeta> postmetaListOld = persistentPost.getPostmetaList();
            List<Postmeta> postmetaListNew = post.getPostmetaList();
            List<Post> postListOld = persistentPost.getPostList();
            List<Post> postListNew = post.getPostList();
            List<Category> categoryListOld = persistentPost.getCategoryList();
            List<Category> categoryListNew = post.getCategoryList();
            List<Tag> tagListOld = persistentPost.getTagList();
            List<Tag> tagListNew = post.getTagList();
            List<Comment> commentListOld = persistentPost.getCommentList();
            List<Comment> commentListNew = post.getCommentList();
            List<String> illegalOrphanMessages = null;
            /**
             * 检查一对多关系是否正确，即确保之前已关联的实体没有被漏掉造成孤儿实体
             */
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
            /**
             * 设置多对一关系映射，确保映射实体类存在
             */
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                post.setUser(userNew);
            }
            if (postParentNew != null) {
                postParentNew = em.getReference(postParentNew.getClass(), postParentNew.getId());
                post.setPostParent(postParentNew);
            }
            /**
             * 设置一对多关系映射，确保每一个实体类存在
             */
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
            List<Category> attachedCategoryListNew = new ArrayList<Category>();
            for (Category categoryListNewCategoryToAttach : categoryListNew) {
                categoryListNewCategoryToAttach = em.getReference(categoryListNewCategoryToAttach.getClass(), categoryListNewCategoryToAttach.getId());
                attachedCategoryListNew.add(categoryListNewCategoryToAttach);
            }
            categoryListNew = attachedCategoryListNew;
            post.setCategoryList(categoryListNew);
            List<Tag> attachedTagListNew = new ArrayList<Tag>();
            for (Tag tagListNewTagToAttach : tagListNew) {
                tagListNewTagToAttach = em.getReference(tagListNewTagToAttach.getClass(), tagListNewTagToAttach.getId());
                attachedTagListNew.add(tagListNewTagToAttach);
            }
            tagListNew = attachedTagListNew;
            post.setTagList(tagListNew);
            List<Comment> attachedCommentListNew = new ArrayList<Comment>();
            for (Comment commentListNewCommentToAttach : commentListNew) {
                commentListNewCommentToAttach = em.getReference(commentListNewCommentToAttach.getClass(), commentListNewCommentToAttach.getId());
                attachedCommentListNew.add(commentListNewCommentToAttach);
            }
            commentListNew = attachedCommentListNew;
            post.setCommentList(commentListNew);
            /**
             * 检查无误，持久化实体
             */
            post = em.merge(post);
            
            /**
             * 设置多对一关系映射的被维护端（一方）
             */
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
            /**
             * 设置一对多关系的关系维护端（多方）
             */
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
            for (Category categoryListOldCategory : categoryListOld) {
                if (!categoryListNew.contains(categoryListOldCategory)) {
                    categoryListOldCategory.getPostList().remove(post);
                    categoryListOldCategory = em.merge(categoryListOldCategory);
                }
            }
            for (Category categoryListNewCategory : categoryListNew) {
                if (!categoryListOld.contains(categoryListNewCategory)) {
                    categoryListNewCategory.getPostList().add(post);
                    categoryListNewCategory = em.merge(categoryListNewCategory);
                }
            }
            for (Tag tagListOldTag : tagListOld) {
                if (!tagListNew.contains(tagListOldTag)) {
                    tagListOldTag.getPostList().remove(post);
                    tagListOldTag = em.merge(tagListOldTag);
                }
            }
            for (Tag tagListNewTag : tagListNew) {
                if (!tagListOld.contains(tagListNewTag)) {
                    tagListNewTag.getPostList().add(post);
                    tagListNewTag = em.merge(tagListNewTag);
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
        } /*finally {
            if (em != null) {
                em.close();
            }
        }*/
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
            List<Category> categoryList = post.getCategoryList();
            for (Category categoryListCategory : categoryList) {
                categoryListCategory.getPostList().remove(post);
                categoryListCategory = em.merge(categoryListCategory);
            }
            List<Tag> tagList = post.getTagList();
            for (Tag tagListTag : tagList) {
                tagListTag.getPostList().remove(post);
                tagListTag = em.merge(tagListTag);
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
//        EntityManager em = getEntityManager();
//        try {
            return em.find(Post.class, id);
//        } finally {
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
            return ((Long) q.getSingleResult()).intValue();
//        } finally {
//            em.close();
//        }
    }

    @Override
    public List<Post> findEntitiesByCategoryDescOrder(Integer termId, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findEntitiesByCategoryDescOrder", Post.class);
        q.setParameter("categoryId", termId);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByCategoryNameDescOrder(String categoryName, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findEntitiesByCategoryNameDescOrder", Post.class);
        q.setParameter("category", categoryName);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public Post findBySlug(String slug) {
        return em.createNamedQuery("Post.findBySlug", Post.class)
            .setParameter("slug", slug).getSingleResult();
    }

    @Override
    public List<Post> findEntitiesByUserId(Integer userId, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findEntitiesByUserId", Post.class);
        q.setParameter("userId", userId);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public Post getReference(Integer id) {
        return em.getReference(Post.class, id);
    }
    
}
