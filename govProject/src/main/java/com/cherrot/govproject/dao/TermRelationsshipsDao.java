/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.dao.exceptions.PreexistingEntityException;
import com.cherrot.govproject.model.TermRelationships;
import com.cherrot.govproject.model.TermRelationshipsPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface TermRelationsshipsDao extends Serializable {

    void create(TermRelationships termRelationships) throws PreexistingEntityException, Exception;

    void destroy(TermRelationshipsPK id) throws NonexistentEntityException;

    void edit(TermRelationships termRelationships) throws NonexistentEntityException, Exception;

    TermRelationships findTermRelationships(TermRelationshipsPK id);

    List<TermRelationships> findTermRelationshipsEntities();

    List<TermRelationships> findTermRelationshipsEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getTermRelationshipsCount();

}
