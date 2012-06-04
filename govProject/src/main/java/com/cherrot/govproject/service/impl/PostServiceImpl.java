/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.PostmetaDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TermService;
import com.cherrot.govproject.util.Constants;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags) {
        create(post, categories, tags, null);
    }

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags, List<Postmeta> postmetas) {
        post.setTermList(categories);
        post.setPostmetaList(postmetas);
        List<Term> tagTerms = termService.createTagsByName(tags);
        addTermList(post, tagTerms);
        postDao.create(post);
    }

    @Override
    @Transactional
    public void create(Post post) {
        postDao.create(post);
    }

    @Override
    public Post find(Integer id) {
        return postDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            postDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return postDao.getCount();
    }

    @Override
    public List<Post> list() {
        return postDao.findEntities();
    }

    @Override
    public List<Post> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Post> list(int pageNum, int pageSize) {
        return postDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Post model) {
        try {
            postDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addTerm(Post post, Term term) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addTermList(Post post, List<Term> termList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
