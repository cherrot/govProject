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
import com.cherrot.govproject.service.TermRelationshipService;
import com.cherrot.govproject.service.TermService;
import com.cherrot.util.Constants;
import com.cherrot.util.pagination.Page;
import com.cherrot.util.pagination.PageUtil;
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
    @Inject
    private TermRelationshipService termRelationshipService;

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags) {
        postDao.create(post);
        for (Term term : categories) {
            term.setTermList(categories);
            termService.create(term);
        }
        termService.createTagsByName(tags);
    }

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags, List<Postmeta> postmetas) {
        postDao.create(post);
        for (Term term : categories) {
            term.setTermList(categories);
            termService.create(term);
            
        }
        termService.createTagsByName(tags);
        for (Postmeta postmeta : postmetas) {
            postmeta.setPost(post);
            postmetaDao.create(postmeta);
        }
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
    public Page<Post> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public Page<Post> list(int pageNum, int pageSize) {
        List<Post> posts = postDao.findEntities(pageSize, (pageNum-1)*pageSize);
        return PageUtil.getPage(getCount(), pageNum, posts, pageSize);
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

}
