package com.peng.leyou.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peng.leyou.pojo.SpecGroup;
import com.peng.leyou.pojo.SpecParam;
import com.peng.leyou.service.impl.SpecificationService;

/**   
* 项目名称：leyou-item-service   
* 类名称：SpecificationController   
* 类描述：   规格controller
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午1:31:20      
* @version     
*/
@RestController
@RequestMapping("/spec")
public class SpecificationController {
	
	@Autowired
	private SpecificationService specificationService;
	
	/** 
	 * @param @param cid
	 * @param @return 
	 * @return ResponseEntity<List<SpecGroup>>  
	 * @Description 通过分类id查询规格组信息
	 * @author 彭坤
	 * @date 2018年12月16日 下午1:35:36
	 */
	@GetMapping("/groups/{cid}")
	public ResponseEntity<List<SpecGroup>> findSpecGroupByCid(@PathVariable("cid") Long cid){
		return ResponseEntity.ok(specificationService.findSpecGroupByCid(cid));
	}
	
	/** 
	 * @param @param gid
	 * @param @return 
	 * @return ResponseEntity<List<SpecParam>>  
	 * @Description 通过规格组id查询规格参数信息
	 * @author 彭坤
	 * @date 2018年12月16日 下午2:06:53
	 */
	@GetMapping("/params")
	public ResponseEntity<List<SpecParam>> findSpecParamList(@RequestParam(value="gid",required=false) Long gid,@RequestParam(value="cid",required=false) Long cid,@RequestParam(value="searching",required=false) Boolean searching){
		return ResponseEntity.ok(specificationService.findSpecParamList(gid,cid,searching));
	}
	
	
	/** 
	 * @param @param cid
	 * @param @return 
	 * @return ResponseEntity<List<SpecGroup>>  
	 * @Description 通过cid查询规格参数组及组内参数
	 * @author 彭坤
	 * @date 2018年12月27日 下午9:57:35
	 */
	@GetMapping("/group/{cid}")
	ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable("cid") Long cid){
		return ResponseEntity.ok(specificationService.querySpecGroupByCid(cid));
	}
	
	

}
