package com.peng.leyou.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.infix.lang.infix.antlr.EventFilterParser.regex_predicate_return;
import com.peng.leyou.service.OrderService;

import lombok.extern.slf4j.Slf4j;

/**   
* 项目名称：leyou-order   
* 类名称：NotifyController   
* 类描述：   微信支付成功回调
* 创建人：彭坤   
* 创建时间：2019年1月19日 下午3:04:52      
* @version     
*/
@Slf4j
@RestController
@RequestMapping("/notive")
public class NotifyController {
	
	@Autowired
	private OrderService orderService;

	/** 
	 * @param @param map 
	 * @return void  
	 * @Description 微信支付成功回调
	 * @author 彭坤
	 * @date 2019年1月19日 下午3:25:59
	 */
	@PostMapping(produces="application/xml")//返回xml类型
	public Map<String, String> notifyUrl(@RequestBody Map<String, String> map) {
		//处理回调
		orderService.handleNotify(map);
		log.info("[微信支付成功回调] 订单支付成功,结果{}",map);
		Map<String, String> result = new HashMap<String,String>();
		result.put("return_code", "SUCCESS");
		result.put("return_msg", "OK");
		return result;
		
	}

	
}
