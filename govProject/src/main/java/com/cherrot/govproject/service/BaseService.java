/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.User;
import com.cherrot.util.pagination.Page;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cherrot
 */
public interface BaseService<Model extends Serializable, PrimaryKey extends Serializable> {

    void create(Model model);
    void edit(Model model);
    User find(PrimaryKey id);
    void destroy(PrimaryKey id);
    int getCount();
    List<Model> list();
    Page<Model> list(int pageNum);
    Page<Model> list(int pageNum, int pageSize);
}
