/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.PostService;
import com.cherrot.util.pagination.Page;
import java.util.List;

/**
 *
 * @author cherrot
 */
public class PostServiceImpl implements PostService{

    @Override
    public void create(Post user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User find(Integer id) {
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

}
