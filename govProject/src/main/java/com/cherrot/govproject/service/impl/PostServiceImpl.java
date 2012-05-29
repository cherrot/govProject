/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.PostmetaDao;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TermRelationshipService;
import com.cherrot.govproject.service.TermService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Service
public class PostServiceImpl implements PostService{

    @Inject
    private PostDao postDao;
    @Inject
    private PostmetaDao postmetaDao;
    @Inject
    private TermService termService;
    @Inject
    private TermRelationshipService termRelationshipService;

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(Post post, List<Term> categories, List<String> tags, List<Postmeta> postmetas) {
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
    public List<Post> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Post> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(Post model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
