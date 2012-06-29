/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.filter;

import com.cherrot.govproject.model.User;
import static com.cherrot.govproject.util.Constants.FILTERED_REQUEST;
import static com.cherrot.govproject.util.Constants.LOGIN_TO_URL;
import static com.cherrot.govproject.util.Constants.USER_XUANCHUANBU;
import static com.cherrot.govproject.web.controller.BaseController.getSessionUser;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 只有宣传部人员才能使用管理功能 本过滤器映射到所有 /admin 开始的URI
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //避免重复拦截
        if (request != null && request.getAttribute(FILTERED_REQUEST) != null) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute(FILTERED_REQUEST, true);
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            User sessionUser = getSessionUser(httpRequest.getSession());

            if (sessionUser == null) {
                String toUrl = httpRequest.getRequestURI();
                httpRequest.setAttribute(LOGIN_TO_URL, toUrl);
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            } else if ((sessionUser.getUserLevel() & USER_XUANCHUANBU) == 0) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendRedirect("/");
                return;
            }

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
