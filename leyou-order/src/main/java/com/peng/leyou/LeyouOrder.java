package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import tk.mybatis.spring.annotation.MapperScan;

@EnableFeignClients
@MapperScan("com.peng.leyou.mapper")
@EnableEurekaClient
@SpringBootApplication
public class LeyouOrder {
	
	public static void main(String[] args) {
		SpringApplication.run(LeyouOrder.class, args);
	}

}
