package com.peng.leyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.peng.leyou.client.UserClient;
import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.pojo.User;
import com.peng.leyou.properties.JWTProperties;
import com.peng.leyou.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableConfigurationProperties(JWTProperties.class)
public class AuthService {
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private JWTProperties jwtProperties;

	public String login(String username, String password) {
		try {
			User user = userClient.queryUserByUserNameAndPassWord(username, password);
			if (user==null) {
				throw new LeyouException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
			}
			String token=JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
			return token;
		} catch (Exception e) {
			log.error("[授权中心] 用户名或密码错误,用户名称:{}",username,e);
			throw new LeyouException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
		}
	}

}
