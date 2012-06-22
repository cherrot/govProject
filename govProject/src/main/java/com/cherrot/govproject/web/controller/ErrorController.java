/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.LinkService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/errors")
public class ErrorController {
    @Inject
    private CategoryService categoryService;
    @Inject
    private LinkService linkService;

    /**
     * 顶部导航栏的文章分类
     * @return
     */
    @ModelAttribute("categories")
    public List<Category> getSecondLevelCategoryList() {
        return categoryService.listSecondLevelCategories(false, false);
    }

    /**
     * 友情链接分类和分类下的友情链接
     * @return
     */
    @ModelAttribute("linkCategories")
    public List<LinkCategory> getLinkCategoryList() {
        return linkService.listLinkCategories(true);
    }

    @RequestMapping("/{errorCode}")
    public String handleError(@PathVariable("errorCode")Integer errorCode) {
        return "/errors/" + errorCode;
    }
}
