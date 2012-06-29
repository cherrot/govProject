package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.User;
import static com.cherrot.govproject.util.Constants.USER_CONTEXT;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.util.Assert;

/**
 * Super class of all Controllers
 */
public class BaseController {

    /**
     * 获取保存在Session中的用户对象
     *
     * @param request
     * @return
     */
    public static User getSessionUser(HttpSession session) {
        return (User) session.getAttribute(USER_CONTEXT);
    }

    /**
     * 保存用户对象到Session中
     *
     * @param session
     * @param user
     */
    public static void setSessionUser(HttpSession session, User user) {
        session.setAttribute(USER_CONTEXT, user);
    }

    /**
     * 获取基于应用程序的url绝对路径
     *
     * @param request
     * @param url 以"/"打头的URL地址
     * @return 基于应用程序的url绝对路径
     */
    public static String getAppbaseUrl(HttpServletRequest request, String url) {
        Assert.hasLength(url, "url不能为空");
        Assert.isTrue(url.startsWith("/"), "必须以/打头");
        return request.getContextPath() + url;
    }
}
