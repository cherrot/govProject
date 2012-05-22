/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.TermTaxonomy;
import com.cherrot.govproject.service.PostService;
import com.cherrot.util.pagination.Page;
import java.util.List;

/**
 *
 * @author cherrot
 */
public class PostServiceImpl implements PostService{

    @Override
    public void create(Post post, List<TermTaxonomy> categories, List<String> tags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(Post post, List<TermTaxonomy> categories, List<String> tags, List<Postmeta> postmetas) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(Post model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Post find(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void destroy(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Post> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Post> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Post> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(Post model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
