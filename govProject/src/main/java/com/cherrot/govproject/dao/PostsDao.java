/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Posts;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface PostsDao extends Serializable {

    void create(Posts posts);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Posts posts) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Posts findPosts(Integer id);

    List<Posts> findPostsEntities();

    List<Posts> findPostsEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getPostsCount();

}
