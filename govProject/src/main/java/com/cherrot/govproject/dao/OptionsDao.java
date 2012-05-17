/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Options;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface OptionsDao extends Serializable {

    void create(Options options);

    void destroy(Integer id) throws NonexistentEntityException;

    void edit(Options options) throws NonexistentEntityException, Exception;

    Options findOptions(Integer id);

    List<Options> findOptionsEntities();

    List<Options> findOptionsEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getOptionsCount();

}
