package com.peng.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy//开启网关代理
//@EnableEurekaClient//开启eureka客户端
//@SpringBootApplication
@SpringCloudApplication
public class LeyouGateway {
	
	public static void main(String[] args) {
		SpringApplication.run(LeyouGateway.class, args);
	}

}
