/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.UserService;
import java.util.Date;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @ModelAttribute("user")
    public User getUser() {
        return new User(null, null, 0, new Date(), "", "");
    }

    @RequestMapping("doRegister")
    public String register(HttpServletRequest request, @Valid @ModelAttribute("user")User user) {
        user.setDisplayName(user.getLogin().split("@")[0]);
        user.setEmail(user.getLogin());
        userService.create(user);
        setSessionUser(request.getSession(), user);
        return "redirect:/";
    }
}
