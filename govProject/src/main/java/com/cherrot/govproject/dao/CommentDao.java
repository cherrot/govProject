/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.User;
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
      List<Comment> findEntitiesByUser(User user, int maxResults, int firstResult);
      List<Comment> findEntitiesByUserDesc(User user, int maxResults, int firsResult);
      List<Comment> findEntitiesByApprovedDesc(boolean approved, int maxResults, int firstResult);
      int getCountByUser(User user);
      int getCountByApproved(boolean approved);
}
