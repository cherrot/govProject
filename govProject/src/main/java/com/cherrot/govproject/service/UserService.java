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
    User findByLoginName(String loginName);
}
