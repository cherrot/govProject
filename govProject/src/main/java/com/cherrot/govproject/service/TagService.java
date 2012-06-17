/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Tag;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface TagService extends BaseService<Tag, Integer> {

    Tag find(Integer id, boolean withPosts);
    Tag findBySlug(String slug, boolean withPosts);
    /**
     * Create Category and TermTaxonomy objects. Only the "type" property of the
     * TermTaxonomy object is set.
     * @param term Category(tag, category, etc) object which would be created ()
     * @param type "type" property of the TermTaxonomy object
     */
     List<Tag> createTagsByName(List<String> tags);
}
