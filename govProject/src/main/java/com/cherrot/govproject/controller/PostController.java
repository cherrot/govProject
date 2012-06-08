/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.service.CommentService;
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
 * TODO: Add http type converters which could convert String values and Integer Values to posts.
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/post")
public class PostController {

    @Inject
    private PostService postService;
    @Inject
    private CommentService commentService;

    /**
     * 返回一个新的Comment对象，默认为等待审核状态
     * @return
     */
    @ModelAttribute("newComment")
    public Comment getNewComment(HttpServletRequest request
        , @RequestParam("postId")int postId){

        Post post = postService.find(postId);
        Comment comment = new Comment(new Date(), false, null, null, null, "0.0.0.0", null);
        comment.setPost(post);
        return comment;
    }

    /**
     * 捕获URI为 /post?id=xxx的请求，显示文章
     * @param postId post的主键
     * @return
     */
    @RequestMapping(params="id")
    public ModelAndView viewPost(@RequestParam("id")int postId) {
        //TODO 最好能将用户浏览器URL置换为文章自定义链接的形式。
        ModelAndView mav = new ModelAndView("viewPost");
        try {
            Post post = postService.find(postId, true, true, true);
            mav.addObject("post", post);
        } catch (NoResultException ex) {
            mav.setViewName("/errors/404");
        }
        return mav;
    }

    /**
     * 捕获URI类似 /post/my-new-artical 形式的文章。
     * @param postSlug 文章短链接（slug字段）
     * @return
     */
    @RequestMapping(value="/{postSlug}", method= RequestMethod.GET)
    //如果@PathVariable不指定参数名，只有在编译时打开debug开关（javac -debug=no）时才可行！（不建议！）
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

    /**
     * 添加评论
     * @param request 用于获取请求者IP
     * @param postId 文章id
     * @param comment 评论
     * @param bindingResult 评论字段合法性验证结果
     * @return
     */
//    @RequestMapping(value="/*", method= RequestMethod.POST)
    public String leaveAComment(HttpServletRequest request
        , @RequestParam("postId")int postId
        , @Valid @ModelAttribute("newComment")Comment comment
        , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            //返回当前页面
            return request.getRequestURI();//当前页面URI
        }
//        Post post = null;
//        try {
//            post = postService.find(postId);
//        } catch (NoResultException ex) {
//            return "/errors/410";
//        }
        comment.setAuthorIp(request.getRemoteAddr());//IP
//        comment.setPost(post);
        commentService.create(comment);
        return request.getRequestURI();//当前页面URI;
    }

//    @RequestMapping(value={"/{postSlug}/edit"})
//    public ModelAndView editPost
}
