package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.peng.leyou.mapper")
@EnableEurekaClient
@SpringBootApplication
public class LeyouUserService {
	
	public static void main(String[] args) {
		SpringApplication.run(LeyouUserService.class, args);
	}

}
