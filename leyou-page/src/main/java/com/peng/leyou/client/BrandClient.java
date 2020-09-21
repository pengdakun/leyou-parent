package com.peng.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

/**   
* 项目名称：leyou-search   
* 类名称：BrandClient   
* 类描述：   品牌的客户端  feign
* 创建人：彭坤   
* 创建时间：2018年12月20日 下午10:33:46      
* @version     
*/
@FeignClient("leyou-item-service")
public interface BrandClient extends BrandApi{}
