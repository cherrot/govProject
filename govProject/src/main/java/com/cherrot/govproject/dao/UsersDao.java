/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Users;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface UsersDao extends Serializable {

    void create(Users users);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Users findUsers(Integer id);

    List<Users> findUsersEntities();

    List<Users> findUsersEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getUsersCount();

}
