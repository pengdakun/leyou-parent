package com.peng.leyou.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.interceptor.LoginInterceptor;
import com.peng.leyou.pojo.Cart;
import com.peng.leyou.utils.JsonUtils;

/**   
* 项目名称：leyou-cart   
* 类名称：CartService   
* 类描述：   购物车service
* 创建人：彭坤   
* 创建时间：2019年1月12日 下午2:24:08      
* @version     
*/
@Service
public class CartService {
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final String CART_PREFIX="cart:user:id:";
	
	
	
	public void addCart(Cart cart) {
		//获得登录的用户
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		Long id = userInfo.getId();
		String skuId = cart.getSkuId().toString();
		Integer num = cart.getNum();
		String key=CART_PREFIX+id;
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
		//判断当前商品是否存在
		Boolean hasKey = boundHashOps.hasKey(skuId);
		if (hasKey) {//购物车存在
			//存在，修改数量
			String cartJson = (String) boundHashOps.get(skuId);
			cart = JsonUtils.parse(cartJson, Cart.class);
			cart.setNum(cart.getNum()+num);
		}
		boundHashOps.put(skuId, JsonUtils.serialize(cart));
	}



	public List<Cart> queryList() {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		String key=CART_PREFIX+userInfo.getId();
		if (!redisTemplate.hasKey(key)) {//key不存在
			throw new LeyouException(ExceptionEnum.CART_REDIS_NOT_FOUND);
		}
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
		List<Cart> carts = boundHashOps.values().stream().map(c->JsonUtils.parse(c.toString(), Cart.class)).collect(Collectors.toList());
		return carts;
	}



	public void updateNum(Long skuId, Integer num) {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		String key=CART_PREFIX+userInfo.getId();
		if (!redisTemplate.hasKey(key)) {//key不存在
			throw new LeyouException(ExceptionEnum.CART_REDIS_NOT_FOUND);
		}
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
		if (!boundHashOps.hasKey(skuId.toString())) {
			throw new LeyouException(ExceptionEnum.CART_REDIS_NOT_FOUND);
		}
		String json=(String) boundHashOps.get(skuId.toString());
		Cart cart = JsonUtils.parse(json, Cart.class);
		cart.setNum(num);
		boundHashOps.put(skuId.toString(), JsonUtils.serialize(cart));
	}



	public void deleteCart(Long skuId) {
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		String key=CART_PREFIX+userInfo.getId();
		BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
		boundHashOps.delete(skuId.toString());
	}
	
	
	
}
