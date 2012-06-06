/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TermService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于forward: 和 redirect: ，见文档P474
 * @author cherrot
 */
@Controller
public class HomeController {

    @Inject
    private TermService termService;
    @Inject
    private LinkService linkService;
    @Inject
    private PostService postService;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("home");
        List<Term> categories = termService.listByTypeOrderbyCount(Term.TermType.CATEGORY, false, false);
        mav.addObject("categories", categories);
        for (Term category : categories) {
            mav.addObject(category.getName(), postService.listNewestPostsByTerm(category, 1, 5));
        }
        List<LinkCategory> linkCategories = linkService.listLinkCategories(true);
        mav.addObject("linkCategories", linkCategories);
        return mav;
    }
}
