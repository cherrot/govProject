/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Inject
    private CommentService commentService;

    @ModelAttribute("comment")
    public Comment get2ndCategory(@RequestParam(value="id", required=false)Integer categoryId) {
        Comment comment = null;
        if (categoryId != null) {
            try {
                comment = commentService.find(categoryId);
            } catch (PersistenceException e) {
                throw new ResourceNotFoundException();
            }
        }
        return comment;
    }

    @RequestMapping(value="", method= RequestMethod.GET)
    public ModelAndView viewComments() {
        ModelAndView mav = new ModelAndView("admin/comments");

       return mav;
    }

    @RequestMapping(value={"","/*/edit"}, method= RequestMethod.POST)
    public ModelAndView doEditCategory(@Valid @ModelAttribute("category")Category category
        , BindingResult result
        , @RequestParam("categoryParent")Integer categoryParentId) {

        ModelAndView mav = new ModelAndView("admin/categories");
        if ( !result.hasErrors() ) {
            Category parent = categoryService.find(categoryParentId);
            category.setCategoryParent(parent);
            if (category.getId() != null) {
                processCategoryParentList4Category(mav, category);
            }
            categoryService.save(parent);
        }
        processCategoryLists(mav);
        return mav;
    }

    @RequestMapping(value="/{categoryId}/edit")
    public ModelAndView editCategory(@PathVariable("categoryId")Integer categoryId) {
        ModelAndView mav = new ModelAndView("admin/editCategory");
        try {
            Category category = categoryService.find(categoryId);
            mav.addObject("category", category);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{categoryId}/delete")
    public String doDeleteCategory(@PathVariable("categoryId")Integer categoryId) {

        try {
            Category category = categoryService.find(categoryId, false, false);
            //顶级分类不可删除！
            if (categoryService.isTopLevelCategory(category)) {
                return "redirect:/admin/categories";
            }
            categoryService.destroy(categoryId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/admin/categories";
    }

    private void processCategoryLists(ModelAndView mav) {
        List<Category> secondCategorys = categoryService.listSecondLevelCategories(false, true);
        mav.addObject("categoryList", secondCategorys);
        List<Category> topCategorys = categoryService.listTopLevelCategories(true);
        mav.addObject("categoryGroups", topCategorys);
    }
}
