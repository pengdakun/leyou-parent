package com.peng.leyou.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.peng.leyou.interceptor.LoginInterceptor;
import com.peng.leyou.properties.JWTProperties;

@Configuration
@EnableConfigurationProperties(JWTProperties.class)
public class MvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private JWTProperties jwtProperties;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor(jwtProperties)).addPathPatterns("/**");
	}
	
	

}
