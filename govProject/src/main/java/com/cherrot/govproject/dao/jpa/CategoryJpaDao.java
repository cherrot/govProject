/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.CategoryDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
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
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Repository
public class CategoryJpaDao implements CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Category category) {
        /**
         * 初始化一对多关系集合
         */
        if (category.getCategoryList() == null) {
            category.setCategoryList(new ArrayList<Category>());
        }
        if (category.getPostList() == null) {
            category.setPostList(new ArrayList<Post>());
        }
        /**
         * 设置多对一关系映射，确保引用在数据库中存在
         */
        Category categoryParent = category.getCategoryParent();
        if (categoryParent != null) {
            categoryParent = em.getReference(categoryParent.getClass(), categoryParent.getId());
            category.setCategoryParent(categoryParent);
        }
        /**
         * 设置一对多关系映射，确保一对多的每个引用都在数据库中存在
         */
        List<Category> attachedCategoryList = new ArrayList<Category>();
        for (Category categoryListCategoryToAttach : category.getCategoryList()) {
            categoryListCategoryToAttach = em.getReference(categoryListCategoryToAttach.getClass(), categoryListCategoryToAttach.getId());
            attachedCategoryList.add(categoryListCategoryToAttach);
        }
        category.setCategoryList(attachedCategoryList);
        List<Post> attachedPostList = new ArrayList<Post>();
        for (Post postListPostToAttach : category.getPostList()) {
            postListPostToAttach = em.getReference(postListPostToAttach.getClass(), postListPostToAttach.getId());
            attachedPostList.add(postListPostToAttach);
        }
        category.setPostList(attachedPostList);
        /**
         * 关系确保无误后，持久化该对象
         */
        em.persist(category);

