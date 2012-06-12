/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.UserService;
import static com.cherrot.govproject.util.Constants.ERROR_MSG_KEY;
import static com.cherrot.govproject.util.Constants.LOGIN_TO_URL;
import static com.cherrot.govproject.web.controller.BaseController.getSessionUser;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Inject
    private CommentService commentService;
    @Inject
    private PostService postService;
    @Inject
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView viewSessionUser(HttpServletRequest request,
        @RequestParam(value="commentPage", required=false)Integer commentPageNum,
        @RequestParam(value="postPage", required=false)Integer postPageNum ) {

        ModelAndView mav = new ModelAndView("viewUser");
        User user = getSessionUser(request.getSession());
        if ( user != null) {
            mav.addObject("user", user);
            List<Comment> userComments = commentService.listByUser(user.getId(),
                commentPageNum==null ? 1 : commentPageNum);
            mav.addObject("userComments", userComments);
            List<Post> userPosts = postService.listByUser(user.getId(),
                postPageNum==null ? 1 : postPageNum);
            mav.addObject("userPosts", userPosts);
        } else {
            request.getSession().setAttribute(ERROR_MSG_KEY, "请重新登录！");
            request.getSession().setAttribute(LOGIN_TO_URL, request.getRequestURI());
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @RequestMapping(value="edit", method=RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("editUser");
        User user = BaseController.getSessionUser(request.getSession());
        if (user != null) {
            mav.addObject("user", user);
        } else {
            request.getSession().setAttribute(LOGIN_TO_URL, request.getRequestURI());
            mav.setViewName("redirect:/login");
        }
        return mav;
    }

    @RequestMapping(value="edit", method= RequestMethod.POST)
    public String doEditUser(@Valid @ModelAttribute("user")User user) {
        userService.edit(user);
        return "redirect:/user/";
    }
}
