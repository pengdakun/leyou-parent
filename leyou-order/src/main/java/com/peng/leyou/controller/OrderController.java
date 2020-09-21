package com.peng.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.dto.OrderDTO;
import com.peng.leyou.pojo.Order;
import com.peng.leyou.service.OrderService;

/**   
* 项目名称：leyou-order   
* 类名称：OrderController   
* 类描述：   订单controller
* 创建人：彭坤   
* 创建时间：2019年1月13日 下午2:28:46      
* @version     
*/
@RestController("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	/** 
	 * @param @param orderDTO
	 * @param @return 
	 * @return ResponseEntity<Long>  
	 * @Description 创建订单
	 * @author 彭坤
	 * @date 2019年1月13日 下午2:28:54
	 */
	@PostMapping
	public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
		return ResponseEntity.ok(orderService.createOrder(orderDTO));
	}
	
	/** 
	 * @param @param oid
	 * @param @return 
	 * @return ResponseEntity<Order>  
	 * @Description 通过id查询订单
	 * @author 彭坤
	 * @date 2019年1月14日 下午9:16:34
	 */
	@GetMapping("/{oid}")
	public ResponseEntity<Order> findOrderById(@PathVariable("oid") Long oid){
		return ResponseEntity.ok(orderService.findOrderById(oid));
	}
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return ResponseEntity<String>  
	 * @Description 创建支付链接
	 * @author 彭坤
	 * @date 2019年1月16日 下午8:59:00
	 */
	@GetMapping("/url/{id}")
	public ResponseEntity<String> createPayUrl(@PathVariable("id") Long id){
		return ResponseEntity.ok(orderService.createPayUrl(id));
	}
	
	/** 
	 * @param @param orderId
	 * @param @return 
	 * @return ResponseEntity<Integer>  
	 * @Description 查询订单状态
	 * @author 彭坤
	 * @date 2019年1月19日 下午4:45:40
	 */
	@GetMapping("/state/{id}")
	public ResponseEntity<Integer> queryOrderStatus(@PathVariable("id") Long orderId){
		return ResponseEntity.ok(orderService.queryOrderStatus(orderId));
	}

}
