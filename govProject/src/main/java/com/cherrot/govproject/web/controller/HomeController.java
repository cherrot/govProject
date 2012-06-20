/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Option;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.OptionService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.SiteLogService;
import com.cherrot.govproject.service.TagService;
import com.cherrot.govproject.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于forward: 和 redirect: ，见文档P474
 * @author cherrot
 */
@Controller
public class HomeController {

    @Inject
    private CategoryService categoryService;
    @Inject
    private LinkService linkService;
    @Inject
    private PostService postService;

    @RequestMapping("/")
    public ModelAndView home(/*TOTO 仅用于测试*/HttpServletRequest request) {
        //TODO 仅用于生成测试数据！
        initData();
        BaseController.setSessionUser(request.getSession(), userService.find(1));

        ModelAndView mav = new ModelAndView("home");
        List<Category> categories = categoryService.list();
        mav.addObject("categories", categories);
        for (Category category : categories) {
            mav.addObject(category.getName(), postService.listNewestPostsByCategory(category.getId(), 1, 5));
        }
        List<LinkCategory> linkCategories = linkService.listLinkCategories(true);
        mav.addObject("linkCategories", linkCategories);
        return mav;
    }

    /**
     * TODO 以下内容仅用于生成测试数据！
     */
    @Inject private CommentService commentService;
    @Inject private UserService userService;
    @Inject private SiteLogService siteLogService;
    @Inject private OptionService optionService;
    @Inject private TagService tagService;
    private void initData() {
        try {
            userService.findByLoginName("cherrot+gov@cherrot.com", false, false, false, false);
        } catch (Exception ex) {
            //创建测试用户
            User user = new User("cherrot+gov@cherrot.com", "root", 0, new Date(), "切萝卜可爱多");
            userService.create(user);
            //创建测试分类和标签
            Category category = new Category(0, "我是文章分类", "test");
            Category category1 = new Category(0, "我也是分类", "test1");
            Tag tag = new Tag(0, "我是标签", "testtag");
            Tag tag1 = new Tag(0, "我也是标签", "test1");
            categoryService.create(category);
            categoryService.create(category1);
            tagService.create(tag);
            tagService.create(tag1);
            //创建测试文章
            Post post = new Post(new Date(), new Date(), true, 0, Post.PostStatus.PUBLISHED, Post.PostType.POST, "test", "我是文章标题", "我是文章内容");
            post.setUser(user);
            List<Category> categoryList = new ArrayList<Category>();
            categoryList.add(category);
            post.setCategoryList(categoryList);
            List<Tag> tagList = new ArrayList<Tag>();
            tagList.add(tag);
            tagList.add(tag1);
            post.setTagList(tagList);
            postService.create(post);
            //创建测试评论
            Comment comment = new Comment(new Date(), true, "Cherrot", "admin@cherrot.com", "http://www.cherrot.com", "127.0.0.1", "我是文章评论");
            comment.setPost(post);
            commentService.create(comment);
//            //创建测试日志
//            SiteLog siteLog = new SiteLog(new Date(), "我是操作日志");
//            siteLog.setUser(user);
//            siteLogService.create(siteLog);
            //创建测试全局选项
            Option option = new Option("test");
            option.setOptionValue("我是全局选项");
            optionService.create(option);
            //创建测试链接分类和链接
            LinkCategory linkCategory = new LinkCategory();
            linkCategory.setName("我是友情链接分类");
            linkService.createLinkCategory(linkCategory);
            Link link = new Link("http://www.cherrot.com", "Cherrot", Link.LinkTarget._blank);
            link.setLinkCategory(linkCategory);
            linkService.create(link);
        }
    }
}
