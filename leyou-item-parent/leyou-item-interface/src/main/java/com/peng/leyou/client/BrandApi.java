package com.peng.leyou.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.peng.leyou.pojo.Brand;

/**   
* 项目名称：leyou-search   
* 类名称：BrandClient   
* 类描述：   品牌的客户端  feign
* 创建人：彭坤   
* 创建时间：2018年12月20日 下午10:33:46      
* @version     
*/
public interface BrandApi {
	
	@GetMapping("/brand/{id}")
	Brand queryBrandById(@PathVariable("id") Long id);
	
	@GetMapping("/brand/brands")
	List<Brand> queryBrandByIds(@RequestParam("ids") List<Long> ids);

}
