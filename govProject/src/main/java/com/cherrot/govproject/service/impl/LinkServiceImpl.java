/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.LinkCategoryDao;
import com.cherrot.govproject.dao.LinkDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.service.LinkService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Service
public class LinkServiceImpl implements LinkService{

    @Inject
    private LinkDao linkDao;
    @Inject
    private LinkCategoryDao linkCategoryDao;

    @Override
    @Transactional
    public void create(Link model) {
        linkDao.create(model);
    }

    @Override
//    @Transactional(readOnly=true)
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
//    @Transactional(readOnly=true)
    public List<Link> list() {
        return linkDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
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

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public LinkCategory findLinkCategory(Integer linkCategoryId, boolean withLinks) {
        LinkCategory linkCategory = linkCategoryDao.find(linkCategoryId);
        if(withLinks){
            linkCategory.getLinkList().isEmpty();
        }
        return linkCategory;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<LinkCategory> listLinkCategories(boolean withLinks) {
        List<LinkCategory> linkCategories = linkCategoryDao.findEntities();
        if(withLinks){
            for (LinkCategory linkCategory : linkCategories) {
                linkCategory.getLinkList().isEmpty();
            }
        }
        return linkCategories;
    }

    @Override
    public void save(Link model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    @Override
    public void createLinkCategory(LinkCategory linkCategory) {
        linkCategoryDao.create(linkCategory);
    }

}
