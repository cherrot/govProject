/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.CategoryDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Category.TermType;
import com.cherrot.govproject.service.TermService;
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
public class TermServiceImpl implements TermService {

    @Inject
    private CategoryDao termDao;

    @Override
    @Transactional
    public void create(Category term) {
        termDao.create(term);
    }

    /**
     * Do nothing if the tag already exists.
     * @param tags
     */
    @Override
    @Transactional
    public List<Category> createTagsByName(List<String> tags) {
        Category term = null;
        List<Category> terms = new ArrayList<Category>();
        for (String tag : tags) {
            try {
               term = termDao.findByNameAndType(tag, Category.TermType.POST_TAG);
            }
            catch (NoResultException  e) {
                term = new Category();
                term.setName(tag);
                term.setSlug(tag);
                term.setType(Category.TermType.POST_TAG);
                termDao.create(term);
            }
            terms.add(term);
        }
        return terms;
    }

    @Override
    @Transactional
    public List<Category> createCategoriesByName(List<String> categories) {
        Category term = null;
        List<Category> terms = new ArrayList<Category>();
        for (String category : categories) {
            try {
               term = termDao.findByNameAndType(category, Category.TermType.CATEGORY);
            }
            catch (NoResultException  e) {
                term = new Category();
                term.setName(category);
                term.setSlug(category);
                term.setType(Category.TermType.CATEGORY);
                termDao.create(term);
            }
            terms.add(term);
        }
        return terms;
    }

    @Override
    @Transactional
    public void edit(Category model) {
        try {
            termDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TermServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TermServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(TermServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
//    @Transactional(readOnly=true)
    public Category find(Integer id) {
        return termDao.find(id);
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
            termDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TermServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TermServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return termDao.getCount();
    }

    @Override
//    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> list() {
        return termDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Category> list(int pageNum) {
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Category> list(int pageNum, int pageSize) {
        return termDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByType(TermType type, boolean withPosts, boolean withTerms) {
        List<Category> terms = termDao.findEntitiesByType(type);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByType(TermType type, int pageNum, boolean withPosts, boolean withTerms) {
        return listByType(type, pageNum, DEFAULT_PAGE_SIZE, withPosts, withTerms);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByType(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms) {
        List<Category> terms = termDao.findEntitiesByType(type, pageSize, (pageNum-1)*pageSize);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByTypeOrderbyCount(TermType type, boolean withPosts, boolean withTerms) {
        List<Category> terms = termDao.findEntitiesByTypeOrderbyCount(type);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByTypeOrderbyCount(TermType type, int pageNum, boolean withPosts, boolean withTerms) {
        return listByTypeOrderbyCount(type, pageNum, DEFAULT_PAGE_SIZE, withPosts, withTerms);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Category> listByTypeOrderbyCount(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms) {
        List<Category> terms = termDao.findEntitiesByTypeOrderbyCount(type, pageSize, (pageNum-1)*pageSize);
        processDependency(terms, withPosts, withTerms);
        return terms;
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
