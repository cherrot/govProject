/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TagService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
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
    @Inject
    private LinkService linkService;

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

    @RequestMapping("/category/{categorySlug}")
    public ModelAndView listPostsByCategoryPage1(@PathVariable("categorySlug") String slug) {

        ModelAndView mav = new ModelAndView("/listPostsByTerm");
        try {
            Category category = categoryService.findBySlug(slug, false, false);
            processPosts4CategoryOrTag(mav, category, true, 1);
        } catch (Exception ex) {
            Logger.getLogger(TermController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/category/{categorySlug}/page/{pageNum}")
    public ModelAndView listPostsByCategory(@PathVariable("categorySlug") String slug, @PathVariable("pageNum") int pageNum) {

        ModelAndView mav = new ModelAndView("/listPostsByTerm");
        try {
            Category category = categoryService.findBySlug(slug, false, false);
            processPosts4CategoryOrTag(mav, category, true, pageNum);
        } catch (Exception ex) {
            Logger.getLogger(TermController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/tag/{tagSlug}")
    public ModelAndView listPostsByTagPage1(@PathVariable("tagSlug") String slug) {

        ModelAndView mav = new ModelAndView("/listPostsByTerm");
        try {
            Tag tag = tagService.findBySlug(slug, false);
            processPosts4CategoryOrTag(mav, tag, false, 1);
        } catch (Exception ex) {
            Logger.getLogger(TermController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/tag/{tagSlug}/page/{pageNum}")
    public ModelAndView listPostsByTag(@PathVariable("tagSlug") String slug, @PathVariable("pageNum") int pageNum) {

        ModelAndView mav = new ModelAndView("/listPostsByTerm");
        try {
            Tag tag = tagService.findBySlug(slug, false);
            processPosts4CategoryOrTag(mav, tag, false, pageNum);
        } catch (Exception ex) {
            Logger.getLogger(TermController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    /**
     *
     * @param <Term>
     * @param mav
     * @param categoryOrTag
     * @param isCategory 使用该标志判断categoryOrTag的类型。 不使用instanceof是为了防止动态代理带来的类型问题
     * @param pageNum
     */
    private <Term extends Serializable> void processPosts4CategoryOrTag(ModelAndView mav, Term categoryOrTag, boolean isCategory, int pageNum) {
        int pageCount;
        List<Post> postList;
        mav.addObject("term", categoryOrTag);
        mav.addObject("pageNum", pageNum);
        if (isCategory) {
            Category term = (Category) categoryOrTag;
            postList = postService.listNewestPostsByCategory(term, pageNum, DEFAULT_PAGE_SIZE);
            pageCount = term.getCount() / DEFAULT_PAGE_SIZE + 1;
        } else {
            Tag term = (Tag) categoryOrTag;
            postList = postService.listNewestPostsByTag(term, pageNum, DEFAULT_PAGE_SIZE);
            pageCount = term.getCount();
        }
        mav.addObject("postList", postList);
        mav.addObject("pageCount", pageCount);
    }
}
