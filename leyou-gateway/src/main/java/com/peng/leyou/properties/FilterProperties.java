package com.peng.leyou.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**   
* 项目名称：leyou-gateway   
* 类名称：FilterProperties   
* 类描述：   允许访问的白名单
* 创建人：彭坤   
* 创建时间：2019年1月7日 下午9:52:57      
* @version     
*/
@Data
@ConfigurationProperties(prefix="leyou.filter")
public class FilterProperties {
	
	private List<String> allowPaths;
	

}
