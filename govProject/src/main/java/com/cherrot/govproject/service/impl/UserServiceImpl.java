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
public class UserServiceImpl implements UserService{

    @Inject
    private UserDao userDao;

    @Inject
    private UsermetaDao usermetaDao;

    @Override
//    @Transactional
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    @Transactional
    public void create(User user, List<Usermeta> usermetas) {
        userDao.create(user);
        for (Usermeta usermeta : usermetas) {
            usermeta.setUser(user);
            usermetaDao.create(usermeta);
        }
    }

    @Override
    @Transactional(readOnly=true)
    public User find(Integer id) {
        return userDao.find(id);
    }

    @Override
    @Transactional(readOnly=true)
    public User find(Integer id, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments) {
        User user = find(id);
        processDependency(user, withSiteLogs, withPosts, withUsermetas, withComments);
        return user;
    }

    @Override
    @Transactional(readOnly=true)
    public User findByLoginName(String loginName, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments) {
        User user = userDao.findByLogin(loginName);
        processDependency(user, withSiteLogs, withPosts, withUsermetas, withComments);
        return user;
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            userDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return userDao.getCount();
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> list() {
        return userDao.findEntities();
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> list(int pageNum) {
        return list(pageNum, Constants.DEFAULT_PAGE_SIZE);
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> list(int pageNum, int pageSize) {
        return userDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(User model) {
        try {
            userDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processDependency(User user, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments) {
        if (withSiteLogs) user.getSiteLogList().isEmpty();
        if (withPosts) user.getPostList().isEmpty();
        if (withUsermetas) user.getUsermetaList().isEmpty();
        if (withComments) user.getCommentList().isEmpty();
    }

    //FIXME 未完成方法
    @Override
    public User validateUser(String loginName, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
