package com.meetu.console.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.meetu.community.domain.User;
import com.meetu.community.service.UserService;
import com.meetu.threadlocal.UserThreadLocal;
import com.meetu.util.CookieUtil;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String ticket = CookieUtil.getCookieValue(request, "TT_TICKET", true);
		User user = null;
		if (StringUtils.isBlank(ticket)) {
			response.sendRedirect("/page/login");
			return false;
		}
		user = JSONObject.parseObject(ticket, User.class);
		if (user == null) {
			// 用户登录超时，跳转到登陆页面
			response.sendRedirect("/page/login");
			return false;
		}
		// 用户已登录
		// 把用户信息放到ThreadLocal中
		UserThreadLocal.set(user);
		return true;

	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
