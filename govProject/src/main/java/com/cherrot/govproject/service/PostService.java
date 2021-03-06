/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import java.io.File;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface PostService extends BaseService<Post, Integer> {

    void create(Post post, List<Category> categories, List<String> tags);

    void create(Post post, List<Category> categories, List<String> tags, List<Postmeta> postmetas);

    Post find(Integer id, boolean withComments, boolean withPostmetas, boolean withCategories, boolean withTags);

    Post findBySlug(String slug, boolean withComments, boolean withPostmetas, boolean withCategories,
        boolean withTags);

    /**
     * 向指定的文章(post)添加文章分类/文章标签（关键字）。此方法不会覆盖post原有的term关联
     *
     * @param post 被操作的post对象
     * @param term 要关联post的文章分类/标签
     */
    void addCategory(Post post, Category term);

    /**
     * 向指定的文章(post)添加文章分类/文章标签（关键字）。此方法不会覆盖post原有的term关联
     *
     * @param post 被操作的post对象
     * @param termList 要关联post的文章分类/标签列表
     */
    void addCategoryList(Post post, List<Category> categories);

    void removeCategory(Post post, Category category);

    void removeCategoryList(Post post, List<Category> categories);

    void addTag(Post post, Tag tag);

    void addTagList(Post post, List<Tag> tags);

    void removeTag(Post post, Tag tag);

    void removeTagList(Post post, List<Tag> tags);

    List<Post> listNewestPosts(int pageNum, int pageSize, boolean withComments, boolean withPostmetas,
        boolean withCategories, boolean withTags);

    /**
     * 根据文章分类取出对应文章。
     *
     * @param term
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Post> listNewestPostsByCategory(Category category, int pageNum, int pageSize);

    List<Post> listNewestPostsByCategorySlug(String categoryName, int pageNum, int pageSize);

    List<Post> listNewestPostsByTag(Tag tag, int pageNum, int pageSize);

    List<Post> listNewestPostsByTagSlug(String tagSlug, int pageNum, int pageSize);

    /**
     * @param userId 文章所属用户的id
     * @param pageNum
     * @return
     */
    List<Post> listByUser(User user, int pageNum, int pageSize);

    List<Post> listNewestPostsByUser(User user, int pageNum, int pageSize);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Post> listNewstPostsByType(Post.PostType type, int pageNum, int pageSize);
    //List<Post> listNewestImagePosts(int pageNum, int pageSize);

    int getCountByUser(User user);

    int getCountByCategory(Category category);

    int getCountByTag(Tag tag);

    int getCountByType(Post.PostType type);

    void addAttachment(Integer postId, File localFile, String mime);

}
