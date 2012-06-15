/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Category.TermType;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TermService extends BaseService<Category, Integer> {

    Category find(Integer id, boolean withPosts, boolean withTerms);
    /**
     * Create Category and TermTaxonomy objects. Only the "type" property of the
     * TermTaxonomy object is set.
     * @param term Category(tag, category, etc) object which would be created ()
     * @param type "type" property of the TermTaxonomy object
     */
    List<Category> createTagsByName(List<String> tags);
    List<Category> createCategoriesByName(List<String> categories);

    List<Category> listByType(TermType type, boolean withPosts, boolean withTerms);
    List<Category> listByType(TermType type, int pageNum, boolean withPosts, boolean withTerms);
    List<Category> listByType(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms);
    List<Category> listByTypeOrderbyCount(TermType type, boolean withPosts, boolean withTerms);
    List<Category> listByTypeOrderbyCount(TermType type, int pageNum, boolean withPosts, boolean withTerms);
    List<Category> listByTypeOrderbyCount(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms);
}
