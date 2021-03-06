/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.SiteLogDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.SiteLog;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.SiteLogService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cherrot
 */
@Service
public class SiteLogServiceImpl implements SiteLogService {

    @Inject
    private SiteLogDao siteLogDao;

    @Override
    @Transactional
    public void create(SiteLog model) {
        siteLogDao.create(model);
    }

    @Override
    @Transactional
    public void create(User user, String logOperation) {
        SiteLog siteLog = new SiteLog();
        siteLog.setUser(user);
        siteLog.setLogOperation(logOperation);
        siteLogDao.create(siteLog);
    }

    @Override
    @Transactional
    public void edit(SiteLog model) {
        try {
            siteLogDao.edit(model);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(SiteLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SiteLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SiteLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SiteLog find(Integer id) {
        return siteLogDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            siteLogDao.destroy(id);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(SiteLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SiteLogServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return siteLogDao.getCount();
    }

    @Override
    public List<SiteLog> list() {
        return siteLogDao.findEntities();
    }

    @Override
    public List<SiteLog> list(int pageNum, int pageSize) {
        return siteLogDao.findEntities(pageSize, (pageNum - 1) * pageSize);
    }

    @Override
    public void save(SiteLog model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }
}
