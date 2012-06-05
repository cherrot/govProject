/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.Term.TermType;
import com.cherrot.govproject.service.TermService;
import com.cherrot.govproject.util.Constants;
import java.util.ArrayList;
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
    public List<Term> createTagsByName(List<String> tags) {
        Term term = null;
        List<Term> terms = new ArrayList<Term>();
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
            terms.add(term);
        }
        return terms;
    }

    @Override
    @Transactional
    public List<Term> createCategoriesByName(List<String> categories) {
        Term term = null;
        List<Term> terms = new ArrayList<Term>();
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
            terms.add(term);
        }
        return terms;
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
    public Term find(Integer id, boolean withPosts, boolean withTerms) {
        Term term = find(id);
        if (withPosts) term.getPostList();
        if (withTerms) term.getTermList();
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
    public List<Term> list() {
        return termDao.findEntities();
    }

    @Override
    public List<Term> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Term> list(int pageNum, int pageSize) {
        return termDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    public List<Term> listByType(TermType type, boolean withPosts, boolean withTerms) {
        List<Term> terms = termDao.findEntitiesByType(type);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    public List<Term> listByType(TermType type, int pageNum, boolean withPosts, boolean withTerms) {
        return listByType(type, pageNum, Constants.DEFAULT_PAGE_SIZE, withPosts, withTerms);
    }

    @Override
    public List<Term> listByType(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms) {
        List<Term> terms = termDao.findEntitiesByType(type, pageSize, (pageNum-1)*pageSize);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    public List<Term> listByTypeOrderByCount(TermType type, boolean withPosts, boolean withTerms) {
        List<Term> terms = termDao.findEntitiesByTypeOrderByCount(type);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    @Override
    public List<Term> listByTypeOrderByCount(TermType type, int pageNum, boolean withPosts, boolean withTerms) {
        return listByTypeOrderByCount(type, pageNum, Constants.DEFAULT_PAGE_SIZE, withPosts, withTerms);
    }

    @Override
    public List<Term> listByTypeOrderByCount(TermType type, int pageNum, int pageSize, boolean withPosts, boolean withTerms) {
        List<Term> terms = termDao.findEntitiesByTypeOrderByCount(type, pageSize, (pageNum-1)*pageSize);
        processDependency(terms, withPosts, withTerms);
        return terms;
    }

    private void processDependency(List<Term> terms, boolean withPosts, boolean withTerms) {
        if (withPosts || withTerms) {
            for (Term term : terms) {
                if (withPosts) term.getPostList();
                if (withTerms) term.getTermList();
            }
        }
    }

}
