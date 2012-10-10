/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.UserService;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
public class AdminHomeController {

    @Inject
    private UserService userService;
    @Inject
    private CategoryService categoryService;

    /**
     * 顶部导航栏的文章分类
     *
     * @return
     */
    @ModelAttribute("categories")
    public List<Category> getSecondLevelCategoryList() {
        return categoryService.listSecondLevelCategories(false, false);
    }
    
    @RequestMapping("/admin")
    public ModelAndView adminPanel(HttpSession session) {
        ModelAndView mav = new ModelAndView("/admin/adminPanel");
        User user = BaseController.getSessionUser(session);
        String userRole = userService.getDescriptionOfUserLevel(user.getUserLevel());
        mav.addObject("userRole", userRole);
        return mav;
    }
}
