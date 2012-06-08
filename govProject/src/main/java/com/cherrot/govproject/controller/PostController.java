/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.service.PostService;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to display posts.
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/post")
public class PostController {

    @Inject private PostService postService;

    @RequestMapping("/{postSlug}")
    //如果@PathVariable不指定参数名，只有在编译时打开debug开关（javac -debug=no）时才可行！！（不建议）
    public ModelAndView viewPost(@PathVariable("postSlug")String postSlug) {
        ModelAndView mav = new ModelAndView("viewPost");
        return mav;
    }

}
