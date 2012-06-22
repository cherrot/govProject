/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TagService;
import static com.cherrot.govproject.util.Constants.SUCCESS_MSG_KEY;
import com.cherrot.govproject.web.exceptions.ForbiddenException;
import com.cherrot.govproject.web.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 本类中使用三种方式处理 @ModelAttribute 注解，一种是通过@ModelAttribute
 * 注解的方法得到新的实例用于数据绑定和验证（处理Comment）
 * 第二种是在编辑文章时用到的，在GET请求中注入post完成数据绑定。
 * 第三中是在新建文章时用到的，如果用户是新建文章，则不注入post对象，此时Spring会使用默认构造器注入。
 * 什么，为什么使用两种方法？ Just for fun ;) !
 * TODO: Add http type converters which could convert String values and Integer Values to posts.
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Controller
@RequestMapping({"/post","admin/post"})
public class PostController {

    @Inject
    private PostService postService;
    @Inject
    private CommentService commentService;
    @Inject
    private CategoryService categoryService;
    @Inject
    private TagService tagService;
    @Inject
    private LinkService linkService;

    /**
     * 顶部导航栏的文章分类
     * @return
     */
    @ModelAttribute("categories")
    public List<Category> getSecondLevelCategoryList() {
        return categoryService.listSecondLevelCategories(false, false);
    }

    /**
     * 友情链接分类和分类下的友情链接
     * @return
     */
    @ModelAttribute("linkCategories")
    public List<LinkCategory> getLinkCategoryList() {
        return linkService.listLinkCategories(true);
    }

    /**
     * 返回一个新的Comment对象
     * 本方法直接使用默认构造器返回一个Comment对象，因此完全可以省略
     * @return
     */
    @ModelAttribute("newComment")
    public Comment getNewComment() {
        return new Comment();
    }

    /*
     * 要在Web控制器解决模型的不完整问题！
     */
    @ModelAttribute("post")
    public Post getPost(@RequestParam(value="id", required=false)Integer postId) {
        Post post = null;
        if (postId != null) {
            //适用于用户提交（POST）文章时从数据库获取文章对象
            post = postService.find(postId, true, true, true, true, true);
        } else {
            //适用于新建文章
            post = new Post();
            post.setTitle("新建文章");
        }
        //既不是新建文章也不是提交文章时(如GET一个文章)，那么需要在对应的方法中注入对象。不然就变成新建文章了（@ModelAttribute会在每个控制器方法前调用）
        return post;
    }

