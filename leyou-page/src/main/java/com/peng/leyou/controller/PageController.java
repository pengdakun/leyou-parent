package com.peng.leyou.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.peng.leyou.service.PageService;

/**   
* 项目名称：leyou-page   
* 类名称：PageController   
* 类描述：   处理详情页需要的数据
* 创建人：彭坤   
* 创建时间：2018年12月27日 下午9:31:37      
* @version     
*/
@Controller
public class PageController {
	
	@Autowired
	private PageService pageService;
	
	
	@GetMapping("/item/{id}.html")
	public String page(@PathVariable("id") Long spuId,Model model) {
		//准备模型数据
		Map<String,Object> map =pageService.loadData(spuId);
		System.out.println(map+"!!!!!!!!!!!!!!!!");
		model.addAllAttributes(map);
		return "item";
	}
	

}
