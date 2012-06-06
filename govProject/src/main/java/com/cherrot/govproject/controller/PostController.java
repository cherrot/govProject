/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.PostService;
import java.util.Date;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
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
 * Controller to display posts.
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/post")
public class PostController {

    @Inject
    private PostService postService;

    @ModelAttribute("newComment")
    public Comment getNewComment(){
        return new Comment(new Date(), false, null, null, null, "0.0.0.0", null);
    }

    @RequestMapping(value="/{postSlug}", method= RequestMethod.GET)
    //如果@PathVariable不指定参数名，只有在编译时打开debug开关（javac -debug=no）时才可行！！（不建议）
    public ModelAndView viewPost(@PathVariable("postSlug")String postSlug) {
        ModelAndView mav = new ModelAndView("viewPost");
        try {
            Post post = postService.findBySlug(postSlug, true, true, true);
            mav.addObject("post", post);
        } catch (NoResultException ex) {
            mav.setViewName("/errors/404");
        }
        return mav;
    }

    @RequestMapping(value="/", params="post")
    public ModelAndView viewPost(@RequestParam("post")int postId) {
        ModelAndView mav = new ModelAndView("viewPost");
        try {
            Post post = postService.find(postId, true, true, true);
            mav.addObject("post", post);
        } catch (NoResultException ex) {
            mav.setViewName("/errors/404");
        }
        return mav;
    }

    @RequestMapping(value="/", method= RequestMethod.POST)
    public String leaveAComment(HttpServletRequest request
        , @RequestParam("postId")int postId
        , @Valid @ModelAttribute("newComment")Comment comment
        , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //TODO 如何返回当前页面？
        }
        try {
            Post post = postService.find(postId);
        } catch (NoResultException ex) {
            //TODO
        }
        //TODO
        return "";
    }
}
