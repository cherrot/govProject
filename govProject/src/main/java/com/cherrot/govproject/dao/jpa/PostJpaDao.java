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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 添加 修改 删除文章时记得处理 目录或标签的setCount()设置正确的文章数目. 还有LinkJpaDao、 CommentJpaDao
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
            user = em.getReference(User.class, user.getId());
            post.setUser(user);
        }
        /**
         * 设置一对多关系映射，确保一对多的每个引用都在数据库中存在
         */
        List<Postmeta> attachedPostmetaList = new ArrayList<Postmeta>();
        for (Postmeta postmetaListPostmetaToAttach : post.getPostmetaList()) {
            postmetaListPostmetaToAttach = em.getReference(Postmeta.class, postmetaListPostmetaToAttach.getId());
            attachedPostmetaList.add(postmetaListPostmetaToAttach);
        }
        post.setPostmetaList(attachedPostmetaList);
        List<Category> attachedCategoryList = new ArrayList<Category>();
        for (Category categoryListCategoryToAttach : post.getCategoryList()) {
            categoryListCategoryToAttach = em.getReference(Category.class, categoryListCategoryToAttach.getId());
            attachedCategoryList.add(categoryListCategoryToAttach);
        }
        post.setCategoryList(attachedCategoryList);
        List<Tag> attachedTagList = new ArrayList<Tag>();
        for (Tag tagListTagToAttach : post.getTagList()) {
            tagListTagToAttach = em.getReference(Tag.class, tagListTagToAttach.getId());
            attachedTagList.add(tagListTagToAttach);
        }
        post.setTagList(attachedTagList);
        List<Comment> attachedCommentList = new ArrayList<Comment>();
        for (Comment commentListCommentToAttach : post.getCommentList()) {
            commentListCommentToAttach = em.getReference(Comment.class, commentListCommentToAttach.getId());
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
        /**
         * 设置一对多关系映射的维护端（多方）。 和 多对多关系
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
        for (Category categoryListCategory : post.getCategoryList()) {
            categoryListCategory.getPostList().add(post);
            //设置count字段
            categoryListCategory.setCount(categoryListCategory.getCount() + 1);
            categoryListCategory = em.merge(categoryListCategory);
        }
        for (Tag tagListTag : post.getTagList()) {
            tagListTag.getPostList().add(post);
            //设置count字段
            tagListTag.setCount(tagListTag.getCount() + 1);
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

        try {
            /**
             * 取出新旧实体的 一对多关系 和 多对一关系所关联的实体
             */
            Post persistentPost = em.find(Post.class, post.getId());
            //XXX Post在控制器中已改变的一对多关系不会被覆盖（分类列表、标签列表、postmeta列表），其他一对多关系会被覆盖。
            try {
                post.getCategoryList().size();
            } catch (Exception e) {
                post.setCategoryList(persistentPost.getCategoryList());
            }
            try {
                post.getPostmetaList().size();
            } catch (Exception e) {
                post.setPostmetaList(persistentPost.getPostmetaList());
            }
            try {
                post.getTagList().size();
            } catch (Exception e) {
                post.setTagList(persistentPost.getTagList());
            }
            post.setCommentList(persistentPost.getCommentList());

            User userOld = persistentPost.getUser();
            User userNew = post.getUser();
            List<Postmeta> postmetaListOld = persistentPost.getPostmetaList();
            List<Postmeta> postmetaListNew = post.getPostmetaList();
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
                userNew = em.getReference(User.class, userNew.getId());
                post.setUser(userNew);
            }
            /**
             * 设置一对多关系映射，确保每一个实体类存在
             */
            List<Postmeta> attachedPostmetaListNew = new ArrayList<Postmeta>();
            for (Postmeta postmetaListNewPostmetaToAttach : postmetaListNew) {
                postmetaListNewPostmetaToAttach = em.getReference(Postmeta.class, postmetaListNewPostmetaToAttach.getId());
                attachedPostmetaListNew.add(postmetaListNewPostmetaToAttach);
            }
            postmetaListNew = attachedPostmetaListNew;
            post.setPostmetaList(postmetaListNew);
            List<Category> attachedCategoryListNew = new ArrayList<Category>();
            for (Category categoryListNewCategoryToAttach : categoryListNew) {
                categoryListNewCategoryToAttach = em.getReference(Category.class, categoryListNewCategoryToAttach.getId());
                attachedCategoryListNew.add(categoryListNewCategoryToAttach);
            }
            categoryListNew = attachedCategoryListNew;
            post.setCategoryList(categoryListNew);
            List<Tag> attachedTagListNew = new ArrayList<Tag>();
            for (Tag tagListNewTagToAttach : tagListNew) {
                tagListNewTagToAttach = em.getReference(Tag.class, tagListNewTagToAttach.getId());
                attachedTagListNew.add(tagListNewTagToAttach);
            }
            tagListNew = attachedTagListNew;
            post.setTagList(tagListNew);
            List<Comment> attachedCommentListNew = new ArrayList<Comment>();
            for (Comment commentListNewCommentToAttach : commentListNew) {
                commentListNewCommentToAttach = em.getReference(Comment.class, commentListNewCommentToAttach.getId());
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
            /**
             * 设置一对多关系的关系维护端（多方） 和 多对多关系
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
            for (Category categoryListOldCategory : categoryListOld) {
                if (!categoryListNew.contains(categoryListOldCategory)) {
                    categoryListOldCategory.getPostList().remove(post);
                    //设置count字段
                    categoryListOldCategory.setCount(categoryListOldCategory.getCount() - 1);
                    categoryListOldCategory = em.merge(categoryListOldCategory);
                }
            }
            for (Category categoryListNewCategory : categoryListNew) {
                if (!categoryListOld.contains(categoryListNewCategory)) {
                    categoryListNewCategory.getPostList().add(post);
                    //设置count字段
                    categoryListNewCategory.setCount(categoryListNewCategory.getCount() + 1);
                    categoryListNewCategory = em.merge(categoryListNewCategory);
                }
            }
            for (Tag tagListOldTag : tagListOld) {
                if (!tagListNew.contains(tagListOldTag)) {
                    tagListOldTag.getPostList().remove(post);
                    //设置count字段
                    tagListOldTag.setCount(tagListOldTag.getCount() - 1);
                    tagListOldTag = em.merge(tagListOldTag);
                }
            }
            for (Tag tagListNewTag : tagListNew) {
                if (!tagListOld.contains(tagListNewTag)) {
                    tagListNewTag.getPostList().add(post);
                    //设置count字段
                    tagListNewTag.setCount(tagListNewTag.getCount() + 1);
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
        List<Category> categoryList = post.getCategoryList();
        for (Category categoryListCategory : categoryList) {
            categoryListCategory.getPostList().remove(post);
            //设置count字段
            categoryListCategory.setCount(categoryListCategory.getCount() - 1);
            categoryListCategory = em.merge(categoryListCategory);
        }
        List<Tag> tagList = post.getTagList();
        for (Tag tagListTag : tagList) {
            tagListTag.getPostList().remove(post);
            //设置count字段
            tagListTag.setCount(tagListTag.getCount() - 1);
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
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Post.class));
//            Query q = em.createQuery(cq);
        Query q = em.createNamedQuery("Post.findAll", Post.class);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
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
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<Post> rt = cq.from(Post.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
        Query q = em.createNamedQuery("Post.getCount");
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public List<Post> findEntitiesByCategoryDesc(Category category, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByCategoryDesc", Post.class);
        q.setParameter("category", category);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByCategorySlugDesc(String categorySlug, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByCategorySlugDesc", Post.class);
        q.setParameter("categorySlug", categorySlug);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByTagDesc(Tag tag, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByTagDesc", Post.class);
        q.setParameter("tag", tag);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByTagSlugDesc(String tagSlug, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByTagSlugDesc", Post.class);
        q.setParameter("tagSlug", tagSlug);
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
    public List<Post> findEntitiesByUser(User user, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByUser", Post.class);
        q.setParameter("user", user);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByUserDesc(User user, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findByUserDesc", Post.class);
        q.setParameter("user", user);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Post> findEntitiesByTypeDesc(Post.PostType type, int maxResults, int firstResult) {
        return null;//FIXME
        
    }
    
    @Override
    public Post getReference(Integer id) {
        return em.getReference(Post.class, id);
    }

    @Override
    public int getCountByUser(User user) {
        return ((Long) em.createNamedQuery("Post.getCountByUser")
            .setParameter("user", user).getSingleResult()).intValue();
    }

    @Override
    public int getCountByCategory(Category category) {
        return ((Long) em.createNamedQuery("Post.getCountByUser")
            .setParameter("category", category).getSingleResult()).intValue();
    }

    @Override
    public int getCountByTag(Tag tag) {
        return ((Long) em.createNamedQuery("Post.getCountByUser")
            .setParameter("tag", tag).getSingleResult()).intValue();
    }

    @Override
    public int getCountByType(Post.PostType type) {
        return ((Long) em.createNamedQuery("Post.getCountByUser")
            .setParameter("type", type).getSingleResult()).intValue();
    }

    @Override
    public List<Post> findEntitiesDesc(int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Post.findAllDesc", Post.class);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
}
