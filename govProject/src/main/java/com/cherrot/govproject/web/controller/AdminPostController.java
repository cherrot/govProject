/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TagService;
import com.cherrot.govproject.service.UserService;
import static com.cherrot.govproject.util.Constants.SUCCESS_MSG_KEY;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
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
 * 管理员修改文章的控制器。 因为管理员可以修改文章的一切属性，比如文章的作者，因此需要单独一个控制器负责
 * 管理员新建文章等同于其他用户新建文章，故本控制器不做处理
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping("/admin/post")
public class AdminPostController {

    @Inject
    private PostService postService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private TagService tagService;
    @Inject
    private UserService userService;

    @ModelAttribute("post")
    public Post getPost(@PathVariable("postId")Integer postId) {
        Post post = null;
        try {
            post = postService.find(postId, false, true, true, true, false);
        } catch (PersistenceException e) {
            throw new ResourceNotFoundException();
        }
        return post;
    }

    /**
     * 注入的Post对象，用户提交文章时表单中未包含的post字段（比如post.id）会丢失，因此应显式添加
     * @param request 用于返回用户请求前的页面(编辑文章页)
     * @param post 提交的post
     * @param bindingResult 数据验证结果
     * @param redirectAttr 用于添加Flash Attributes用于redirect后的控制器/页面使用
     * @return
     */
    @RequestMapping(value="/{postId}", method= RequestMethod.POST)
    public String doEditPost(HttpServletRequest request
        ,@Valid @ModelAttribute("post")Post post
        ,BindingResult bindingResult
        ,RedirectAttributes redirectAttr
        ,@RequestParam("postTags")String postTags
        ,@RequestParam("postCategories")Integer[] categoryIds
        ,@RequestParam("postAuthor")Integer postAuthorId) {

        if (bindingResult.hasErrors()) {
            redirectAttr.addFlashAttribute("post", post);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
        } else {
            //文章标签
            List<Tag> tagList = tagService.createTagsByName(Arrays.asList(postTags.split("\\s*,|，\\s*")));//匹配非汉字： [^\\u4e00-\\u9fa5]+
            post.setTagList(tagList);
            //文章目录
            List<Category> categoryList = new ArrayList<Category>();
            for (Integer categoryId : categoryIds) {
                categoryList.add(categoryService.find(categoryId));
            }
            post.setCategoryList(categoryList);
            //文章作者
            User author = userService.find(postAuthorId);
            post.setUser(author);
            //保存
            postService.save(post);
            redirectAttr.addFlashAttribute(SUCCESS_MSG_KEY, "文章保存成功！");
        }
        return "redirect:/admin/post/" + post.getId() + "/edit";
    }

    @RequestMapping(value="/{postId}", method= RequestMethod.GET)
    public ModelAndView editPost(@ModelAttribute("post")Post post) {

        ModelAndView mav = processModels4EditPost(post);
        return mav;
    }

    /**
     * 注入editPost页面所必需的对象，比如以Map注入枚举类型PostStatus
     * @param post 可以是持久化Post对象或新建Post对象，不能为null。
     * @return 注入必需Model的ModelAndView
     */
    private ModelAndView processModels4EditPost(Post post) {
        //使用editPost.jsp
        ModelAndView mav = new ModelAndView("admin/editPost");
        //设置文章发布状态的Map，用于<form:select>
        Map<Post.PostStatus, String> postStatusMap = new EnumMap<Post.PostStatus, String>(Post.PostStatus.class);
        postStatusMap.put(Post.PostStatus.PUBLISHED, Post.PostStatus.PUBLISHED.getDescription());
        postStatusMap.put(Post.PostStatus.DRAFT, Post.PostStatus.DRAFT.getDescription());
        postStatusMap.put(Post.PostStatus.PENDING, Post.PostStatus.PENDING.getDescription());
        mav.addObject("postStatus", postStatusMap);
        //添加全部文章分类(二级分类和子分类)
        List<Category> categories = categoryService.listSecondLevelCategories(false, true);
        mav.addObject("categories", categories);

        //设置已选文章分类
        List<Category> postCategories = post.getCategoryList();
        for (Category category : postCategories) {
            mav.addObject(category.getName(), Boolean.TRUE);
        }

        //设置用户列表。管理员可以更改文章的作者。
        List<User> users = userService.list();
        mav.addObject("userList", users);
        return mav;
    }
}
