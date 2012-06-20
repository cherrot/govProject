/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.CategoryDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
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
}
