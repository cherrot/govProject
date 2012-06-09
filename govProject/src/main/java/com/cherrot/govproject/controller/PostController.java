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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 本类中使用两种方式处理 @ModelAttribute 注解，一种是通过@ModelAttribute
 * 注解的方法得到新的实例用于数据绑定和验证（处理Comment）
 * 第二种是在编辑文章时用到的，在GET请求中注入post完成数据绑定。
 * 什么，为什么使用两种方法？ Just for fun ;) !
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
     * 返回一个新的Comment对象
     * 本方法直接使用默认构造器返回一个Comment对象，因此完全可以省略
     * @return
     */
    @ModelAttribute("newComment")
    public Comment getNewComment(){
        return new Comment();
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
            mav.setViewName("redirect:/errors/404");
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
            mav.setViewName("redirect:/errors/404");
        }
        return mav;
    }

    /**
     * 添加评论
     * 关于在redirectView中添加临时属性请参考：
     * @see Spring3.1文档480页 Supported method argument types、489页 Specifying redirect and flash attributes 、503页 Using flash attributes
     * @see http://blog.goyello.com/2011/12/16/enhancements-spring-mvc31/
     * @see https://github.com/SpringSource/spring-mvc-showcase/wiki/@MVC-Flash-Attribute-Support 注意@RedirectAttributes已不存在
     * redirect后保留comment对象和bindingResult：
     * @see http://stackoverflow.com/questions/2543797/spring-redirect-after-post-even-with-validation-errors
     * @param request 用于获取请求者IP
     * @param postId 文章id
     * @param comment 评论
     * @param bindingResult 评论字段合法性验证结果
     * @return
     */
    @RequestMapping(value={"/{postSlug}","*"}, method= RequestMethod.POST)
    public String leaveAComment(HttpServletRequest request
        , @RequestParam("postId")final int postId
        , @Valid @ModelAttribute("newComment")final Comment comment
        , final BindingResult bindingResult
        , RedirectAttributes redirectAttr) {

        if (bindingResult.hasErrors()) {
            System.err.println();
            redirectAttr.addFlashAttribute("newComment", comment);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.newComment",bindingResult);
            //返回Post前的页面
            //return "redirect:" + request.getRequestURI();//当前页面URI--会导致重复的contextPath
            String referer = request.getHeader("Referer");
            return "redirect:"+ referer;
        }

        Post post = null;
        try {
            post = postService.find(postId);
        } catch (NoResultException ex) {
            return "redirect:/errors/410";
        }

        comment.setAuthorIp(request.getRemoteAddr());//IP
        comment.setPost(post);
        commentService.create(comment);
        redirectAttr.addFlashAttribute("postComment", comment);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * 进入编辑文章页面
     * @param postSlug
     * @return
     */
    @RequestMapping(value="/{postSlug}/edit", method= RequestMethod.GET)
    public ModelAndView editPost(@PathVariable("postSlug")final String postSlug) {
        ModelAndView mav = new ModelAndView("editPost");
        try {
            Post post = postService.findBySlug(postSlug, false, true, true);
            mav.addObject("post", post);
        } catch(NoResultException ex) {
            mav.setViewName("redirect:errors/404");
        } finally {
            return mav;
        }
    }

    /**
     * 进入编辑文章或新建文章页面
     * @param postId
     * @return
     */
    @RequestMapping(value = {"/edit", "/create"}, method= RequestMethod.GET)
    public ModelAndView editPost(@RequestParam(value="id", required=false)final Integer postId) {
        ModelAndView mav = new ModelAndView("editPost");
        try {
            Post post = null;
            if (postId != null){
                post = postService.find(postId, false, true, true);
            } else {
                //TODO 如果未指定id，直接使用默认构造器。 默认构造器需按契约设定字段值。 下面这行可以删除
                post = new Post();
            }
            mav.addObject("post", post);
        } catch(NoResultException ex) {
            mav.setViewName("redirect:errors/404");
        } finally {
            return mav;
        }
    }

    /**
     * TODO: 通过editPost方法注入的post对象，用户提交文章时表单中未包含的post字段（比如post.id）是否会丢失？
     * @param request 用于返回用户请求前的页面
     * @param post 提交的post
     * @param bindingResult 数据验证结果
     * @param redirectAttr 用于添加Flash Attributes用于redirect后的控制器/页面使用
     * @return
     */
    @RequestMapping(value={"/*/edit", "/edit", "/create"})
    public String doEditPost(HttpServletRequest request
        ,@Valid @ModelAttribute("post")Post post
        ,BindingResult bindingResult
        ,RedirectAttributes redirectAttr) {

        if (bindingResult.hasErrors()) {
            redirectAttr.addFlashAttribute("post", post);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
        }
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
