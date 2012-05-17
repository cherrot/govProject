/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Commentmeta;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface CommentmetaDao extends Serializable {

    void create(Commentmeta commentmeta);

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(Commentmeta commentmeta) throws NonexistentEntityException, Exception;

    Commentmeta findCommentmeta(Integer id);

    List<Commentmeta> findCommentmetaEntities();

    List<Commentmeta> findCommentmetaEntities(int maxResults, int firstResult);

    int getCommentmetaCount();

    EntityManager getEntityManager();

}
