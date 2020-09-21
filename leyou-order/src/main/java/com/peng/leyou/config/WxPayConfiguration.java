package com.peng.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants.SignType;

@Configuration
public class WxPayConfiguration {
	
	@Bean
	@ConfigurationProperties("leyou.pay")
	public PayConfig payConfig() {
		return new PayConfig();
	}
	
	@Bean
	public WXPay wxPay(PayConfig payConfig) {
		return new WXPay(payConfig,SignType.HMACSHA256);
	}
	

}
