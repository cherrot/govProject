/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.model.Usermeta;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface UserService extends BaseService<User, Integer> {

    void create(User user, List<Usermeta> usermetas);
    User find(Integer id, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments);
    User findByLoginName(String loginName, boolean withSiteLogs, boolean withPosts, boolean withUsermetas, boolean withComments);
    /**
     * 验证用户名、密码是否合法
     * @param user
     * @return 如果用户验证成功，则返回对应的User对象，否则返回null
     */
    User validateUser(String loginName, String password);
    /**
     * 根据用户的userLevel(用户级别)计算用户的角色属性并返回其描述
     * @param userLevel 用户级别，定义在User中
     * @return  用户角色的描述（如“系统管理员”、“文联官员”之类）
     */
    String getDescriptionOfUserLevel(int userLevel);
}
