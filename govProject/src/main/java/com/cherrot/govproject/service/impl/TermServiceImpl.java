/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.TermTaxonomyDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermTaxonomy;
import com.cherrot.govproject.model.TermTaxonomy.TermType;
import com.cherrot.govproject.service.TermService;
import com.cherrot.util.pagination.Page;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service manages Term and TermTaxonomy.
 * @author cherrot
 */
public class TermServiceImpl implements TermService {

    @Inject
    private TermDao termDao;
    @Inject
    private TermTaxonomyDao termTaxonomyDao;

    @Override
    @Transactional
    public void create(Term term, TermType type) {
        termDao.create(term);
        TermTaxonomy termTaxonomy = new TermTaxonomy();
        termTaxonomy.setType(type);
        termTaxonomy.setTermId(term);
        termTaxonomyDao.create(termTaxonomy);
    }

    @Override
    @Transactional
    public void createOrEditTags(List<String> tags) {
        for (String tag : tags) {
            List<Term> terms = termDao.findEntitiesByName(tag);
            Term term = null;
            if (terms.isEmpty()) {
                term = new Term();
                term.setName(tag);
                termDao.create(term);
            } else {
                term = terms.get(0);
            }
            TermTaxonomy termTaxonomy = new TermTaxonomy();
            termTaxonomy.setType(TermType.POST_TAG);
            termTaxonomy.setTermId(term);
            termTaxonomyDao.create(termTaxonomy);
        }
    }

    @Override
    @Transactional
    public void createOrEditCategories(List<String> categories) {
        for (String catagory : categories) {
            List<Term> terms = termDao.findEntitiesByName(catagory);
            Term term = null;
            if (terms.isEmpty()) {
                term = new Term();
                term.setName(catagory);
                termDao.create(term);
            } else {
                term = terms.get(0);
            }
            TermTaxonomy termTaxonomy = new TermTaxonomy();
            termTaxonomy.setType(TermType.CATEGORY);
            termTaxonomy.setTermId(term);
            termTaxonomyDao.create(termTaxonomy);
        }
    }

    /**
     * Create a Term which is a POST_TAG;
     * @param model
     */
    @Override
    public void create(Term model) {
        create(model, TermType.POST_TAG);
    }

    @Override
    @Transactional
    public void edit(Term model) {
        try {
            termDao.edit(model);
        }
        catch (IllegalOrphanException | NonexistentEntityException ex) {
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
        catch (IllegalOrphanException | NonexistentEntityException ex) {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Term> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
