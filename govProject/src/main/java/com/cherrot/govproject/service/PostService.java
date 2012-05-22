/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.TermTaxonomy;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostService extends BaseService<Post, Integer> {

    void create(Post post, List<TermTaxonomy> categories, List<String> tags);
    void create(Post post, List<TermTaxonomy> categories, List<String> tags, List<Postmeta> postmetas);
}
