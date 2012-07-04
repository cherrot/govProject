/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Comment;
import com.cherrot.govproject.model.Link;
import com.cherrot.govproject.model.LinkCategory;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.CommentService;
import com.cherrot.govproject.service.LinkService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.TagService;
import com.cherrot.govproject.service.UserService;
import com.cherrot.govproject.util.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 关于forward: 和 redirect: ，见Spring3.1文档P474
 *
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

    /**
     * 顶部导航栏的文章分类
     *
     * @return
     */
    @ModelAttribute("categories")
    public List<Category> getSecondLevelCategoryList() {
        //XXX 仅用于生成测试数据！
        initData();
        return categoryService.listSecondLevelCategories(false, false);
    }

    /**
     * 友情链接分类和分类下的友情链接
     *
     * @return
     */
    @ModelAttribute("linkCategories")
    public List<LinkCategory> getLinkCategoryList() {
        return linkService.listLinkCategories(true);
    }

    @RequestMapping("/")
    public ModelAndView home() {

        ModelAndView mav = new ModelAndView("home");

        //添加首页主体部分的文章群组分类和该分类的最新文章
        List<Category> topLevelCategorys = categoryService.listTopLevelCategories(true);
        mav.addObject("categoryGroups", topLevelCategorys);
        for (Category group : topLevelCategorys) {
            for (Category category : group.getCategoryList()) {
                mav.addObject(category.getName(), postService.listNewestPostsByCategory(category, 1, 5));
            }
        }
        //添加多媒体文章
        List<Post> imagePosts = postService.listNewestImagePosts(1, 5);
        mav.addObject("imagePosts", imagePosts);
        return mav;
    }
    /**
     * TODO 以下内容仅用于生成测试数据！
     */
    @Inject
    private CommentService commentService;
    @Inject
    private UserService userService;
    @Inject
    private TagService tagService;

    private void initData() {
        try {
            userService.findByLoginName("f@f.f", false, false, false, false);
        } catch (Exception ex) {
            //创建测试用户 用户名 f@f.f 密码 fff
            User user = new User("f@f.f", "fff", Constants.USER_XUANCHUANBU, new Date(), "Cherrot");
            userService.create(user);
            //创建测试分类
            Category group1 = new Category(0, "分类群组1", "group1");
            Category group2 = new Category(0, "分类群组2", "group2");
            Category group3 = new Category(0, "分类群组3", "group3");
            Category group4 = new Category(0, "分类群组4", "group4");
            Category group5 = new Category(0, "分类群组5", "group5");
            Category groupHidden = new Category(0, "隐藏群组", "hidden");
            Category groupMultimedia = new Category(0, "多媒体分组", "multimedia");
            Category wenxue = new Category(0, "文学作品", "wenxue");
            Category yingshi = new Category(0, "影视作品", "yingshi");
            Category mingren = new Category(0, "文化名人", "mingren");
            Category fushi = new Category(0, "服饰风采", "fushi");
            Category zongjiao = new Category(0, "宗教文化", "zongjiao");
            Category fengjing = new Category(0, "风景名胜", "fengjing");
            Category bowuguan = new Category(0, "博物馆", "bowuguan");
            Category tiyuguan = new Category(0, "体育馆", "tiyuguan");
            Category yichan = new Category(0, "非物质文化遗产", "feiwuzhiyichan");
            Category shufa = new Category(0, "书法作品", "shufa");
            Category dongman = new Category(0, "动漫作品", "dongman");
            Category chanye = new Category(0, "文化产业", "wenhuachanye");
            Category jieqing = new Category(0, "节庆活动", "jieqing");
            Category meishi = new Category(0, "民间美食", "meishi");
            Category qiye = new Category(0, "文化企业", "qiye");
            Category tushuguan = new Category(0, "图书馆", "tushuguan");
            Category jianzhu = new Category(0, "建筑", "jianzhu");
            Category aiguozhuyi = new Category(0, "爱国主义教育基地", "爱国主义教育基地");
            Category meishu = new Category(0, "美术作品", "meishu");
            Category wutaiyishu = new Category(0, "舞台艺术", "wutaiyishu");
            Category wenhuajiaoliu = new Category(0, "文化交流", "wenhuajiaoliu");
            Category minsu = new Category(0, "民风民俗", "minsu");
            Category fangyan = new Category(0, "民间方言", "fangyan");
            Category lishi = new Category(0, "历史事件", "lishi");
            Category meishuguan = new Category(0, "美术馆", "meishuguan");
            Category mingrenguju = new Category(0, "名人故人居", "mingrenguju");
            Category sheying = new Category(0, "摄影作品", "sheying");
            Category gongyi = new Category(0, "工艺美术", "gongyi");
            Category huodong = new Category(0, "文化活动", "wenhuahuodong");
            Category tiyu = new Category(0, "体育竞技", "tiyu");
            Category techan = new Category(0, "名特产品", "techan");
            Category wenwu = new Category(0, "各级文物", "wenwu");
            Category wenhuaguan = new Category(0, "文化馆", "wenhuaguan");
            Category chuanshuo = new Category(0, "民间传说故事", "chuanshuo");
            categoryService.create(group1);
            categoryService.create(group2);
            categoryService.create(group3);
            categoryService.create(group4);
            categoryService.create(group5);
            categoryService.create(groupMultimedia);
            categoryService.create(groupHidden);
            wenxue.setCategoryParent(group1);
            shufa.setCategoryParent(group1);
            meishu.setCategoryParent(group1);
            sheying.setCategoryParent(group1);
            yingshi.setCategoryParent(group2);
            dongman.setCategoryParent(group2);
            wutaiyishu.setCategoryParent(group2);
            gongyi.setCategoryParent(group2);
            mingren.setCategoryParent(group3);
            chanye.setCategoryParent(group3);
            wenhuajiaoliu.setCategoryParent(group3);
            huodong.setCategoryParent(group3);
            fushi.setCategoryParent(group4);
            jieqing.setCategoryParent(group4);
            minsu.setCategoryParent(group4);
            meishi.setCategoryParent(group4);
            fengjing.setCategoryParent(group5);
            lishi.setCategoryParent(group5);
            mingrenguju.setCategoryParent(group5);
            bowuguan.setCategoryParent(group5);
            tiyuguan.setCategoryParent(groupHidden);
            yichan.setCategoryParent(groupHidden);
            qiye.setCategoryParent(groupHidden);
            tushuguan.setCategoryParent(groupHidden);
            jianzhu.setCategoryParent(groupHidden);
            aiguozhuyi.setCategoryParent(groupHidden);
            fangyan.setCategoryParent(groupHidden);
            meishuguan.setCategoryParent(groupHidden);
            tiyu.setCategoryParent(groupHidden);
            techan.setCategoryParent(groupHidden);
            wenwu.setCategoryParent(groupHidden);
            wenhuaguan.setCategoryParent(groupHidden);
            chuanshuo.setCategoryParent(groupHidden);
            zongjiao.setCategoryParent(groupHidden);
            categoryService.create(wenxue);
            categoryService.create(shufa);
            categoryService.create(meishu);
            categoryService.create(sheying);
            categoryService.create(yingshi);
            categoryService.create(dongman);
            categoryService.create(wutaiyishu);
            categoryService.create(gongyi);
            categoryService.create(mingren);
            categoryService.create(chanye);
            categoryService.create(wenhuajiaoliu);
            categoryService.create(huodong);
            categoryService.create(fushi);
            categoryService.create(jieqing);
            categoryService.create(minsu);
            categoryService.create(meishi);
            categoryService.create(fengjing);
            categoryService.create(lishi);
            categoryService.create(mingrenguju);
            categoryService.create(bowuguan);
            categoryService.create(tiyuguan);
            categoryService.create(yichan);
            categoryService.create(qiye);
            categoryService.create(tushuguan);
            categoryService.create(jianzhu);
            categoryService.create(aiguozhuyi);
            categoryService.create(fangyan);
            categoryService.create(meishuguan);
            categoryService.create(tiyu);
            categoryService.create(techan);
            categoryService.create(wenwu);
            categoryService.create(wenhuaguan);
            categoryService.create(chuanshuo);
            categoryService.create(zongjiao);
            //创建测试文章标签
            Tag tag = new Tag(0, "测试", "testtag");
            Tag tag1 = new Tag(0, "文章标签", "testtag2");
            tagService.create(tag);
            tagService.create(tag1);
            //创建测试文章
            Post post = new Post(new Date(), new Date(), true, 0, Post.PostStatus.PUBLISHED, Post.PostType.POST, "test", "我是文章标题", "我是文章内容");
            post.setUser(user);
            List<Category> categoryList = new ArrayList<Category>();
            categoryList.add(wenxue);
            categoryList.add(sheying);
            categoryList.add(zongjiao);
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
            //创建测试链接分类和链接
            LinkCategory wenhuaLinkCategory = new LinkCategory();
            wenhuaLinkCategory.setName("文化相关网站");
            LinkCategory officialLinkCategory = new LinkCategory();
            officialLinkCategory.setName("官网链接");
            linkService.createLinkCategory(wenhuaLinkCategory);
            linkService.createLinkCategory(officialLinkCategory);
            Link yinyuejiaxiehui = new Link("http://www.chnmusic.org/", "中国音乐家协会", Link.LinkTarget._blank);
            Link yishujiaxiehui = new Link("http://www.chinaaa.org.cn/", "中国艺术家协会", Link.LinkTarget._blank);
            Link sheyingjiaxiehui = new Link("http://www.cpanet.cn/cms/index.html", "中国摄影家协会", Link.LinkTarget._blank);
            Link wudaojiaxiehui = new Link("http://www.danceinchina.org/web/index.php", "中国舞蹈家协会", Link.LinkTarget._blank);
            Link shufajiaxiehui = new Link("http://www.ccagov.com.cn/", "中国书法家协会", Link.LinkTarget._blank);
            Link bowuguanLink = new Link("http://www.kmmuseum.com/", "昆明博物馆", Link.LinkTarget._blank);
            Link xinxigang = new Link("http://www.kunming.cn/", "昆明信息港", Link.LinkTarget._blank);
            Link dianshitai = new Link("http://www.kmtv.com.cn/OnlineTV/SeeList.asp?Sort_ID=42", "昆明电视台", Link.LinkTarget._blank);
            Link kunmingribao = new Link("http://daily.clzg.cn/html/2012-04/22/node_281.htm", "昆明日报", Link.LinkTarget._blank);
            yinyuejiaxiehui.setLinkCategory(wenhuaLinkCategory);
            yishujiaxiehui.setLinkCategory(wenhuaLinkCategory);
            sheyingjiaxiehui.setLinkCategory(wenhuaLinkCategory);
            wudaojiaxiehui.setLinkCategory(wenhuaLinkCategory);
            shufajiaxiehui.setLinkCategory(wenhuaLinkCategory);
            bowuguanLink.setLinkCategory(officialLinkCategory);
            xinxigang.setLinkCategory(officialLinkCategory);
            dianshitai.setLinkCategory(officialLinkCategory);
            kunmingribao.setLinkCategory(officialLinkCategory);
            linkService.create(yinyuejiaxiehui);
            linkService.create(yishujiaxiehui);
            linkService.create(sheyingjiaxiehui);
            linkService.create(wudaojiaxiehui);
            linkService.create(shufajiaxiehui);
            linkService.create(bowuguanLink);
            linkService.create(xinxigang);
            linkService.create(dianshitai);
            linkService.create(kunmingribao);
        }
    }
}
