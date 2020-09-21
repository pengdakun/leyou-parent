package com.peng.leyou.mq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peng.leyou.service.PageService;

/**   
* 项目名称：leyou-search   
* 类名称：ItemListener   
* 类描述：   监听rabbitmq消息
* 创建人：彭坤   
* 创建时间：2019年1月1日 下午9:51:45      
* @version     
*/
@Component
public class ItemListener {
	
	@Autowired
	private PageService pageService;
	
	
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(name="page.item.insertOrUpdate.queue",durable="true"),
			exchange=@Exchange(name="leyou.item.exchange",type=ExchangeTypes.TOPIC),
			key= {"item.insert","item.update"}
			))
	public void insertOrUpdate(Long spuId) {
		if (spuId==null) {
			return;
		}
		//处理消息
		pageService.createHtml(spuId);
	}
	
	
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(name="page.item.delete.queue",durable="true"),
			exchange=@Exchange(name="leyou.item.exchange",type=ExchangeTypes.TOPIC),
			key= {"item.delete"}
			))
	public void delete(Long spuId) {
		if (spuId==null) {
			return;
		}
		//处理消息
		pageService.deleteHtml(spuId);
	}
	

}
