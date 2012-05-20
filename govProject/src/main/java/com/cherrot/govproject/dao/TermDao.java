/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface TermDao extends BaseDao<Term, Integer> {

//    void create(Term term);
//
//    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;
//
//    void edit(Term term) throws IllegalOrphanException, NonexistentEntityException, Exception;
//
//    Term findTerm(Integer id);
//
//    List<Term> findTermEntities();
//
//    List<Term> findTermEntities(int maxResults, int firstResult);
//
//    EntityManager getEntityManager();
//
//    int getTermCount();

}
