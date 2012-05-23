/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.LinkDao;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.TermRelationshipService;
import com.cherrot.govproject.service.TermService;
import com.cherrot.util.pagination.Page;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

/**
 *
 * @author cherrot
 */
@Service
public class LinkServiceImpl implements LinkService{

    @Inject
    private TermRelationshipService termRelationshipService;
    @Inject
    private LinkDao linkDao;
    @Inject
    private TermService termService;

    @Override
    public void create(Link link, List<Term> categories) {

    }

    @Override
    public void create(Link model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Link find(Integer id) {
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
    public List<Link> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Link> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<Link> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(Link model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
