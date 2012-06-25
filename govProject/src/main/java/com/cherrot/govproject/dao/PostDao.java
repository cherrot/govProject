/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostDao extends Serializable, BaseDao<Post, Integer> {

    Post findBySlug(String slug);
    List<Post> findEntitiesDesc(int maxResults, int firstResult);
    List<Post> findEntitiesByCategoryDesc(Category category, int maxResults, int firstResult);
    List<Post> findEntitiesByCategorySlugDesc(String categorySlug, int maxResults, int firstResult);
    List<Post> findEntitiesByTagDesc(Tag tag, int maxResults, int firstResult);
    List<Post> findEntitiesByTagSlugDesc(String tagSlug, int maxResults, int firstResult);
    List<Post> findEntitiesByUser(User user, int maxResults, int firstResult);
    List<Post> findEntitiesByUserDesc(User user, int maxResults, int firstResult);
    /**
     * mime支持模糊查询。可以使用JPQL的模糊字符串。(如"image/%")
     * @param mime
     * @return
     */
    List<Post> findEntitiesByMimeDesc(String mime, int maxResults, int firstResult);
    int getCountByUser(User user);
    int getCountByCategory(Category category);
    int getCountByTag(Tag tag);

//    void create(Post post);
//
//    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;
//
//    void edit(Post post) throws IllegalOrphanException, NonexistentEntityException, Exception;
//
//    Post findPost(Integer id);
//
//    List<Post> findPostEntities();
//
//    List<Post> findPostEntities(int maxResults, int firstResult);
//
//    EntityManager getEntityManager();
//
//    int getPostCount();

}
