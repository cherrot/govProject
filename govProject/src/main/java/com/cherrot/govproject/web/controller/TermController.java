/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import javax.persistence.NoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
public class TermController {

    @RequestMapping("/category/{categorySlug}")
    public ModelAndView listPostsByCategoryPage1(@PathVariable("categorySlug")String slug) {
        ModelAndView mav = new ModelAndView("/listPosts");
        //TODO 根据分类的slug取出分类，然后根据该分类分页查询文章列表
        try {

        } catch (NoResultException e) {
            mav.setViewName("redirect:/errors/404");
        }
        return mav;
    }

    @RequestMapping("/category/{categorySlug}/{pageNum}")
    public ModelAndView listPostsByCategory(@PathVariable("categorySlug")String slug) {
        ModelAndView mav = new ModelAndView("/listPosts");
        //TODO 根据分类的slug取出分类，然后根据该分类分页查询文章列表
        return mav;
    }

    @RequestMapping("/tag/{tagSlug}")
    public ModelAndView listPostsByTagPage1(@PathVariable("tagSlug")String slug) {
        ModelAndView mav = new ModelAndView("/listPosts");
        //与上面类同
        return mav;
    }

    @RequestMapping("/tag/{tagSlug}/{pageNum}")
    public ModelAndView listPostsByTag(@PathVariable("tagSlug")String slug) {
        ModelAndView mav = new ModelAndView("/listPosts");
        //与上面类同
        return mav;
    }
}
