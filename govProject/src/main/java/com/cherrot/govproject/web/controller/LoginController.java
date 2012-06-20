/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.web.controller;

import com.cherrot.govproject.model.User;
import com.cherrot.govproject.model.Usermeta;
import com.cherrot.govproject.service.UserService;
import static com.cherrot.govproject.util.Constants.ERROR_MSG_KEY;
import static com.cherrot.govproject.util.Constants.LOGIN_TO_URL;
import static com.cherrot.govproject.util.Constants.USER_CONTEXT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author cherrot
 */
@Controller
public class LoginController {

    @Inject
    private UserService userService;

    @ModelAttribute("newUser")
    public User getNewUser() {
        return new User(null, null, 0, new Date(), null);
    }

    @RequestMapping(value={"/login","/register"}, method= RequestMethod.GET)
    public String login() {
        return "/login";
    }

    /**
     * Clear user's session after logout.
     * @see BaseController#setSessionUser(javax.servlet.http.HttpSession, com.cherrot.govproject.model.User)
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
		session.removeAttribute(USER_CONTEXT);
        session.invalidate();//总是忘记他！
		return "redirect:/";
	}

    /**
     * 用户登陆
     * @param request
     * @param user
     * @return
     */
	@RequestMapping(value="/login", method= RequestMethod.POST)
	public ModelAndView doLogin(HttpServletRequest request,
        @RequestParam("username")String username, @RequestParam("password")String password) {

        // see Spring3 doc: 16.5 Resolving views -- The forward: prefix
		ModelAndView mav = new ModelAndView("/login");

        User user = userService.validateUser(username, password);
		if ( user != null) {
			BaseController.setSessionUser(request.getSession(),user);
            //toUrl is used to save the previous URL the user is visiting before doLogin.
			String toUrl = (String)request.getSession().getAttribute(LOGIN_TO_URL);
			request.getSession().removeAttribute(LOGIN_TO_URL);
			if(toUrl == null || toUrl.isEmpty()){
				toUrl = "/";
			}
			mav.setViewName("redirect:"+toUrl);
		} else { //用户验证失败
            mav.addObject(ERROR_MSG_KEY, "用户名或密码错误");
		}
		return mav;
	}

    @RequestMapping(value="/register", method= RequestMethod.POST)
    public String doRegister(HttpServletRequest request
        , @Valid @ModelAttribute("newUser")User user, BindingResult bindingResult
        , @RequestParam("birthday")Date birthday, @RequestParam("gender")String gender) {

        if (bindingResult.hasErrors()) {
            return "/login";
        }

        List<Usermeta> usermetas = new ArrayList<Usermeta>(2);
        Usermeta birthdayMeta = new Usermeta("birthday");
        birthdayMeta.setMetaValue(birthday.toString());
        Usermeta genderMeta = new Usermeta("gender");
        genderMeta.setMetaValue(gender);
        birthdayMeta.setUser(user);
        genderMeta.setUser(user);
        usermetas.add(birthdayMeta);
        usermetas.add(genderMeta);
        userService.create(user);
        BaseController.setSessionUser(request.getSession(), user);
        return "redirect:/";
    }
}
