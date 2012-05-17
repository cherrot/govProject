/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Postmeta;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface PostmetaDao extends Serializable {

    void create(Postmeta postmeta);

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(Postmeta postmeta) throws NonexistentEntityException, Exception;

    Postmeta findPostmeta(Integer id);

    List<Postmeta> findPostmetaEntities();

    List<Postmeta> findPostmetaEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getPostmetaCount();

}
