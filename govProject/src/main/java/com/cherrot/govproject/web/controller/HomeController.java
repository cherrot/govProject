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
        topLevelCategorys.remove(categoryService.findBySlug("hidden", false, false));
        topLevelCategorys.remove(categoryService.findImageCategory(false));
        topLevelCategorys.remove(categoryService.findVideoCategory(false));

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
            userService.findByLoginName("admin@cherrot.com", false, false, false, false);
        } catch (Exception ex) {
            User user = new User("admin@cherrot.com", "admin", Constants.USER_XUANCHUANBU, new Date(), "管理员");
            userService.create(user);
            //创建测试分类
            Category groupHidden = new Category("隐藏群组", "hidden");
            Category group1 = new Category("首页群组1", "group1");
            Category group2 = new Category("首页群组2", "group2");
            Category group3 = new Category("首页群组3", "group3");
            Category group4 = new Category("首页群组4", "group4");
            Category group5 = new Category("首页群组5", "group5");
            Category groupImage = new Category("图片", "image");
            Category groupVideo = new Category("视频", "video");
            Category wenxue = new Category("文学作品", "wenxue");
            Category yingshi = new Category("影视作品", "yingshi");
            Category mingren = new Category("文化名人", "mingren");
            Category fushi = new Category("服饰风采", "fushi");
            Category zongjiao = new Category("宗教文化", "zongjiao");
            Category fengjing = new Category("风景名胜", "fengjing");
            Category bowuguan = new Category("博物馆", "bowuguan");
            Category tiyuguan = new Category("体育馆", "tiyuguan");
            Category yichan = new Category("非物质文化遗产", "feiwuzhiyichan");
            Category shufa = new Category("书法作品", "shufa");
            Category dongman = new Category("动漫作品", "dongman");
            Category chanye = new Category("文化产业", "wenhuachanye");
            Category jieqing = new Category("节庆活动", "jieqing");
            Category meishi = new Category("民间美食", "meishi");
            Category qiye = new Category("文化企业", "qiye");
            Category tushuguan = new Category("图书馆", "tushuguan");
            Category jianzhu = new Category("建筑", "jianzhu");
            Category aiguozhuyi = new Category("爱国主义教育基地", "爱国主义教育基地");
            Category meishu = new Category("美术作品", "meishu");
            Category wutaiyishu = new Category("舞台艺术", "wutaiyishu");
            Category wenhuajiaoliu = new Category("文化交流", "wenhuajiaoliu");
            Category minsu = new Category("民风民俗", "minsu");
            Category fangyan = new Category("民间方言", "fangyan");
            Category lishi = new Category("历史事件", "lishi");
            Category meishuguan = new Category("美术馆", "meishuguan");
            Category mingrenguju = new Category("名人故人居", "mingrenguju");
            Category sheying = new Category("摄影作品", "sheying");
            Category gongyi = new Category("工艺美术", "gongyi");
            Category huodong = new Category("文化活动", "wenhuahuodong");
            Category tiyu = new Category("体育竞技", "tiyu");
            Category techan = new Category("名特产品", "techan");
            Category wenwu = new Category("各级文物", "wenwu");
            Category wenhuaguan = new Category("文化馆", "wenhuaguan");
            Category chuanshuo = new Category("民间传说故事", "chuanshuo");
            categoryService.create(groupHidden);
            categoryService.create(group1);
            categoryService.create(group2);
            categoryService.create(group3);
            categoryService.create(group4);
            categoryService.create(group5);
            categoryService.create(groupImage);
            categoryService.create(groupVideo);
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
