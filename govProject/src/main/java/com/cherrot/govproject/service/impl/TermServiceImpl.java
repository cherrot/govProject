/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.TermService;
import com.cherrot.util.Constants;
import com.cherrot.util.pagination.Page;
import com.cherrot.util.pagination.PageUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service manages Term and TermTaxonomy.
 * @author cherrot
 */
@Service
public class TermServiceImpl implements TermService {

    @Inject
    private TermDao termDao;

    @Override
    @Transactional
    public void create(Term term) {
        termDao.create(term);
    }

    /**
     * Do nothing if the tag already exists.
     * @param tags
     */
    @Override
    @Transactional
    public void createTagsByName(List<String> tags) {
        Term term = null;
        for (String tag : tags) {
            try {
               term = termDao.findByNameAndType(tag, Term.TermType.POST_TAG);
            }
            catch (NoResultException  e) {
                term = new Term();
                term.setName(tag);
                term.setSlug(tag);
                term.setType(Term.TermType.POST_TAG);
                termDao.create(term);
            }
        }
    }

    @Override
    @Transactional
    public void createCategoriesByName(List<String> categories) {
        Term term = null;
        for (String category : categories) {
            try {
               term = termDao.findByNameAndType(category, Term.TermType.CATEGORY);
            }
            catch (NoResultException  e) {
                term = new Term();
                term.setName(category);
                term.setSlug(category);
                term.setType(Term.TermType.CATEGORY);
                termDao.create(term);
            }
        }
    }

    @Override
    @Transactional
    public void edit(Term model) {
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
    public Term find(Integer id) {
        return termDao.find(id);
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
    public List<Term> list() {
        return termDao.findEntities();
    }

    @Override
    public Page<Term> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public Page<Term> list(int pageNum, int pageSize) {
        List<Term> terms = termDao.findEntities(pageSize, (pageNum-1)*pageSize);
        return PageUtil.getPage(getCount(), pageNum, terms, pageSize);
    }
}
