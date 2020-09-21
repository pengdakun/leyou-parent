package com.peng.leyou.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.pojo.Cart;
import com.peng.leyou.service.CartService;


/**   
* 项目名称：leyou-cart   
* 类名称：CartController   
* 类描述：   购物车controller
* 创建人：彭坤   
* 创建时间：2019年1月12日 下午2:18:49      
* @version     
*/
@RestController
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	/** 
	 * @param @param cart
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 新增购物车到redis
	 * @author 彭坤
	 * @date 2019年1月12日 下午2:21:00
	 */
	@PostMapping
	public ResponseEntity<Void> addCart(@RequestBody Cart cart){
		cartService.addCart(cart);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/** 
	 * @param @return 
	 * @return ResponseEntity<List<Cart>>  
	 * @Description 从redis中查询数据
	 * @author 彭坤
	 * @date 2019年1月12日 下午3:14:41
	 */
	@GetMapping("/list")
	public ResponseEntity<List<Cart>> list(){
		return ResponseEntity.ok(cartService.queryList());
	}
	
	/** 
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 修改购物车数量
	 * @author 彭坤
	 * @date 2019年1月12日 下午3:31:16
	 */
	@PutMapping
	public ResponseEntity<Void> updateNum(@RequestParam("id") Long skuId,@RequestParam("num") Integer num){
		cartService.updateNum(skuId,num);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	/** 
	 * @param @param skuId
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 删除购物车
	 * @author 彭坤
	 * @date 2019年1月12日 下午3:53:04
	 */
	@DeleteMapping("/{skuId}")
	public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId){
		cartService.deleteCart(skuId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	

}
