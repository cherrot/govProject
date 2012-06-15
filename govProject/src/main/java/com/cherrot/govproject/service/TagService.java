/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Category;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TagService extends BaseService<Category, Integer> {

    Category find(Integer id, boolean withPosts);
    /**
     * Create Category and TermTaxonomy objects. Only the "type" property of the
     * TermTaxonomy object is set.
     * @param term Category(tag, category, etc) object which would be created ()
     * @param type "type" property of the TermTaxonomy object
     */
    List<Category> createTagsByName(List<String> tags);
}
