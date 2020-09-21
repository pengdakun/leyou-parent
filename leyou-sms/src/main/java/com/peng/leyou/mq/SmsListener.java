package com.peng.leyou.mq;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aliyuncs.exceptions.ClientException;
import com.peng.leyou.properties.SmsProperties;
import com.peng.leyou.sms.util.SmsUtil;
import com.peng.leyou.utils.JsonUtils;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
	
	@Autowired
	private SmsUtil smsUtil;
	
	@Autowired
	private SmsProperties smsProperties;
	
	//发送短息验证码监听器
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(name="sms.verify.code.queue",durable="true"),
			exchange=@Exchange(name="leyou.sms.exchange",type=ExchangeTypes.TOPIC),
			key= {"sms.verify.code"}
			))
	public void sms(Map<String, String> map) {
		
		if (CollectionUtils.isEmpty(map)) {
			return;
		}
		
		String phone = map.remove("phone");
		if (StringUtils.isBlank(phone)) {
			return;
		}
		
		smsUtil.sendSms(phone, JsonUtils.serialize(map), smsProperties.getVerifyCodeTemplate(), smsProperties.getSignName());
	}
	

}
