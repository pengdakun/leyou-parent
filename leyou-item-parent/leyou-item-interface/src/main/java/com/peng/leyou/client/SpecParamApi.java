package com.peng.leyou.client;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peng.leyou.pojo.SpecGroup;
import com.peng.leyou.pojo.SpecParam;

@RequestMapping("/spec")
public interface SpecParamApi {
	
	@GetMapping("/params")
	List<SpecParam> findSpecParamList(@RequestParam(value="gid",required=false) Long gid,@RequestParam(value="cid",required=false) Long cid,@RequestParam(value="searching",required=false) Boolean searching);
	
	
	@GetMapping("/group/{cid}")
	List<SpecGroup> querySpecGroupByCid(@PathVariable("cid") Long cid);

}
