/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Comment;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface CommentDao extends Serializable, BaseDao<Comment, Integer> {

//    void create(Comment comment);
//
//    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;
//
//    void edit(Comment comment) throws IllegalOrphanException, NonexistentEntityException, Exception;
//
//    Comment findComment(Integer id);
//
//    List<Comment> findCommentEntities();
//
//    List<Comment> findCommentEntities(int maxResults, int firstResult);
//
//    int getCommentCount();
//
//    EntityManager getEntityManager();
      List<Comment> findEntitiesByUserId(Integer userId, int maxResults, int firstResult);
      List<Comment> findEntitiesByUserIdDesc(Integer userId, int maxResults, int firsResult);
      int getCountByUser(Integer userId);
}
