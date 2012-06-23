/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.util.Constants;
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
    public Comment getComment(@RequestParam(value="id", required=false)Integer categoryId) {
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

    @RequestMapping("")
    public ModelAndView viewComments(@RequestParam(value="pending", required=false)Boolean onlyPending) {
        return processCommentList(1, onlyPending);
    }
    
    @RequestMapping("/page/{pageNum}")
    public ModelAndView viewComments(@PathVariable("pageNum")int pageNum
        , @RequestParam(value="pending", required=false)Boolean onlyPending) {
        return processCommentList(pageNum, onlyPending);
    }

    @RequestMapping(value="/*/edit", method= RequestMethod.POST)
    public String doEditComment(@Valid @ModelAttribute("comment")Comment comment
        , BindingResult result) {

        String returnString = "redirect:/admin/comment";
        if (result.hasErrors()) {
            returnString = "admin/editComment";
        } else {
            commentService.edit(comment);
        }
        return returnString;
    }

    @RequestMapping(value="/{commentId}/edit", method= RequestMethod.GET)
    public ModelAndView editComment(@PathVariable("commentId")Integer commentId) {
        ModelAndView mav = new ModelAndView("admin/editComment");
        try {
            Comment comment = commentService.find(commentId);
            mav.addObject("comment", comment);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{commentId}/delete")
    public String doDeleteComment(@PathVariable("commentId")Integer commentId) {
        try {
            commentService.destroy(commentId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/admin/comment";
    }
    
    private ModelAndView processCommentList(int pageNum, boolean onlyPending) {
        ModelAndView mav = new ModelAndView("admin/comments");
        //FIXME: 添加只获取未审核评论的方法
        List<Comment> comments = commentService.list(pageNum, Constants.DEFAULT_PAGE_SIZE);
        mav.addObject("commentList", comments);
        //TODO: 添加pageNum和pageCount
        return mav;
    }
}
