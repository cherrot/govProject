/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Term;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostService extends BaseService<Post, Integer> {

    void create(Post post, List<Term> categories, List<String> tags);
    void create(Post post, List<Term> categories, List<String> tags, List<Postmeta> postmetas);
    Post find(Integer id, boolean withComments, boolean withPostmetas, boolean withTerms);
//    /**
//     * 只得到Post的引用，不取回post。主要用于验证该slug是否存在（通过捕获EntityNotFoundException）。
//     * @param slug post的slug（用户自定义的文章链接）
//     * @return Post的引用
//     */
//    Post getReferenceBySlug(String slug);
    Post findBySlug(String slug, boolean withComments, boolean withPostmetas, boolean withTerms);
    /**
     * 向指定的文章(post)添加文章分类/文章标签（关键字）。注意，此方法不能覆盖post原有的term关联
     * @param post 被操作的post对象
     * @param term 要关联post的文章分类/标签
     */
    void addTerm(Post post, Term term);
    /**
     * 向指定的文章(post)添加文章分类/文章标签（关键字）。注意，此方法不能覆盖post原有的term关联
     * @param post 被操作的post对象
     * @param termList 要关联post的文章分类/标签列表
     */
    void addTermList(Post post, List<Term> terms);
    void removeTerm(Post post, Term term);
    void removeTermList(Post post, List<Term> terms);
    /**
     * 根据文章分类取出对应文章。
     * TODO： 注意只取回文章，不取回附件（即只取出type为POST的记录），目前的实现忘记筛选类型了。
     * @param term
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Post> listNewestPostsByTerm(Term term, int pageNum, int pageSize);
    List<Post> listNewestPostsByCategoryName(String categoryName, int pageNum, int pageSize);
    /**
     * 根据用户的id取出用户发表的文章。 注意只取回文章，不取回附件（即只取出type为POST的记录）
     * @param userId 文章所属用户的id
     * @param pageNum
     * @return
     */
    List<Post> listByUser(Integer userId, int pageNum);
    List<Post> listByUser(Integer userId, int pageNum, int pageSize);
}
