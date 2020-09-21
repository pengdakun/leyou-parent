package com.peng.louyou.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.peng.leyou.LeyouSms;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=LeyouSms.class)
public class SmsTest {
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	@Test
	public void test() {
		try {
			Map<String, String> map = new HashMap<String,String>();
			map.put("phone", "15797704768");
			map.put("code", "5698");
			amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",map);
			Thread.sleep(10000);
		} catch (AmqpException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
