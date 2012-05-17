/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comments;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface CommentsDao extends Serializable {

    void create(Comments comments);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Comments comments) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Comments findComments(Integer id);

    List<Comments> findCommentsEntities();

    List<Comments> findCommentsEntities(int maxResults, int firstResult);

    int getCommentsCount();

    EntityManager getEntityManager();

}
