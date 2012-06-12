/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.service.UserService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @Inject
    private UserService userService;

    @RequestMapping
    public String viewUser() {
        return null;
    }

    public String viewUserList() {
        return null;
    }

    @RequestMapping(value="/delete", params="id")
    public String deleteUser(HttpServletRequest request, @RequestParam("id")Integer id) {
        userService.destroy(id);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
