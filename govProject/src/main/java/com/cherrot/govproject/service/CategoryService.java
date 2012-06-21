/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Category;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface CategoryService extends BaseService<Category, Integer> {

    Category find(Integer id, boolean withPosts, boolean withChildCategories);
    Category findBySlug(String slug, boolean withPosts, boolean withChildCategories);
    /**
     * Create Category and TermTaxonomy objects. Only the "type" property of the
     * TermTaxonomy object is set.
     * @param term Category(tag, category, etc) object which would be created ()
     * @param type "type" property of the TermTaxonomy object
     */
    List<Category> createCategoriesByName(List<String> categories);
    /**
     * 返回顶级文章分类，即categoryParent为null的分类。
     * 这些分类是“伪分类”，仅用于将二级分类分组，以便在首页以分类页卡的形式显示。
     * @return 顶级分类的列表
     */
    List<Category> listTopLevelCategories(boolean withChildCategories);
    /**
     * 返回二级文章分类，即首页顶部导航栏显示的文章分类
     * @param withPosts true if 一并取出该分类下关联的文章
     * @param withChildCategories true if 一并取出该分类的子分类
     * @return
     */
    List<Category> listSecondLevelCategories(boolean withPosts, boolean withChildCategories);
}
