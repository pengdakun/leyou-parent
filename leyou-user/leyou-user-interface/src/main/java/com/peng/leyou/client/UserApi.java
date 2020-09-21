package com.peng.leyou.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peng.leyou.pojo.User;

public interface UserApi {

	
	@GetMapping("/query")
	User queryUserByUserNameAndPassWord(@RequestParam("username") String username,@RequestParam("password") String password);
	
}
