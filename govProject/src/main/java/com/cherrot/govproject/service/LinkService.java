/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface LinkService extends BaseService<Link, Integer> {

    List<LinkCategory> listCategories();
}
