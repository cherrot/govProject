/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Terms;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface TermsDao extends Serializable {

    void create(Terms terms);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Terms terms) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Terms findTerms(Integer id);

    List<Terms> findTermsEntities();

    List<Terms> findTermsEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getTermsCount();

}
