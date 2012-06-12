/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.SiteLogService;
import com.cherrot.govproject.service.TermService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
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
public class PostServiceImpl implements PostService {

    @Inject
    private PostDao postDao;
    @Inject
    private TermService termService;
    @Inject
    private SiteLogService siteLogService;

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags) {
        create(post, categories, tags, null);
        siteLogService.create(post.getUser(),post.getTitle()+"被创建" );
    }

    @Override
    @Transactional
    public void create(Post post, List<Term> categories, List<String> tags, List<Postmeta> postmetas) {
        post.setTermList(categories);
        post.setPostmetaList(postmetas);
        List<Term> tagTerms = termService.createTagsByName(tags);
        addTermList(post, tagTerms);
        postDao.create(post);
        siteLogService.create(post.getUser(),post.getTitle()+"被创建" );
    }

    @Override
    @Transactional
    public void create(Post post) {
        postDao.create(post);
        siteLogService.create(post.getUser(),post.getTitle()+"被创建" );
    }

    @Override
    @Transactional(readOnly=true)
    public Post find(Integer id) {
        return postDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        Post post = postDao.find(id);
        siteLogService.create(post.getUser(), post.getTitle()+"被删除" );
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
    @Transactional(readOnly=true)
    public List<Post> list() {
        return postDao.findEntities();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Post> list(int pageNum) {
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Post> list(int pageNum, int pageSize) {
        return postDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Post model) {
        siteLogService.create(model.getUser(), model.getTitle()+"被修改" );
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
    @Transactional
    public void addTerm(Post post, Term term) {
        post.getTermList().add(term);
    }

    @Override
    @Transactional
    public void addTermList(Post post, List<Term> terms) {
        post.getTermList().addAll(terms);
    }

    @Override
    @Transactional
    public void removeTerm(Post post, Term term) {
        post.getTermList().remove(term);
    }

    @Override
    @Transactional
    public void removeTermList(Post post, List<Term> terms) {
        post.getTermList().removeAll(terms);
    }

    @Override
    @Transactional(readOnly=true)
    public Post find(Integer id, boolean withComments, boolean withPostmetas, boolean withTerms) {
        Post post = find(id);
        processDependency(post, withComments, withPostmetas, withTerms);
        return post;
    }

    @Override
    @Transactional(readOnly=true)
    public Post findBySlug(String slug, boolean withComments, boolean withPostmetas, boolean withTerms) {
        Post post = postDao.findBySlug(slug);
        processDependency(post, withComments, withPostmetas, withTerms);
        return post;
    }

    @Override
    @Transactional(readOnly=true)
    public List<Post> listNewestPostsByTerm(Term term, int pageNum, int pageSize) {
        return postDao.findEntitiesByTermOrderbyCreateDate(term, pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Post> listNewestPostsByCategoryName(String categoryName, int pageNum, int pageSize) {
        return postDao.findEntitiesByCategoryNameOrderbyCreateDate(categoryName, pageSize, (pageNum-1)*pageSize);
    }

    private void processDependency(Post post, boolean withComments, boolean withPostmetas, boolean withTerms){
        if (withComments) post.getCommentList().isEmpty();
        if (withPostmetas) post.getPostmetaList().isEmpty();
        if (withTerms) post.getTermList().isEmpty();
    }

    @Override
    public void save(Post model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    //FIXME 未完成
    @Override
    public List<Post> listByUser(Integer userId, int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //FIXME 未完成
    @Override
    public List<Post> listByUser(Integer userId, int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
