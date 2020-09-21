package com.peng.leyou.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.properties.FilterProperties;
import com.peng.leyou.properties.JWTProperties;
import com.peng.leyou.utils.CookieUtils;
import com.peng.leyou.utils.JwtUtils;

/**   
* 项目名称：leyou-gateway   
* 类名称：AuthFilter   
* 类描述：   用户登录连接校验
* 创建人：彭坤   
* 创建时间：2019年1月7日 下午9:27:31      
* @version     
*/
@Component
@EnableConfigurationProperties(value= {JWTProperties.class,FilterProperties.class})
public class AuthFilter extends ZuulFilter{
	
	@Autowired
	private JWTProperties jwtProperties;
	
	@Autowired
	private FilterProperties filterProperties;

	@Override
	public boolean shouldFilter() {//是否启用过滤器
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestURI = request.getRequestURI();
		//判断是否放行
		return !isAllowPath(requestURI);
	}

	
	private boolean isAllowPath(String requestURI) {
		List<String> allowPaths = filterProperties.getAllowPaths();
		for (String allowPath: allowPaths) {
			if(requestURI.startsWith(allowPath)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object run() throws ZuulException {//过滤规则
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		//获取token
		String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
		//解析token
		try {
			UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
			//校验用户权限
		} catch (Exception e) {
			//未登录，拦截
			ctx.setSendZuulResponse(false);//拦截
			ctx.setResponseStatusCode(403);//返回状态码
		}
		return null;
	}

	@Override
	public String filterType() {//过滤器类型
		return FilterConstants.PRE_TYPE;//前置拦截
	}

	@Override
	public int filterOrder() {//过滤器顺序
		return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;//在官方过滤器之前
	}

}
