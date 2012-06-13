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
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    @RequestMapping(params="id")
    public ModelAndView viewUser(@RequestParam("id")Integer userId) {
        ModelAndView mav = new ModelAndView("/viewUser");
        //TODO 获取User对象。查看 viewUser.jsp确定需要注入的对象。 可以参考UserController的同名方法。
        //注意此方法是给管理员用的
        User user = userService.find(userId);
        mav.addObject("user", user);
        String userRole = userService.getDescriptionOfUserLevel(user.getUserLevel());
        mav.addObject("userRole", userRole);
        List<Post> userPosts = postService.listNewesPostsByUser(userId, 1);
        mav.addObject("userPosts",userPosts);
        List<Comment> userComments = commentService.listNewesCommentsByUser(userId, 1);
        mav.addObject("userComments", userComments);
        return mav;
    }

    /**
     * 分页返回所有用户的列表。
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView viewUserList(
            @RequestParam(value="pageNum", required=false)Integer pageNum, 
            @RequestParam(value="pageSize",required=false)Integer pageSize) {
        
        ModelAndView mav = new ModelAndView("/admin/users");
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = DEFAULT_PAGE_SIZE;
        List<User> userList = userService.list(pageNum, pageSize);
        mav.addObject("userList", userList);
        List<String> roleList = new ArrayList<String>();
        List<Integer> postCountList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        
        for (User user : userList) {
            roleList.add( userService.getDescriptionOfUserLevel(user.getUserLevel()) );
            postCountList.add( postService.getCountByUser(user.getId()) );
            commentCountList.add( commentService.getCountByUser(user.getId()) );
        }
        
        mav.addObject("roleList", roleList);
        mav.addObject("postCountList", postCountList);
        mav.addObject("commentCountList", commentCountList);
        
        return mav;
    }

    @RequestMapping(value="/delete", params="id")
    public String deleteUser(HttpServletRequest request, @RequestParam("id")Integer id) {
        userService.destroy(id);
        //返回用户之前的页面
        //这是HTTP协议的特点之一，访问某一URL时会保留之前的页面URL到Referer属性里．
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
