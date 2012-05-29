/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.UserService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author cherrot
 */
@Controller
@RequestMapping("/user/")
public class RegisterController extends BaseController {

    @Inject
    UserService userService;

    @RequestMapping("doRegister")
    public String register(HttpServletRequest request, User user) {
        userService.create(user);
        setSessionUser(request.getSession(), user);
        return "/home";
    }
}
