/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.service.TagService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
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
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Inject
    private TagService tagService;

    @ModelAttribute("tag")
    public Tag getTag(@RequestParam(value="id", required=false)Integer tagId) {
        Tag tag = null;
        if (tagId != null) {
            try {
                tag = tagService.find(tagId);
            } catch (PersistenceException e) {
                throw new ResourceNotFoundException();
            }
        }
        return tag;
    }

    @RequestMapping(value="", method= RequestMethod.GET)
    public ModelAndView viewTags() {
        return processTagLists(1);
    }

    @RequestMapping(value="/page/${pageNum}", method= RequestMethod.GET)
    public ModelAndView viewTags(@PathVariable("pageNum")int pageNum) {
        return processTagLists(pageNum);
    }

    @RequestMapping(value="/*", method= RequestMethod.POST)
    public ModelAndView doEditTag(@Valid @ModelAttribute("tag")Tag tag
        , BindingResult result) {

        ModelAndView mav = new ModelAndView("redirect:/admin/tag");
        if (result.hasErrors() ) {
            mav.setViewName("/admin/editTag");
        } else {
            tagService.edit(tag);
        }
        return mav;
    }

    @RequestMapping(value="/{tagId}", method= RequestMethod.GET)
    public ModelAndView editTag(@PathVariable("tagId")Integer tagId) {
        ModelAndView mav = new ModelAndView("admin/editTag");
        try {
            Tag tag = tagService.find(tagId);
            mav.addObject("tag", tag);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{tagId}/delete")
    public String doDeleteTag(@PathVariable("tagId")Integer tagId) {
        try {
            tagService.destroy(tagId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/admin/tag";
    }

    private ModelAndView processTagLists(int pageNum) {
        ModelAndView mav = new ModelAndView("admin/tags");
        List<Tag> tags = tagService.list(pageNum, DEFAULT_PAGE_SIZE);
        mav.addObject("tagList", tags);
        mav.addObject("pageNum", pageNum);
        int pageCount = tagService.getCount()/DEFAULT_PAGE_SIZE +1;
        mav.addObject("pageCount", pageCount);
        return mav;
    }
}