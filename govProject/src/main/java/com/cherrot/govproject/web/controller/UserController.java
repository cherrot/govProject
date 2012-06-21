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
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
import static com.cherrot.govproject.util.Constants.ERROR_MSG_KEY;
import static com.cherrot.govproject.util.Constants.LOGIN_TO_URL;
import com.cherrot.govproject.web.exceptions.ForbiddenException;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/user")
public class UserController {

    @Inject
    private CommentService commentService;
    @Inject
    private PostService postService;
    @Inject
    private UserService userService;

    @ModelAttribute("user")
    public User getUser(@RequestParam(value="id", required=false)Integer userId, HttpSession session) {
        User user = null;
        if (userId != null) {
            try {
                user = userService.find(userId, false, false, true, false);
            } catch (PersistenceException e) {
                throw new ResourceNotFoundException();
            }
        }/* else {
            //可能为null
            user = BaseController.getSessionUser(session);
        }*/
        return user;
    }

    @RequestMapping(value={"","/"})
    public ModelAndView viewSessionUser(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("viewUser");
        User user = BaseController.getSessionUser(request.getSession());
        if ( user != null) {
            mav.addObject("user", user);
            processComments(mav, user, 1);
            processPosts(mav, user, 1);
        } else {
            request.getSession().setAttribute(ERROR_MSG_KEY, "您必须登录才能查看自己的个人资料");
            request.getSession().setAttribute(LOGIN_TO_URL, request.getRequestURL());
//            mav.setViewName("redirect:/login");
            throw new ForbiddenException();
        }
        return mav;
    }

    @RequestMapping("/{userId}")
    public ModelAndView viewUser(@PathVariable("userId")Integer userId) {

        ModelAndView mav = new ModelAndView("viewUser");
        try {
            User user = userService.find(userId, false, false, true, false);
            mav.addObject("user", user);
            processComments(mav, user, 1);
            processPosts(mav, user, 1);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("editUser");
        User user = BaseController.getSessionUser(request.getSession());
        if (user != null) {
            mav.addObject("user", user);
        } else {
            request.getSession().setAttribute(LOGIN_TO_URL, request.getRequestURL());
//            mav.setViewName("redirect:/login");
            throw new ForbiddenException();
        }
        return mav;
    }

    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public String doEditUser(@Valid @ModelAttribute("user")User user
        , final BindingResult result
        , HttpServletRequest request) {

        if (result.hasErrors()) {
            return "editUser";
        }

        User sessionUser = BaseController.getSessionUser(request.getSession());
        //XXX 不能写成sessionUser.equals(user):sessionUser可能为null；而且user是刚刚从数据库中取出的user.getClass可能并不是User.class(因为有动态代理)
        if (user.equals(sessionUser)) {
            userService.edit(user);
            return "redirect:/user/";
        } else {
            request.getSession().setAttribute(LOGIN_TO_URL, request.getRequestURL());
            throw new ForbiddenException();
        }
    }

    @RequestMapping("/{userId}/posts")
    public ModelAndView listPostsByUser(@PathVariable("userId")Integer userId) {

        ModelAndView mav = new ModelAndView("listPostsByUser");
        try {
            User user = userService.find(userId);
            mav.addObject("user", user);
            processPosts(mav, user, 1);
        } catch(PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{userId}/posts/page/{pageNum}")
    public ModelAndView listPostsByUser(@PathVariable("userId")Integer userId
        , @PathVariable("pageNum")Integer pageNum) {

        ModelAndView mav = new ModelAndView("listPostsByUser");
        try {
            User user = userService.find(userId);
            mav.addObject("user", user);
            processPosts(mav, user, pageNum);
        } catch(PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{userId}/comments")
    public ModelAndView listComments(@PathVariable("userId")Integer userId) {

        ModelAndView mav = new ModelAndView("listCommentsByUser");
        try {
            User user = userService.find(userId);
            mav.addObject("user", user);
            processComments(mav, user, 1);
        } catch(PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    @RequestMapping("/{userId}/comments/page/{pageNum}")
    public ModelAndView listComments(@PathVariable("userId")Integer userId
        , @PathVariable("pageNum")int pageNum) {

        ModelAndView mav = new ModelAndView("listCommentsByUser");
        try {
            User user = userService.find(userId);
            mav.addObject("user", user);
            processComments(mav, user, pageNum);
        } catch(PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    private void processPosts(ModelAndView mav, User user, int pageNum) {
        List<Post> userPosts = postService.listNewesPostsByUser(user, pageNum, DEFAULT_PAGE_SIZE);
        mav.addObject("postList", userPosts);
    }

    private void processComments(ModelAndView mav, User user, int pageNum) {
        List<Comment> userComments = commentService.listNewesCommentsByUser(user, pageNum, DEFAULT_PAGE_SIZE);
        mav.addObject("commentList", userComments);
    }
}
