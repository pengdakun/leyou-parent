package com.peng.leyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.pojo.Category;
import com.peng.leyou.service.impl.CategoryService;

/**   
* 项目名称：leyou-item-service   
* 类名称：CategoryController   
* 类描述：   商品分类controller
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午2:02:31      
* @version     
*/
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	/** 
	 * @param @param parentId
	 * @param @return 
	 * @return List<Category>  
	 * @Description 通过父id查询分类
	 * @author 彭坤
	 * @date 2018年12月9日 下午2:02:46
	 */
	@GetMapping("/list")
	public ResponseEntity<List<Category>> queryCategorylistByPid(@RequestParam("pid") Long parentId){
		return ResponseEntity.ok(categoryService.queryCategorylistByPid(parentId));
	}
	
	
	/** 
	 * @param @param ids
	 * @param @return 
	 * @return ResponseEntity<List<Category>>  
	 * @Description 通过分类id查询分类
	 * @author 彭坤
	 * @date 2018年12月20日 下午9:57:54
	 */
	@GetMapping("/list/ids")
	public ResponseEntity<List<Category>> queryCategoryListByIds(@RequestParam("ids") List<Long> ids){
		return ResponseEntity.ok(categoryService.findByIds(ids));
	}

}
