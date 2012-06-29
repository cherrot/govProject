/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Category;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface CategoryDao extends Serializable, BaseDao<Category, Integer> {

    Category findBySlug(String slug);

    Category findByName(String name);

    /**
     *
     * @return categoryParent属性为null的对象集合
     */
    List<Category> findEntitiesHavingNullParent();

    /**
     *
     * @return categoryParent属性是顶级分类（即findEntitiesHasNullParent方法的返回结果）的对象集合
     */
    List<Category> findEntitiesHavingTopLevelParent();
}
