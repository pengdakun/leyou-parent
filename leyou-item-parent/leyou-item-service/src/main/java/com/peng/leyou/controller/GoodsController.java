package com.peng.leyou.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.dto.CartDTO;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.pojo.Sku;
import com.peng.leyou.pojo.Spu;
import com.peng.leyou.pojo.SpuDetail;
import com.peng.leyou.service.impl.GoodsService;

/**   
* 项目名称：leyou-item-service   
* 类名称：GoodsController   
* 类描述：   商品相关controller
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午3:30:39      
* @version     
*/
@RestController
@RequestMapping("/spu")
public class GoodsController {
	
	@Autowired
	private GoodsService goodsService;
	
	/** 
	 * @param @param key
	 * @param @param saleable
	 * @param @param page
	 * @param @param rows
	 * @param @return spu分页条件查询
	 * @return ResponseEntity<PageResult<Spu>>  
	 * @Description 
	 * @author 彭坤
	 * @date 2018年12月16日 下午3:47:31
	 */
	@GetMapping("/page")
	public ResponseEntity<PageResult<Spu>> findSpuByPage(@RequestParam(value="key",required=false) String key,@RequestParam("saleable") Boolean saleable,
			@RequestParam("page") Integer page,@RequestParam("rows") Integer rows){
		return ResponseEntity.ok(goodsService.findSpuByPage(key,saleable,page,rows));
	}
	
	/** 
	 * @param @param spu
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 添加商品
	 * @author 彭坤
	 * @date 2018年12月16日 下午10:45:14
	 */
	@PostMapping("/goods")
	public ResponseEntity<Void> addGoods(@RequestBody Spu spu){
		goodsService.addGoods(spu);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return ResponseEntity<SpuDetail>  
	 * @Description 通过spuId查询spuDetail
	 * @author 彭坤
	 * @date 2018年12月17日 下午9:53:13
	 */
	@GetMapping("/spu/detail/{id}")
	public ResponseEntity<SpuDetail> queryDetailBySpuId(@PathVariable("id") Long spuId){
		return ResponseEntity.ok(goodsService.queryDetailBySpuId(spuId));
	}
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return ResponseEntity<List<Spu>>  
	 * @Description 通过id查询sku
	 * @author 彭坤
	 * @date 2018年12月17日 下午10:01:09
	 */
	@GetMapping("/list/ids")
	public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id){
		return ResponseEntity.ok(goodsService.querySkuBySpuId(id));
	}
	
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return ResponseEntity<Spu>  
	 * @Description 通过id查询spu
	 * @author 彭坤
	 * @date 2018年12月27日 下午9:41:17
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
		return ResponseEntity.ok(goodsService.querySpuById(id));
	}
	
	
	/** 
	 * @param @param ids
	 * @param @return 
	 * @return ResponseEntity<List<Sku>>  
	 * @Description 通过skuid集合查询sku
	 * @author 彭坤
	 * @date 2019年1月9日 下午9:43:20
	 */
	@GetMapping("/sku/list/ids")
	public ResponseEntity<List<Sku>> querySkuBySkuIds(@RequestParam("ids") List<Long> ids){
		return ResponseEntity.ok(goodsService.querySkuBySkuIds(ids));
	}
	
	/** 
	 * @param @param cartDTOs
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 减库存
	 * @author 彭坤
	 * @date 2019年1月13日 下午9:22:01
	 */
	@PostMapping("stock/decrease")
	public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDTO> cartDTOs){
		goodsService.decreaseStock(cartDTOs);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	
	
	

}
