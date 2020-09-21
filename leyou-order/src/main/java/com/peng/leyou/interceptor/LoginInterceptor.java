package com.peng.leyou.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.descriptor.tld.TldParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.properties.JWTProperties;
import com.peng.leyou.utils.CookieUtils;
import com.peng.leyou.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

/**   
* 项目名称：leyou-cart   
* 类名称：LoginInterceptor   
* 类描述：   是否登录拦截
* 创建人：彭坤   
* 创建时间：2019年1月9日 下午10:30:52      
* @version     
*/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
	
	private static ThreadLocal<UserInfo> tl=new ThreadLocal<UserInfo>();
	
	private JWTProperties jwtProperties;
	
	public LoginInterceptor(JWTProperties jwtProperties) {
		this.jwtProperties=jwtProperties;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//清除数据
		tl.remove();
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	//前置拦截
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
			UserInfo userinfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
//			request.setAttribute("userinfo", userinfo);
			//存到线程共享
			tl.set(userinfo);
			return true;
		} catch (Exception e) {
			log.error("[购物车服务] 加入购物车，用户未登录",e);
			return false;
		}
	}
	
	
	public static UserInfo getUserInfo() {
		return tl.get();
	}
	

}
