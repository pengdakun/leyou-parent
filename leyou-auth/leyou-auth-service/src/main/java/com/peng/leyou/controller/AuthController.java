package com.peng.leyou.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.creation.SuspendMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.pojo.User;
import com.peng.leyou.properties.JWTProperties;
import com.peng.leyou.service.AuthService;
import com.peng.leyou.utils.CookieUtils;
import com.peng.leyou.utils.JwtUtils;

/**   
* 项目名称：leyou-auth-service   
* 类名称：AuthController   
* 类描述：   用户授权
* 创建人：彭坤   
* 创建时间：2019年1月6日 下午8:47:29      
* @version     
*/
@RestController
@EnableConfigurationProperties(JWTProperties.class)
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Value("${leyou.jwt.cookieName}")
	private String cookieName;
	
	@Autowired
	private JWTProperties jwtProperties;
	
	/** 
	 * @param @param username
	 * @param @param password
	 * @param @return 
	 * @return ResponseEntity<String>  
	 * @Description 用户登录
	 * @author 彭坤
	 * @date 2019年1月6日 下午8:47:37
	 */
	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestParam("username") String username,@RequestParam("password") String password,HttpServletRequest request,HttpServletResponse response){
		String token = authService.login(username,password);
		System.out.println(token+"------------"+cookieName);
		CookieUtils.setCookie(request, response, cookieName, token, false);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/** 
	 * @param @param request
	 * @param @return 
	 * @return ResponseEntity<User>  
	 * @Description 验证用户是否登录
	 * @author 彭坤
	 * @date 2019年1月7日 下午9:01:51
	 */
	@GetMapping("/verify")
	public ResponseEntity<UserInfo> verify(@CookieValue(value="LEYOU_TOKEN",required=false) String cookieValue,HttpServletRequest request,HttpServletResponse response){
		try {
			UserInfo userInfo = JwtUtils.getInfoFromToken(cookieValue, jwtProperties.getPublicKey());
			//重新生成taken,刷新token有效时间
			String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
			CookieUtils.setCookie(request, response, cookieName, token, false);
			
			return ResponseEntity.ok(userInfo);
		} catch (Exception e) {
			throw new LeyouException(ExceptionEnum.UNAUTHORIZED);
		}
	}

	
	
}
