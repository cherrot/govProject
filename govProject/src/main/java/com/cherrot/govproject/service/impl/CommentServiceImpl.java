/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.CommentDao;
import com.cherrot.govproject.dao.CommentmetaDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.util.Constants;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cherrot
 */
@Service
public class CommentServiceImpl implements CommentService{
    @Inject
    private CommentDao commentDao;

    @Inject
    private CommentmetaDao commentmetaDao;

    @Override
    @Transactional
    public void create(Comment comment, List<Commentmeta> commentmetas) {
        commentDao.create(comment);
        for (Commentmeta commentmeta : commentmetas) {
            commentmeta.setComment(comment);
            commentmetaDao.create(commentmeta);
        }
    }

    @Override
    @Transactional
    public void create(Comment comment) {
        commentDao.create(comment);
    }

    @Override
    public Comment find(Integer id) {
        return commentDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            commentDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(CommentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(CommentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return commentDao.getCount();
    }

    @Override
    public List<Comment> list() {
        return commentDao.findEntities();
    }

    @Override
    public List<Comment> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Comment> list(int pageNum, int pageSize) {
        return commentDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Comment model) {
        try {
            commentDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(CommentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(CommentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(CommentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
