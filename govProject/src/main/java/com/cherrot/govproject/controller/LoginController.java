/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.service.UserService;
import com.cherrot.govproject.util.Constants;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cherrot
 */
@Controller
@RequestMapping("/user/")
public class LoginController extends BaseController {

    @Inject
    UserService userService;

    /**
     * 用户登陆
     * @param request
     * @param user
     * @return
     */
	@RequestMapping("doLogin")
	public ModelAndView login(HttpServletRequest request,
        @RequestParam("username")String username, @RequestParam("password")String password) {

		User dbUser = userService.findByLoginName(username, false, false, false);
		ModelAndView mav = new ModelAndView();
        // see Spring3 doc: 16.5 Resolving views -- The forward: prefix
		mav.setViewName("/home");
		if (dbUser == null) {
			mav.addObject(Constants.ERROR_MSG_KEY, "用户名不存在");
		} else if ( ! dbUser.getPass().equals(password) ) {
			mav.addObject(Constants.ERROR_MSG_KEY, "用户密码不正确");
		} else {
			setSessionUser(request.getSession(),dbUser);
            //toUrl is used to save the previous URL the user is visiting before login.
			String toUrl = (String)request.getSession().getAttribute(Constants.LOGIN_TO_URL);
			request.getSession().removeAttribute(Constants.LOGIN_TO_URL);
			if(toUrl == null || toUrl.isEmpty()){
				toUrl = "/";
			}
			mav.setViewName("redirect:"+toUrl);
		}
		return mav;
	}

    /**
     * Clear user's session after logout.
     * @see BaseController#setSessionUser(javax.servlet.http.HttpSession, com.cherrot.govproject.model.User)
     * @param session
     * @return
     */
    @RequestMapping("doLogout")
    public String logout(HttpSession session) {
		session.removeAttribute(Constants.USER_CONTEXT);
		return "redirect:/";
	}
}
