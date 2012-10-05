/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.PostService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于forward: 和 redirect: ，见Spring3.1文档P474
 *
 * @author cherrot
 */
@Controller
public class HomeController {

    @Inject
    private CategoryService categoryService;
    @Inject
    private LinkService linkService;
    @Inject
    private PostService postService;

    /**
     * 顶部导航栏的文章分类
     *
     * @return
     */
    @ModelAttribute("categories")
    public List<Category> getSecondLevelCategoryList() {
        return categoryService.listSecondLevelCategories(false, false);
    }

    /**
     * 友情链接分类和分类下的友情链接
     *
     * @return
     */
    @ModelAttribute("linkCategories")
    public List<LinkCategory> getLinkCategoryList() {
        return linkService.listLinkCategories(true);
    }

    @RequestMapping("/")
    public ModelAndView home() {

        ModelAndView mav = new ModelAndView("home");

        //添加首页主体部分的文章群组分类和该分类的最新文章
        List<Category> topLevelCategorys = categoryService.listTopLevelCategories(true);
        topLevelCategorys.remove(categoryService.findBySlug("hidden", false, false));
        topLevelCategorys.remove(categoryService.findImageCategory(false));
        topLevelCategorys.remove(categoryService.findVideoCategory(false));

        mav.addObject("categoryGroups", topLevelCategorys);
        for (Category group : topLevelCategorys) {
            for (Category category : group.getCategoryList()) {
                mav.addObject(category.getName(), postService.listNewestPostsByCategory(category, 1, 5));
            }
        }
        //添加多媒体文章
        List<Post> imagePosts = postService.listNewestImagePosts(1, 5);
        mav.addObject("imagePosts", imagePosts);
        return mav;
    }
}
