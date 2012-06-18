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
    Comment find(Integer id, boolean withCommentmetas, boolean withChildComments);
    List<Comment> list(boolean withCommentmetas, boolean withChildComments);
    List<Comment> list(int pageNum, int pageSize, boolean withCommentmetas, boolean withChildComments);
    /**
     * 根据用户ID取回其发表的文章评论
     * @param userId 评论所属用户的ID
     * @param pageNum 页码数
     * @return
     */
    List<Comment> listByUser(Integer userId, int pageNum, int pageSize);
    List<Comment> listNewesCommentsByUser(Integer userId, int pageNum, int pageSize);
    int getCountByUser(Integer userId);
}
