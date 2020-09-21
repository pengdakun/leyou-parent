package com.peng.leyou.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peng.leyou.dto.CartDTO;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.pojo.Sku;
import com.peng.leyou.pojo.Spu;
import com.peng.leyou.pojo.SpuDetail;

@RequestMapping("/spu")
public interface GoodsApi {
	
	@GetMapping("/page")
	PageResult<Spu> findSpuByPage(@RequestParam(value="key",required = false) String key,@RequestParam("saleable") Boolean saleable,
			@RequestParam("page") Integer page,@RequestParam("rows") Integer rows);
	
	@GetMapping("/spu/detail/{id}")
	SpuDetail queryDetailBySpuId(@PathVariable("id") Long spuId);
	
	@GetMapping("/list/ids")
	List<Sku> querySkuBySpuId(@RequestParam("id") Long id);
	
	
	@GetMapping("/{id}")
	Spu querySpuById(@PathVariable("id") Long id);
	
	
	@GetMapping("/sku/list/ids")
	List<Sku> querySkuBySkuIds(@RequestParam("ids") List<Long> ids);
	
	
	@PostMapping("stock/decrease")
	void decreaseStock(@RequestBody List<CartDTO> cartDTOs);
	

}
