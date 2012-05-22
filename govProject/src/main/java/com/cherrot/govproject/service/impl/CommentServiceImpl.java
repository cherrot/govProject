/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.util.pagination.Page;
import java.util.List;

/**
 *
 * @author cherrot
 */
public class CommentServiceImpl implements CommentService{

    @Override
    public void create(Comment comment, List<Commentmeta> commentmetas) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(Comment model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Comment find(Integer id) {
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
    public List<Comment> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Comment> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Comment> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(Comment model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
