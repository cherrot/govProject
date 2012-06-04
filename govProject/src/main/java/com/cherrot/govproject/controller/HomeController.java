/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.TermService;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cherrot
 */
@Controller
public class HomeController {

    @Inject
    TermService termService;
    @Inject
    LinkService linkService;

    @RequestMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("home");
        List<Term> categories = termService.listByTypeOrderByCount(Term.TermType.CATEGORY);
        mav.addObject("categories", categories);
        List<LinkCategory> linkCategories = linkService.listCategories();
        mav.addObject("linkCategories", linkCategories);
        return mav;
    }
}
