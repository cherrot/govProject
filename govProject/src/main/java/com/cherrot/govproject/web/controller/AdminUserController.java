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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @Inject
    private UserService userService;

    @RequestMapping(params="id")
    public ModelAndView viewUser(@RequestParam("id")Integer userId) {
        ModelAndView mav = new ModelAndView("/viewUser");
        //TODO 获取User对象。查看 viewUser.jsp确定需要注入的对象。 可以参考UserController的同名方法。
        //注意此方法是给管理员用的。
        return mav;
    }

    /**
     * 分页返回所有用户的列表。
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView viewUserList() {
        //TODO 未实现
        return null;
    }

    @RequestMapping(value="/delete", params="id")
    public String deleteUser(HttpServletRequest request, @RequestParam("id")Integer id) {
        userService.destroy(id);
        //返回用户之前的页面
        //这是HTTP协议的特点之一，访问某一URL时会保留之前的页面URL到Referer属性里．
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
