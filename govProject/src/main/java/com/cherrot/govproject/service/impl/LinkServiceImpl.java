/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.LinkDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.TermRelationship;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.TermRelationshipService;
import com.cherrot.govproject.service.TermService;
import com.cherrot.govproject.util.Constants;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void create(Link link, List<Term> categories) {
        linkDao.create(link);
        for(Term category : categories) {
            termRelationshipService
                    .create(new TermRelationship(link.getId(), category.getId()));
            category.setCount(category.getCount()+1);
        }
    
    }

    @Override
    @Transactional
    public void create(Link model) {
        linkDao.create(model);
    }

    @Override
    public Link find(Integer id) {
        return linkDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            linkDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(LinkServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(LinkServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return linkDao.getCount();
    }

    @Override
    public List<Link> list() {
        return linkDao.findEntities();
    }

    @Override
    public List<Link> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Link> list(int pageNum, int pageSize) {
        return linkDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Link model) {
                try {
            linkDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(LinkServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(LinkServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(LinkServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
