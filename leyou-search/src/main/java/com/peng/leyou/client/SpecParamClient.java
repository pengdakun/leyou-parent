package com.peng.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

/**   
* 项目名称：leyou-search   
* 类名称：SpecParamClient   
* 类描述：  规格客户顿 feign
* 创建人：彭坤   
* 创建时间：2018年12月20日 下午10:52:53      
* @version     
*/
@FeignClient("leyou-item-service")
public interface SpecParamClient extends SpecParamApi {

}
