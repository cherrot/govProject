/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.UserService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cherrot
 */
@Controller
public class HomeController {

    @Inject
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("home");
//        List<User> people = userService.list();
//        mav.addObject("people", people);
        User user = userService.findByLoginName("admin@cherrot.com");
        mav.addObject("person", user);
        return mav;
    }
}
