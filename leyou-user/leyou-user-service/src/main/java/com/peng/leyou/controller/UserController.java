package com.peng.leyou.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.pojo.User;
import com.peng.leyou.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/** 
	 * @param @param data
	 * @param @param type
	 * @param @return 
	 * @return ResponseEntity<Boolean>  
	 * @Description 校验数据
	 * @author 彭坤
	 * @date 2019年1月5日 下午2:23:58
	 */
	@GetMapping("/check/{data}/{type}")
	public ResponseEntity<Boolean> checkData(@PathVariable(value="data") String data,@PathVariable(value="type") Integer type){
		return ResponseEntity.ok(userService.checkData(data,type));		
	}
	
	/** 
	 * @param @param phone
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 发送验证码
	 * @author 彭坤
	 * @date 2019年1月5日 下午2:24:13
	 */
	@PostMapping("/code")
	public ResponseEntity<Void> sendMsg(@RequestParam("phone") String phone) {
		userService.sendMsg(phone);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/** 
	 * @param @param user
	 * @param @param code
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 用户注册
	 * @author 彭坤
	 * @date 2019年1月5日 下午3:18:15
	 */
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid User user,BindingResult bindingResult,@RequestParam("code") String code){
		if (bindingResult.hasErrors()) {
			throw new RuntimeException(bindingResult.getFieldErrors().stream().map(s->s.getDefaultMessage()).collect(Collectors.joining("-")));
		}
		userService.userService(user,code);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/** 
	 * @param @param username
	 * @param @param password
	 * @param @return 
	 * @return ResponseEntity<User>  
	 * @Description 通过用户名及密码查询用户
	 * @author 彭坤
	 * @date 2019年1月6日 下午1:35:11
	 */
	@GetMapping("/query")
	public ResponseEntity<User> queryUserByUserNameAndPassWord(@RequestParam("username") String username,@RequestParam("password") String password){
		return ResponseEntity.ok(userService.queryUserByUserNameAndPassWord(username,password));
		
	}
	

}
