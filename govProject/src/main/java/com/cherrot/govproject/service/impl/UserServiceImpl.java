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
import com.cherrot.govproject.service.SiteLogService;
import com.cherrot.govproject.service.UserService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
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
    @Inject
    private SiteLogService siteLogService;

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
        siteLogService.create(user, user.getLogin() + "被创建");
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
        User user = userDao.find(id);
        siteLogService.create(user, user.getLogin()+"被删除");
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
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
    @Transactional(readOnly=true)
    public List<User> list(int pageNum, int pageSize) {
        return userDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(User model) {
        siteLogService.create(model, model.getLogin()+"被修改");
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

    @Override
    public User validateUser(String loginName, String password) {
        User user = null;
        try {
            user = findByLoginName(loginName, false, false, false, false);
        } catch (Exception e) {

        } finally {
            return user;
        }
    }

    @Override
    public void save(User model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    @Override
    public String getDescriptionOfUserLevel(int userLevel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
