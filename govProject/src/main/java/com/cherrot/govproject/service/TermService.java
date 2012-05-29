/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.Term.TermType;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TermService extends BaseService<Term, Integer> {

    /**
     * Create Term and TermTaxonomy objects. Only the "type" property of the
     * TermTaxonomy object is set.
     * @param term Term(tag, category, etc) object which would be created ()
     * @param type "type" property of the TermTaxonomy object
     */
    List<Term> createTagsByName(List<String> tags);
    List<Term> createCategoriesByName(List<String> categories);
}
