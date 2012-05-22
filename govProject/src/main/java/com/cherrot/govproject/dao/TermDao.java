/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Term;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TermDao extends BaseDao<Term, Integer> {

    List<Term> findEntitiesByName(String name);
    Term findBySlug(String slug);
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
