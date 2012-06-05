/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Term;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostDao extends Serializable, BaseDao<Post, Integer> {

    List<Post> findEntitiesByTermOrderbyCreateDate(Term term, int maxResults, int firstResult);
    List<Post> findEntitiesByCategoryNameOrderbyCreateDate(String categoryName, int maxResults, int firstResult);
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
