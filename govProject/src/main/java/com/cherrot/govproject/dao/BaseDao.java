/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface BaseDao<Model extends Serializable, PrimaryKey extends Serializable> {

    void create(Model model);
    void destroy(PrimaryKey id) throws IllegalOrphanException, NonexistentEntityException;
    void edit(Model model) throws IllegalOrphanException, NonexistentEntityException, Exception;
    Model find(PrimaryKey id);
    List<Model> findEntities();
    List<Model> findEntities(int maxResults, int firstResult);
    int getCount();
}
