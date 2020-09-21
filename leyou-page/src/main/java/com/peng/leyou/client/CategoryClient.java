package com.peng.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

/**   
* 项目名称：leyou-search   
* 类名称：CategoryClient   
* 类描述：   分类的客户端  通过feign
* 创建人：彭坤   
* 创建时间：2018年12月20日 下午10:08:54      
* @version     
*/
@FeignClient("leyou-item-service")
public interface CategoryClient extends CategoryApi{}
