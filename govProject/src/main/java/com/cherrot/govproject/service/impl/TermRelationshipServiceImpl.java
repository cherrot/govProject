/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TermRelationshipDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.TermRelationship;
import com.cherrot.govproject.model.TermRelationshipPK;
import com.cherrot.govproject.service.TermRelationshipService;
import com.cherrot.util.Constants;
import com.cherrot.util.pagination.Page;
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
public class TermRelationshipServiceImpl implements TermRelationshipService{

    @Inject
    private TermRelationshipDao termRelationshipDao;

    @Override
    @Transactional
    public void create(TermRelationship model) {
        termRelationshipDao.create(model);
    }

    @Override
    public TermRelationship find(TermRelationshipPK id) {
        return termRelationshipDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(TermRelationshipPK id) {
        try {
            termRelationshipDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TermRelationshipServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TermRelationshipServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return termRelationshipDao.getCount();
    }

    @Override
    public List<TermRelationship> list() {
        return termRelationshipDao.findEntities();
    }

    @Override
    public Page<TermRelationship> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    public Page<TermRelationship> list(int pageNum, int pageSize) {
        List<TermRelationship> termRelationships =
                termRelationshipDao.findEntities(pageSize, (pageNum-1)*pageSize);
        return PageUtil.getPage(getCount(), pageNum, termRelationships, pageSize);
    }

    @Override
    @Transactional
    public void edit(TermRelationship model) {
        try {
            termRelationshipDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TermRelationshipServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TermRelationshipServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(TermRelationshipServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
