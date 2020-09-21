package com.peng.leyou.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.mapper.BrandMapper;
import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.service.impl.BrandService;

/**   
* 项目名称：leyou-item-service   
* 类名称：BrandController   
* 类描述：   品牌controller
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午9:08:04      
* @version     
*/
@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	//key=&page=1&rows=25&sortBy=id&desc=false
	/** 
	 * @param @param key
	 * @param @param page
	 * @param @param rows
	 * @param @param sortBy
	 * @param @param desc
	 * @param @return 
	 * @return ResponseEntity<PageResult<Brand>>  
	 * @Description 查询分页
	 * @author 彭坤
	 * @date 2018年12月9日 下午9:21:19
	 */
	@GetMapping("/page")
	public ResponseEntity<PageResult<Brand>> findByPage(@RequestParam(value="key",required=false) String key,@RequestParam(value="page",defaultValue="1") Integer page,
			@RequestParam(value="rows",defaultValue="15") Integer rows,@RequestParam(value="sortBy",required=false) String sortBy,
			@RequestParam(value="desc",defaultValue="false") boolean desc){
		return ResponseEntity.ok(brandService.findByPage(key,page,rows,sortBy,desc));
	}
	
	
	/** 
	 * @param @param brand
	 * @param @param cids
	 * @param @return 
	 * @return ResponseEntity<Void>  
	 * @Description 添加品牌
	 * @author 彭坤
	 * @date 2018年12月9日 下午10:20:38
	 */
	@PostMapping
	public ResponseEntity<Void> addBrand(Brand brand,@RequestParam("cids") List<Long> cids) {
		brandService.addBrand(brand,cids);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/** 
	 * @param @param cid
	 * @param @return 
	 * @return ResponseEntity<List<Brand>>  
	 * @Description 通过分类id查询品牌
	 * @author 彭坤
	 * @date 2018年12月16日 下午8:52:02
	 */
	@GetMapping("/cid/{cid}")
	public ResponseEntity<List<Brand>> findListByCategoryId(@PathVariable("cid") Long cid){
		return ResponseEntity.ok(brandService.findListByCategoryId(cid));
	}
	
	/** 
	 * @param @param id
	 * @param @return 
	 * @return ResponseEntity<Brand>  
	 * @Description 通过品牌id查询
	 * @author 彭坤
	 * @date 2018年12月20日 下午10:01:21
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
		return ResponseEntity.ok(brandService.findById(id));
	}
	
	/** 
	 * @param @param ids
	 * @param @return 
	 * @return ResponseEntity<List<Brand>>  
	 * @Description 通过id集合查询
	 * @author 彭坤
	 * @date 2018年12月23日 下午9:50:16
	 */
	@GetMapping("/brands")
	public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
		return ResponseEntity.ok(brandService.queryBrandByIds(ids));
	};
	
	

}