        /**
         * 设置多对一关系映射的被维护端（一方）
         */
        if (categoryParent != null) {
            categoryParent.getCategoryList().add(category);
            categoryParent = em.merge(categoryParent);
        }
        /**
         * 设置一对多关系映射的维护端（多方）。
         * 疑问：如果oldCategoryParentOfCategoryListCategory 和 category是同一引用会不会出问题？
         * 答案：不会，这是在create方法中，category之前不存在的
         */
        for (Category categoryListCategory : category.getCategoryList()) {
            Category oldCategoryParentOfCategoryListCategory = categoryListCategory.getCategoryParent();
            categoryListCategory.setCategoryParent(category);
            categoryListCategory = em.merge(categoryListCategory);
            if (oldCategoryParentOfCategoryListCategory != null) {
                oldCategoryParentOfCategoryListCategory.getCategoryList().remove(categoryListCategory);
                oldCategoryParentOfCategoryListCategory = em.merge(oldCategoryParentOfCategoryListCategory);
            }
        }
        for (Post postListPost : category.getPostList()) {
            postListPost.getCategoryList().add(category);
            postListPost = em.merge(postListPost);
        }
    }

    @Override
    @Transactional
    public void edit(Category category) throws NonexistentEntityException, Exception {
        try {
            /**
             * 取出新旧Category对象的 一对多关系 和 多对一关系所关联的实体
             */
            Category persistentCategory = em.find(Category.class, category.getId());
            Category categoryParentOld = persistentCategory.getCategoryParent();
            Category categoryParentNew = category.getCategoryParent();
            List<Category> categoryListOld = persistentCategory.getCategoryList();
            List<Category> categoryListNew = category.getCategoryList();
            List<Post> postListOld = persistentCategory.getPostList();
            List<Post> postListNew = category.getPostList();

            //FIXME 临时方案 应该在业务逻辑层解决！
//            if (categoryListNew == null) categoryListNew = categoryListOld;
//            if (postListNew == null) postListNew = postListOld;

            /**
             * 设置多对一关系映射，确保映射实体类存在
             */
            if (categoryParentNew != null) {
                categoryParentNew = em.getReference(categoryParentNew.getClass(), categoryParentNew.getId());
                category.setCategoryParent(categoryParentNew);
            }
            /**
             * 设置一对多关系映射，确保每一个实体类存在
             */
            List<Category> attachedCategoryListNew = new ArrayList<Category>();
            for (Category categoryListNewCategoryToAttach : categoryListNew) {
                categoryListNewCategoryToAttach = em.getReference(categoryListNewCategoryToAttach.getClass(), categoryListNewCategoryToAttach.getId());
                attachedCategoryListNew.add(categoryListNewCategoryToAttach);
            }
            categoryListNew = attachedCategoryListNew;
            category.setCategoryList(categoryListNew);
            List<Post> attachedPostListNew = new ArrayList<Post>();
            for (Post postListNewPostToAttach : postListNew) {
                postListNewPostToAttach = em.getReference(postListNewPostToAttach.getClass(), postListNewPostToAttach.getId());
                attachedPostListNew.add(postListNewPostToAttach);
            }
            postListNew = attachedPostListNew;
            category.setPostList(postListNew);
            /**
             * 持久化该实体
             */
            category = em.merge(category);

            /**
             * 设置多对一关系映射的被维护端（一方）
             */
            if (categoryParentOld != null && !categoryParentOld.equals(categoryParentNew)) {
                categoryParentOld.getCategoryList().remove(category);
                categoryParentOld = em.merge(categoryParentOld);
            }
            if (categoryParentNew != null && !categoryParentNew.equals(categoryParentOld)) {
                categoryParentNew.getCategoryList().add(category);
                categoryParentNew = em.merge(categoryParentNew);
            }
            /**
             * 设置一对多关系的关系维护端（多方）
             */
            for (Category categoryListOldCategory : categoryListOld) {
                if (!categoryListNew.contains(categoryListOldCategory)) {
                    categoryListOldCategory.setCategoryParent(null);
                    categoryListOldCategory = em.merge(categoryListOldCategory);
                }
            }
            for (Category categoryListNewCategory : categoryListNew) {
                if (!categoryListOld.contains(categoryListNewCategory)) {
                    Category oldCategoryParentOfCategoryListNewCategory = categoryListNewCategory.getCategoryParent();
                    categoryListNewCategory.setCategoryParent(category);
                    categoryListNewCategory = em.merge(categoryListNewCategory);
                    if (oldCategoryParentOfCategoryListNewCategory != null && !oldCategoryParentOfCategoryListNewCategory.equals(category)) {
                        oldCategoryParentOfCategoryListNewCategory.getCategoryList().remove(categoryListNewCategory);
                        oldCategoryParentOfCategoryListNewCategory = em.merge(oldCategoryParentOfCategoryListNewCategory);
                    }
                }
            }
            for (Post postListOldPost : postListOld) {
                if (!postListNew.contains(postListOldPost)) {
                    postListOldPost.getCategoryList().remove(category);
                    postListOldPost = em.merge(postListOldPost);
                }
            }
            for (Post postListNewPost : postListNew) {
                if (!postListOld.contains(postListNewPost)) {
                    postListNewPost.getCategoryList().add(category);
                    postListNewPost = em.merge(postListNewPost);
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = category.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The term with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public void destroy(Integer id) throws NonexistentEntityException {
        Category category;
        try {
            category = em.getReference(Category.class, id);
            category.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The term with id " + id + " no longer exists.", enfe);
        }
        Category categoryParent = category.getCategoryParent();
        if (categoryParent != null) {
            categoryParent.getCategoryList().remove(category);
            categoryParent = em.merge(categoryParent);
        }
        List<Category> categoryList = category.getCategoryList();
        for (Category categoryListCategory : categoryList) {
            categoryListCategory.setCategoryParent(null);
            categoryListCategory = em.merge(categoryListCategory);
        }
        List<Post> postList = category.getPostList();
        for (Post postListPost : postList) {
            postListPost.getCategoryList().remove(category);
            postListPost = em.merge(postListPost);
        }
        em.remove(category);
    }

    @Override
    public List<Category> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Category> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Category> findEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Category.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Category find(Integer id) {
        return em.find(Category.class, id);
    }

    @Override
    public int getCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Category> rt = cq.from(Category.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public Category findBySlug(String slug) {
        return em.createNamedQuery("Category.findBySlug", Category.class)
            .setParameter("slug", slug).getSingleResult();
    }

    @Override
    public Category getReference(Integer id) {
        return em.getReference(Category.class, id);
    }
}
