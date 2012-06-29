/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface BaseService<Model extends Serializable, PrimaryKey extends Serializable> {

    void create(Model model);

    void edit(Model model);

    Model find(PrimaryKey id);

    void destroy(PrimaryKey id);

    void save(Model model);

    int getCount();

    List<Model> list();

    List<Model> list(int pageNum, int pageSize);
}
