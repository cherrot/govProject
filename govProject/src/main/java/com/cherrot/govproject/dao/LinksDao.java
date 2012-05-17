/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Links;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface LinksDao extends Serializable {

    void create(Links links);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(Links links) throws IllegalOrphanException, NonexistentEntityException, Exception;

    Links findLinks(Integer id);

    List<Links> findLinksEntities();

    List<Links> findLinksEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getLinksCount();

}
