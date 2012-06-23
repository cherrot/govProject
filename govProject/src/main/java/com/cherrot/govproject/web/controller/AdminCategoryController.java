/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.service.CategoryService;
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
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Inject
    private CategoryService categoryService;

    @ModelAttribute("category")
    public Category get2ndCategory(@RequestParam(value="id", required=false)Integer categoryId) {
        Category category = null;
        if (categoryId != null) {
            try {
                category = categoryService.find(categoryId);
            } catch (PersistenceException e) {
                throw new ResourceNotFoundException();
            }
        } else {
            category = new Category();
        }
        return category;
    }

    @RequestMapping(value="", method= RequestMethod.GET)
    public ModelAndView viewCategories() {
        ModelAndView mav = new ModelAndView("admin/categories");
        processCategoryLists(mav);
       return mav;
    }

    @RequestMapping(value={"","/*/edit"}, method= RequestMethod.POST)
    public ModelAndView doEditCategory(@Valid @ModelAttribute("category")Category category
        , BindingResult result
        , @RequestParam("categoryParent")Integer categoryParentId) {

        ModelAndView mav = new ModelAndView("redirect:/admin/category");
        Category parent = null;
        try {
            parent = categoryService.find(categoryParentId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        category.setCategoryParent(parent);
        if (result.hasErrors() ) {
            //此页面也可处理新建目录的请求。
            mav.setViewName("/admin/editCategory");
            //调用该方法须确保category的categoryParent属性已被设置
            processCategoryParentList4Category(mav, category);
        } else {
            categoryService.save(category);
        }
        return mav;
    }

    @RequestMapping(value="/{categoryId}/edit", method= RequestMethod.GET)
    public ModelAndView editCategory(@PathVariable("categoryId")Integer categoryId) {
        ModelAndView mav = new ModelAndView("admin/editCategory");
        try {
            Category category = categoryService.find(categoryId);
            mav.addObject("category", category);
            processCategoryParentList4Category(mav, category);
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
                return "redirect:/admin/category";
            }
            categoryService.destroy(categoryId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/admin/category";
    }

    private void processCategoryLists(ModelAndView mav) {
        List<Category> secondCategorys = categoryService.listSecondLevelCategories(false, true);
        mav.addObject("categoryList", secondCategorys);
        List<Category> topCategorys = categoryService.listTopLevelCategories(true);
        mav.addObject("categoryGroups", topCategorys);
    }

    private void processCategoryParentList4Category(ModelAndView mav, Category category) {
        List<Category> categoryParents = null;
        if (categoryService.isSecondLevelCategory(category)) {
            categoryParents = categoryService.listTopLevelCategories(false);
        } else {
            categoryParents = categoryService.listSecondLevelCategories(false, false);
        }
        mav.addObject("categoryParents", categoryParents);
    }
}
