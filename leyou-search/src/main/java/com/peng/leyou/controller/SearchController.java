package com.peng.leyou.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.search.pojo.Goods;
import com.peng.leyou.search.pojo.SearchRequest;
import com.peng.leyou.service.SearchService;

/**   
* 项目名称：leyou-search   
* 类名称：SearchController   
* 类描述：   搜索controller
* 创建人：彭坤   
* 创建时间：2018年12月23日 下午2:26:05      
* @version     
*/
@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	/** 
	 * @param @param searchRequest
	 * @param @return 
	 * @return ResponseEntity<PageResult<Goods>>  
	 * @Description 搜索
	 * @author 彭坤
	 * @date 2018年12月23日 下午2:28:25
	 */
	@PostMapping("/page")
	public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
		return ResponseEntity.ok(searchService.search(searchRequest));
	}

}
