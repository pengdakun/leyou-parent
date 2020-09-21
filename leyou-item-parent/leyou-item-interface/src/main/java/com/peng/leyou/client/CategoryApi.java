package com.peng.leyou.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peng.leyou.pojo.Category;

/**   
* 项目名称：leyou-search   
* 类名称：CategoryClient   
* 类描述：   分类的客户端  通过feign
* 创建人：彭坤   
* 创建时间：2018年12月20日 下午10:08:54      
* @version     
*/
public interface CategoryApi {

	@GetMapping("/category/list/ids")
	List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);
	
	
}
