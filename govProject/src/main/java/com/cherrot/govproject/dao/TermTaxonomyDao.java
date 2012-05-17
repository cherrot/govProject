/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.TermTaxonomy;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author cherrot
 */
public interface TermTaxonomyDao extends Serializable {

    void create(TermTaxonomy termTaxonomy);

    void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException;

    void edit(TermTaxonomy termTaxonomy) throws IllegalOrphanException, NonexistentEntityException, Exception;

    TermTaxonomy findTermTaxonomy(Integer id);

    List<TermTaxonomy> findTermTaxonomyEntities();

    List<TermTaxonomy> findTermTaxonomyEntities(int maxResults, int firstResult);

    EntityManager getEntityManager();

    int getTermTaxonomyCount();

}
