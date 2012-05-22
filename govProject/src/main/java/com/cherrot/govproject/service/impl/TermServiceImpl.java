/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.TermTaxonomyDao;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermTaxonomy;
import com.cherrot.govproject.model.TermTaxonomy.TermType;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.TermService;
import com.cherrot.util.pagination.Page;
import java.util.List;
import javax.inject.Inject;

/**
 * This service manages Term and TermTaxonomy.
 * @author cherrot
 */
public class TermServiceImpl implements TermService{

    @Inject
    private TermDao termDao;
    @Inject
    private TermTaxonomyDao termTaxonomyDao;

    @Override
    public void create(Term term, TermType type) {
        termDao.create(term);
        TermTaxonomy termTaxonomy = new TermTaxonomy();
        termTaxonomy.setType(type);
        termTaxonomy.setTermId(term);
        termTaxonomyDao.create(termTaxonomy);
    }

    @Override
    public void createTags(List<String> tags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createCatagories(List<String> catagories) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void create(Term model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(Term model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User find(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void destroy(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Term> list() {
        throw new UnsupportedOperationException("Not supported yet.");
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
