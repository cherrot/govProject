/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.CategoryDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service manages Category and TermTaxonomy.
 * @author cherrot
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Inject
    private CategoryDao categoryDao;

    @Override
    @Transactional
    public void create(Category category) {
        categoryDao.create(category);
    }

    @Override
    @Transactional
    public List<Category> createCategoriesByName(List<String> categoryStrings) {
        Category category = null;
        List<Category> categories = new ArrayList<Category>();
        for (String categoryString : categoryStrings) {
            try {
               category = categoryDao.findByName(categoryString);
            }
            catch (NoResultException  e) {
                category = new Category();
                category.setName(categoryString);
                category.setSlug(categoryString);
                create(category);
            }
            categories.add(category);
        }
        return categories;
    }

    @Override
    @Transactional
    public void edit(Category model) {
        //XXX: 覆盖传入实体的一对多关系，解决延时加载导致的问题。 必须假定传入实体没有修改其一对多关系集合
        Category dbModel = find(model.getId());
        model.setCategoryList(dbModel.getCategoryList());
        model.setPostList(dbModel.getPostList());
        try {
            categoryDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(CategoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(CategoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Category find(Integer id) {
        return categoryDao.find(id);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Category find(Integer id, boolean withPosts, boolean withTerms) {
        Category term = find(id);
        if (withPosts) term.getPostList().isEmpty();
        if (withTerms) term.getCategoryList().isEmpty();
        return term;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Category findBySlug(String slug, boolean withPosts, boolean withChildCategories) {
        Category category = categoryDao.findBySlug(slug);
        if (withPosts) category.getPostList().isEmpty();
        if (withChildCategories) category.getCategoryList().isEmpty();
        return category;
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            Category category = find(id);
            //子分类上升一级
            for (Category child : category.getCategoryList()) {
                child.setCategoryParent(category.getCategoryParent());
            }
            for (Post post : category.getPostList()) {
                //如果此文章只属于要删除的分类，那么将默认分类添加到该文章的分类中。
                if (post.getCategoryList().size() == 1) {
                    post.getCategoryList().add(category);
                }
            }
            categoryDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(CategoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return categoryDao.getCount();
    }

    @Override
    public List<Category> list() {
        return categoryDao.findEntities();
    }

    @Override
    public List<Category> list(int pageNum, int pageSize) {
        return categoryDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listTopLevelCategories(boolean withChildCategories) {
        List<Category> categorys = categoryDao.findEntitiesHavingNullParent();
        processDependency(categorys, false, withChildCategories);
        return categorys;
    }

    @Override
    public List<Category> listSecondLevelCategories(boolean withPosts, boolean withChildCategories) {
        List<Category> categorys = categoryDao.findEntitiesHavingTopLevelParent();
        processDependency(categorys, withPosts, withChildCategories);
        return categorys;
    }

    private void processDependency(List<Category> terms, boolean withPosts, boolean withChildCategories) {
        if (withPosts || withChildCategories) {
            for (Category term : terms) {
                if (withPosts) term.getPostList().isEmpty();
                if (withChildCategories) term.getCategoryList().isEmpty();
            }
        }
    }

    @Override
    public void save(Category model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    @Override
    public boolean isSecondLevelCategory(Category category) {
        return listTopLevelCategories(false).contains(category.getCategoryParent());
    }

    @Override
    public boolean isTopLevelCategory(Category category) {
        return category.getCategoryParent() == null;
    }

    @Override
    public Category getDefaultCategory() {
        //PENDING: 本方法将返回第一个二级分类
        return listSecondLevelCategories(false, false).get(0);
    }
}
