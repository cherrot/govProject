/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Category;
import java.io.Serializable;

/**
 *
 * @author cherrot
 */
public interface CategoryDao extends Serializable, BaseDao<Category, Integer> {

    Category findBySlug(String slug);
    Category findByName(String name);
}
