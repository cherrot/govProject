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
    Term findByNameAndType(String name, Term.TermType type);
    Term findBySlug(String slug);
//    void create(TermTaxonomy termTaxonomy);
//
//    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;
//
//    void edit(TermTaxonomy termTaxonomy) throws IllegalOrphanException, NonexistentEntityException, Exception;
//
//    TermTaxonomy findTermTaxonomy(Integer id);
//
//    List<TermTaxonomy> findTermTaxonomyEntities();
//
//    List<TermTaxonomy> findTermTaxonomyEntities(int maxResults, int firstResult);
//
//    EntityManager getEntityManager();
//
//    int getTermTaxonomyCount();

}
