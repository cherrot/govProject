/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TagService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.List;
import javax.inject.Inject;
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

    @Inject
    private CategoryService categoryService;
    @Inject
    private TagService tagService;
    @Inject
    private PostService postService;
    
    @RequestMapping("/category/{categorySlug}")
    public ModelAndView listPostsByCategoryPage1(@PathVariable("categorySlug")String slug) {
        ModelAndView mav = new ModelAndView("/listPosts");
        //TODO 根据分类的slug取出分类，然后根据该分类分页查询文章列表
        try {
            Category category = categoryService.findBySlug(slug, false, false);
            mav.addObject("term", category);
            List<Post> postList = postService.listNewestPostsByCategory(category.getId(), 1, DEFAULT_PAGE_SIZE);
            mav.addObject("postList", postList);
            mav.addObject("pageNum", 1);
            int pageCount = category.getCount();
            mav.addObject(categoryService);
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
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
