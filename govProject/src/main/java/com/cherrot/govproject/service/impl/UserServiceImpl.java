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
public class UserServiceImpl implements UserService{

    @Inject
    private UserDao userDao;
    @Inject
    private UsermetaDao usermetaDao;
    @Inject
    private SiteLogService siteLogService;

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
            usermeta.setUser(user);
            usermetaDao.create(usermeta);
        }
        siteLogService.create(user, "创建用户" + user.getLogin() + " 。ID: "+user.getId());
    }

    @Override
//    @Transactional(readOnly=true)
    public User find(Integer id) {
        return userDao.find(id);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public User find(Integer id, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments) {
        User user = find(id);
        processDependency(user, withSiteLogs, withPosts, withUsermetas, withComments);
        return user;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public User findByLoginName(String loginName, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments) {
        User user = userDao.findByLogin(loginName);
        processDependency(user, withSiteLogs, withPosts, withUsermetas, withComments);
        return user;
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        User user = userDao.find(id);
        siteLogService.create(user, "用户 " + user.getLogin()+" 被删除。ID: "+user.getId());
        try {
            userDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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
    public List<User> list(int pageNum, int pageSize) {
        return userDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(User model) {
        //TODO: 最好在web层解决掉这些问题。
//        User dbUser = userDao.find(model.getId());
//        if (model.getCommentList() == null) model.setCommentList(dbUser.getCommentList());
//        if (model.getPostList() == null) model.setPostList(dbUser.getPostList());
//        if (model.getSiteLogList() == null) model.setSiteLogList(dbUser.getSiteLogList());
//        if (model.getUsermetaList() == null) model.setUsermetaList(dbUser.getUsermetaList());
        try {
            userDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        siteLogService.create(model, "用户 " + model.getLogin() +" (ID:" + model.getId() + ") 更新了个人资料");
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

        }
        return user;
    }

    @Override
    public void save(User model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    //使用二进制位表示用户级别。允许用户有多个级别。
    @Override
    public String getDescriptionOfUserLevel(int userLevel) {
        StringBuilder stringBuilder = new StringBuilder();
        if ( (userLevel & 0xE0) > 0 ) stringBuilder.append("宣传部管理员 ");
        if ( (userLevel & 0x1E) > 0 ) stringBuilder.append("文联工作人员 ");
        if ( (userLevel & 0x1) > 0 ) stringBuilder.append("普通用户 ");
        return stringBuilder.toString();
    }
}
