/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.service.PostService;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/admin/post")
public class AdminPostController {

    @Inject
    private PostService postService;

    //FIXME 未完成  PostControlle中应当有类似方法
    @RequestMapping(value="/list", method= RequestMethod.GET)
    public ModelAndView viewPostList(@RequestParam(value="userId", required=false)Integer userId) {
        //不指定用户Id，分页取出所有post
        if (userId == null) {
            postService.list(userId)
        } else { //指定了用户Id，分页取出属于该用户的文章。
            postService.listByUser(userId, userId)
        }
    }
}
