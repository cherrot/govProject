/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;

import com.cherrot.govproject.model.Tag;
import java.io.Serializable;

/**
 *
 * @author cherrot
 */
public interface TagDao extends Serializable, BaseDao<Tag, Integer> {

    Tag findBySlug(String slug);
}
