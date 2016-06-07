package com.meetu.console.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.meetu.community.domain.User;
import com.meetu.community.service.UserService;
import com.meetu.threadlocal.UserThreadLocal;
import com.meetu.util.CookieUtil;

@Controller
@RequestMapping("console")
public class ConsoleController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = userService.selectUserByLoginName(username);
			String ticket = JSONObject.toJSONString(user);
			if (user != null && user.getPassword().equals(password)) {
				UserThreadLocal.set(user);
				CookieUtil.setCookie(request, response, "TT_TICKET", ticket,
						60 * 60 * 24 * 1, true);
			} else {
				return "login";
			}

			return "index";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "login";
	}

}
