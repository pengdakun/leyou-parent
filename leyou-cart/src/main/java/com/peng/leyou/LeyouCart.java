package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//添加注释dev dev dev
@EnableEurekaClient
@SpringBootApplication
public class LeyouCart {
	public static void main(String[] args) {
		SpringApplication.run(LeyouCart.class, args);
	}
}
