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
    void addTermList(Post post, List<Term> termList);
}
