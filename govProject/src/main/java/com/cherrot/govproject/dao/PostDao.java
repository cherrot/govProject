/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Post;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostDao extends Serializable, BaseDao<Post, Integer> {

    Post findBySlug(String slug);
    List<Post> findEntitiesByCategoryDescOrder(Integer termId, int maxResults, int firstResult);
    List<Post> findEntitiesByCategorySlugDescOrder(String categoryName, int maxResults, int firstResult);
    List<Post> findEntitiesByUserId(Integer userId, int maxResults, int firstResult);
    
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
