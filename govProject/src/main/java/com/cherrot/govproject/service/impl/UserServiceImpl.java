/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.UserDao;
import com.cherrot.govproject.dao.UsermetaDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.model.Usermeta;
import com.cherrot.govproject.service.UserService;
import com.cherrot.util.pagination.Page;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
public class UserServiceImpl implements UserService{

    @Inject
    private UserDao userDao;

    @Inject
    private UsermetaDao usermetaDao;

    @Override
    @Transactional
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    @Transactional
    public void create(User user, List<Usermeta> usermetas) {
        userDao.create(user);
        for (Usermeta usermeta : usermetas) {
            usermeta.setUserId(user);
            usermetaDao.create(usermeta);
        }
    }

    @Override
    public User find(Integer id) {
        return userDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            userDao.destroy(id);
        }
        catch (IllegalOrphanException | NonexistentEntityException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return userDao.getCount();
    }

    @Override
    public List<User> list() {
        return userDao.findEntities();
    }

    @Override
    public Page<User> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Page<User> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void edit(User model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
