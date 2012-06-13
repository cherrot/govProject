/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.UserService;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
public class AdminPanelController {

    @Inject
    private UserService userService;

    @RequestMapping("/admin")
    public ModelAndView adminPanel(HttpSession session) {
        ModelAndView mav = new ModelAndView("/admin/adminPanel");
        User user = BaseController.getSessionUser(session);
        String userRole = userService.getDescriptionOfUserLevel(user.getUserLevel());
        mav.addObject("userRole", userRole);
        return mav;
    }
}
