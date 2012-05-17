/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Usermeta;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface UsermetaDao extends Serializable {

    void create(Usermeta usermeta);

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(Usermeta usermeta) throws NonexistentEntityException, Exception;

    Usermeta findUsermeta(Integer id);

    List<Usermeta> findUsermetaEntities();

    List<Usermeta> findUsermetaEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getUsermetaCount();

}
