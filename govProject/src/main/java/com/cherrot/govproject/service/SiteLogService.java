/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service;

import com.cherrot.govproject.model.SiteLog;
import com.cherrot.govproject.model.User;

/**
 *
 * @author cherrot
 */
public interface SiteLogService extends BaseService<SiteLog, Integer> {

    void create(User user, String logOperation);
}
