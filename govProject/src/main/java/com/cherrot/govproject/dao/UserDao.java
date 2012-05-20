/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.User;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface UserDao extends BaseDao<User, Integer> {

//    void create(User user);
//
//    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;
//
//    void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception;
//
//    User findUser(Integer id);
//
//    List<User> findUserEntities();
//
//    List<User> findUserEntities(int maxResults, int firstResult);
//
//    EntityManager getEntityManager();
//
//    int getUserCount();

}
