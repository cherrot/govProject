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
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
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
    public void create(Category term) {
        categoryDao.create(term);
    }    

    @Override
    @Transactional
    public List<Category> createCategoriesByName(List<String> categories) {
        Category term = null;
        List<Category> terms = new ArrayList<Category>();
        for (String categoryString : categories) {
            try {
               term = categoryDao.findByName(categoryString);
            }
            catch (NoResultException  e) {
                term = new Category();
                term.setName(categoryString);
                term.setSlug(categoryString);
                create(term);
            }
            terms.add(term);
        }
        return terms;
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
//    @Transactional(readOnly=true)
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
//    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> list() {
        return categoryDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Category> list(int pageNum) {
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Category> list(int pageNum, int pageSize) {
        return categoryDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    private void processDependency(List<Category> terms, boolean withPosts, boolean withTerms) {
        if (withPosts || withTerms) {
            for (Category term : terms) {
                if (withPosts) term.getPostList().isEmpty();
                if (withTerms) term.getCategoryList().isEmpty();
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
