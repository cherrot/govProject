/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Term;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TermDao extends Serializable, BaseDao<Term, Integer> {

    Term findByNameAndType(String name, Term.TermType type);
    Term findBySlug(String slug);
    List<Term> findEntitiesByType(Term.TermType type);
    List<Term> findEntitiesByType(Term.TermType type, int maxResults, int firstResult);
    List<Term> findEntitiesByTypeOrderByCount(Term.TermType type);
    List<Term> findEntitiesByTypeOrderByCount(Term.TermType type, int maxResults, int firstResult);

}
