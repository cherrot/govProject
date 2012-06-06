/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.model.SiteLog;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.SiteLogService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Service
public class SiteLogServiceImpl implements SiteLogService{

    /**
     * FIXME 此类未完成
     * @param user
     * @param logOperation
     */
    @Override
    @Transactional
    public void create(User user, String logOperation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void create(SiteLog model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void edit(SiteLog model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly=true)
    public SiteLog find(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly=true)
    public List<SiteLog> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly=true)
    public List<SiteLog> list(int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly=true)
    public List<SiteLog> list(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
