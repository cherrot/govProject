/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.OptionDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Option;
import com.cherrot.govproject.service.OptionService;
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
public class OptionServiceImpl implements OptionService{

    @Inject
    private OptionDao optionDao;


    @Override
    @Transactional
    public void create(Option option) {
        optionDao.create(option);
    }

    @Override
//    @Transactional(readOnly=true)
    public Option find(Integer id) {
        return optionDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            optionDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(OptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(OptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return optionDao.getCount();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Option> list() {
        return optionDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Option> list(int pageNum, int pageSize) {
        return optionDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Option model) {
        try {
            optionDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(OptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(OptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(OptionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(Option model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

}
