/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

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

    //日后或许也会映射 /post/list?categoryId=xxx此类的URI
    @RequestMapping("/category/{categorySlug}")
    public ModelAndView listPostsByCategory(@PathVariable("categorySlug")String slug) {
        ModelAndView mav = new ModelAndView("/posts");
        //TODO 根据分类的slug取出分类，然后根据该分类分页查询文章列表
        return mav;
    }
    @RequestMapping("/tag/{tagSlug}")
    public ModelAndView listPostsByTag(@PathVariable("tagSlug")String slug) {
        ModelAndView mav = new ModelAndView("/posts");
        //与上面类同
        return mav;
    }
}
