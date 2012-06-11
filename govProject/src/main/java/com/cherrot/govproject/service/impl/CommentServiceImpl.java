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
    @Transactional(readOnly=true)
    public Comment find(Integer id) {
        return commentDao.find(id);
    }

    @Override
    @Transactional(readOnly=true)
    public Comment find(Integer id, boolean withCommentmetas, boolean withChildComments) {
        Comment comment = find(id);
        if (withCommentmetas) comment.getCommentmetaList().isEmpty();
        if (withChildComments) comment.getCommentList().isEmpty();
        return comment;
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
    @Transactional(readOnly=true)
    public List<Comment> list(boolean withCommentmetas, boolean withChildComments) {
        List<Comment> comments = commentDao.findEntities();
        processDependency(comments, withCommentmetas, withChildComments);
        return comments;
    }

    @Override
    public List<Comment> list(int pageNum,boolean withCommentmetas, boolean withChildComments) {
        return list(pageNum, DEFAULT_PAGE_SIZE, withCommentmetas, withChildComments);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Comment> list(int pageNum, int pageSize,boolean withCommentmeta, boolean withChildComments) {
        List<Comment> comments = commentDao.findEntities(pageSize, (pageNum-1)*pageSize);
        processDependency(comments, withCommentmeta, withChildComments);
        return comments;
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

    private void processDependency(List<Comment> comments, boolean withCommentmetas, boolean withChildComments) {
        if (withCommentmetas || withChildComments) {
            for (Comment comment : comments) {
                if (withCommentmetas) comment.getCommentmetaList().isEmpty();
                if (withChildComments) comment.getCommentList().isEmpty();
            }
        }
    }

    @Override
    @Transactional(readOnly=true)
    public List<Comment> list() {
        return commentDao.findEntities();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Comment> list(int pageNum) {
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Comment> list(int pageNum, int pageSize) {
        return commentDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    public void save(Comment model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }
}
