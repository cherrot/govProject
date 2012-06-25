/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.UserService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
import static com.cherrot.govproject.util.Constants.USER_NORMAL;
import static com.cherrot.govproject.util.Constants.USER_PENDING;
import static com.cherrot.govproject.util.Constants.USER_WENLIAN;
import static com.cherrot.govproject.util.Constants.USER_XUANCHUANBU;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
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
@RequestMapping("/admin/user")
public class AdminUserController {

    @Inject
    private UserService userService;
    @Inject
    private CommentService commentService;
    @Inject
    private PostService postService;

    @ModelAttribute("user")
    public User getUser(@RequestParam(value="id", required=false)Integer userId) {
        User user = null;
        if (userId != null) {
            try {
                user = userService.find(userId);
            } catch (PersistenceException e) {
                throw new ResourceNotFoundException();
            }
        } else {
            user = new User();
        }
        return user;
    }

    @RequestMapping("")
    public ModelAndView viewUserList() {
        return processUserList(1);
    }

    @RequestMapping("/page/{pageNum}")
    public ModelAndView viewUserList(@PathVariable("pageNum")int pageNum) {
        return processUserList(pageNum);
    }

    @RequestMapping(value="/{userId}", method= RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("userId")Integer userId) {
        ModelAndView mav = new ModelAndView("admin/editUser");
        try {
            User user = userService.find(userId);
            mav.addObject("user", user);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        Map<Integer, String> roleMap = new WeakHashMap<Integer, String>();
        roleMap.put(USER_PENDING, userService.getDescriptionOfUserLevel(USER_PENDING));
        roleMap.put(USER_NORMAL, userService.getDescriptionOfUserLevel(USER_NORMAL));
        roleMap.put(USER_WENLIAN, userService.getDescriptionOfUserLevel(USER_WENLIAN));
        roleMap.put(USER_XUANCHUANBU, userService.getDescriptionOfUserLevel(USER_XUANCHUANBU));
        mav.addObject("roleMap", roleMap);
        return mav;
    }

    @RequestMapping(value="/*", method= RequestMethod.POST)
    public ModelAndView doEditTag(@Valid @ModelAttribute("tag")User user
        , BindingResult result) {

        ModelAndView mav = new ModelAndView("redirect:/admin/user");
        if (result.hasErrors() ) {
            mav.setViewName("/admin/editUser");
        } else {
            userService.save(user);
        }
        return mav;
    }

    @RequestMapping("/{userId}/delete")
    public String doDeleteTag(@PathVariable("userId") Integer userId) {
        try {
            userService.destroy(userId);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return "redirect:/admin/tag";
    }

    private ModelAndView processUserList(int pageNum) {
        ModelAndView mav = new ModelAndView("/admin/users");
        List<User> userList = userService.list(pageNum, DEFAULT_PAGE_SIZE);
        mav.addObject("userList", userList);
        List<String> roleList = new ArrayList<String>();
        List<Integer> postCountList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        for (User user : userList) {
            roleList.add( userService.getDescriptionOfUserLevel(user.getUserLevel()) );
            postCountList.add( postService.getCountByUser(user) );
            commentCountList.add( commentService.getCountByUser(user) );
        }
        mav.addObject("roleList4UserList", roleList);
        mav.addObject("postCountList", postCountList);
        mav.addObject("commentCountList", commentCountList);
        int pageCount = userService.getCount()/DEFAULT_PAGE_SIZE +1;
        mav.addObject("pageNum", pageNum);
        mav.addObject("pageCount", pageCount);

        return mav;
    }
}