    /**
     * 捕获URI为 /post?postId=xxx的请求，显示文章
     * @param postId post的主键
     * @return
     */
    @RequestMapping(params="postId")
    public String viewPost(@RequestParam("postId")int postId) {
        try {
            Post post = postService.find(postId, false, false, false, false, false);
            return "redirect:/post/"+post.getSlug();
        } catch (Exception ex) {
            Logger.getLogger(PostController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
    }

    /**
     * 捕获URI类似 /post/my-new-artical 形式的文章。
     * @param postSlug 文章短链接（slug字段）
     * @return
     */
    @RequestMapping(value="/{postSlug}", method= RequestMethod.GET)
    public ModelAndView viewPost(@PathVariable("postSlug")String postSlug
        ,@CookieValue(value="pendingCommentsId", required=false)String pendingCommentsId
        ,HttpServletResponse response) {

        ModelAndView mav = new ModelAndView("viewPost");
        try {
            Post post = postService.findBySlug(postSlug, true, true, true, true, false);
            mav.addObject("post", post);
            mav.addObject("tagList", post.getTagList());
            //读取Cookie将访问者所有未审核评论显示在页面上
            if (pendingCommentsId != null) {
                List<Comment> pendingComments = processPendingCommentsString(pendingCommentsId);
                mav.addObject("pendingComments", pendingComments);
                StringBuilder strBuilder = new StringBuilder();
                for (Comment comment : pendingComments) {
                    strBuilder.append(comment.getId()).append(",");
                }
                //去掉末尾的逗号
                pendingCommentsId = strBuilder.length()>1 ? strBuilder.substring(0, strBuilder.length()-1) : null;
                Cookie cookie = new Cookie("pendingCommentsId", pendingCommentsId);
                if (pendingCommentsId == null) {
                    //删除cookie
                    cookie.setMaxAge(0);
                }
                response.addCookie(cookie);
            }
        } catch (Exception ex) {
            Logger.getLogger(PostController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
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
    @RequestMapping(value={"/{postSlug}"}, method= RequestMethod.POST)
    public String doCreateComment(HttpServletRequest request
        , @RequestParam("postId")final int postId
        , @Valid @ModelAttribute("newComment")final Comment comment
        , final BindingResult bindingResult
        , RedirectAttributes redirectAttr
        , @CookieValue(value="pendingCommentsId", required=false)String pendingCommentsId
        , HttpServletResponse response) {

        /**
         * 通过FlashAttribute传递临时属性到redirect后的控制器方法中。
         */
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
        } catch (Exception ex) {
            Logger.getLogger(PostController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        comment.setPost(post);
        commentService.create(comment);

        /**
         * 将该访问者的所有未审核评论写入Cookie
         */
        //将新增评论加入cookie
        if (pendingCommentsId != null) {
            pendingCommentsId = pendingCommentsId + "," + comment.getId();
        } else {
            pendingCommentsId = comment.getId().toString();
        }
        Cookie pendingCommentsCookie = new Cookie("pendingCommentsId", pendingCommentsId);
        response.addCookie(pendingCommentsCookie);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    /**
     * 进入编辑文章页面
     * @param postSlug
     * @return
     */
    @RequestMapping(value="/{postSlug}/edit", method= RequestMethod.GET)
    public ModelAndView editPost(@PathVariable("postSlug")final String postSlug
        ,HttpSession session) {

        ModelAndView mav = null;
        try {
            Post post = postService.findBySlug(postSlug, false, true, true, true, false);
            if ( (BaseController.getSessionUser(session) == null) || ( !BaseController.getSessionUser(session).equals(post.getUser()) ) ) {
                //无权修改
                throw new ForbiddenException();
            }
            mav = processModels4EditPost(post);
        } catch(PersistenceException ex) {
            Logger.getLogger(PostController.class.getSimpleName()).log(Level.WARNING, ex.getMessage(), ex);
            throw new ResourceNotFoundException();
        }
        return mav;
    }

    /**
     * 新建文章。
     * 如果是新建文章，Spring会自动使用默认构造器进行数据绑定，因此没有显式实例化post对象到ModelAndView中
     * @param postId
     * @return
     */
    @RequestMapping(value = "/create", method= RequestMethod.GET)
    public ModelAndView createPost(@ModelAttribute("post")Post post) {
//        Post post = new Post();
//        post.setTitle("新建文章");
        ModelAndView mav = processModels4EditPost(post);
        return mav;
    }

    /**
     * 注入的Post对象，用户提交文章时表单中未包含的post字段（比如post.id）会丢失，因此应显式添加
     * @param request 用于返回用户请求前的页面(编辑文章页)
     * @param post 提交的post
     * @param bindingResult 数据验证结果
     * @param redirectAttr 用于添加Flash Attributes用于redirect后的控制器/页面使用
     * @return
     */
    @RequestMapping(value={"/*/edit", "/create"}, method= RequestMethod.POST)
    public String doEditPost(HttpServletRequest request
        ,@Valid @ModelAttribute("post")Post post
        ,BindingResult bindingResult
        ,RedirectAttributes redirectAttr
        ,@RequestParam("postTags")String postTags
        ,@RequestParam("postCategories")Integer[] postCategories) {

        if (bindingResult.hasErrors()) {
            redirectAttr.addFlashAttribute("post", post);
            redirectAttr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
        } else {
            if (post.getId() == null) {
                User author = BaseController.getSessionUser(request.getSession());
                if ( author == null || ( post.getUser()!=null && !post.getUser().equals(author) ) ) {
                    throw new ForbiddenException();
                }
                post.setUser(author);
            }
            //文章标签
            List<Tag> tagList = tagService.createTagsByName(Arrays.asList(postTags.split("\\s*,|，\\s*")));//匹配非汉字： [^\\u4e00-\\u9fa5]+
            post.setTagList(tagList);
            //文章目录
            List<Category> categoryList = new ArrayList<Category>();
            for (Integer categoryId : postCategories) {
                categoryList.add(categoryService.find(categoryId));
            }
            post.setCategoryList(categoryList);
            //保存
            postService.save(post);
            redirectAttr.addFlashAttribute(SUCCESS_MSG_KEY, "文章保存成功！");
        }
//        String referer = request.getHeader("Referer");
        return "redirect:/post/" + post.getSlug() + "/edit";
    }


    /**
     * 注入editPost页面所必需的对象，比如以Map注入枚举类型PostStatus
     * @param post 可以是持久化Post对象或新建Post对象，不能为null。
     * @return 注入必需Model的ModelAndView
     */
    private ModelAndView processModels4EditPost(Post post) {
        //使用editPost.jsp
        ModelAndView mav = new ModelAndView("editPost");
        //设置文章发布状态的Map，用于<form:select>
        Map<Post.PostStatus, String> postStatusMap = new EnumMap<Post.PostStatus, String>(Post.PostStatus.class);
        postStatusMap.put(Post.PostStatus.PUBLISHED, Post.PostStatus.PUBLISHED.getDescription());
        postStatusMap.put(Post.PostStatus.DRAFT, Post.PostStatus.DRAFT.getDescription());
        postStatusMap.put(Post.PostStatus.PENDING, Post.PostStatus.PENDING.getDescription());
        mav.addObject("postStatus", postStatusMap);
        //添加全部文章分类
        List<Category> categories = categoryService.list();
        mav.addObject("categories", categories);

        if ( post.getId() != null ) {
            //覆盖@ModelAttribute注入的"post"对象
            mav.addObject("post", post);
            //设置已选文章分类
            List<Category> postCategories = post.getCategoryList();
            for (Category category : postCategories) {
                mav.addObject(category.getName(), Boolean.TRUE);
            }
            //设置文章标签
            mav.addObject("tagList", post.getTagList());
        }
        return mav;
    }

//    private String tagList2String(List<Tag> tagList) {
//        StringBuilder strBuilder = new StringBuilder();
//        for (Tag tag : tagList) {
//            strBuilder.append(tag.getName()).append(", ");
//        }
//        return strBuilder.length()>2 ? strBuilder.substring(0, strBuilder.length()-2) : "";
//    }

    /**
     *
     * @param pendingCommentsId 若该字符串中的某个评论已通过审核，也会将其从字符串中删除
     * @return 根据pendingCommentsId得到的未审核的评论列表
     */
    private List<Comment> processPendingCommentsString(String pendingCommentsId) {
        List<Comment> comments = new ArrayList<Comment>();
        String[] commentIdStrings = pendingCommentsId.split(",");
        for (int i=0; i<commentIdStrings.length; i++) {
            Comment comment = commentService.find(Integer.parseInt(commentIdStrings[i]));
            if ( !comment.getApproved() ) {
                comments.add(comment);
            }
        }
        return comments;
    }
}
