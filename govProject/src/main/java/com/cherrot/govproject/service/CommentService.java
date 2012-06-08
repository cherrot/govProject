/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Commentmeta;
import java.util.List;
/**
 *
 * @author cherrot
 */
public interface CommentService extends BaseService<Comment, Integer> {

    void create(Comment comment, List<Commentmeta> commentmetas);
    public Comment find(Integer id, boolean withCommentmetas, boolean withChildComments);
    public List<Comment> list(boolean withCommentmetas, boolean withChildComments);
    public List<Comment> list(int pageNum, boolean withCommentmetas, boolean withChildComments);
    public List<Comment> list(int pageNum, int pageSize, boolean withCommentmetas, boolean withChildComments);
    public List<Comment> listByPostWithHierarchyOrderbyDate();
}
