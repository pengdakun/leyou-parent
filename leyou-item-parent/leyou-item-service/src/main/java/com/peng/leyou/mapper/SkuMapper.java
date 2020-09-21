package com.peng.leyou.mapper;

import com.peng.leyou.pojo.Sku;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**   
* 项目名称：leyou-item-service   
* 类名称：SkuMapper   
* 类描述：   sku mapper
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午10:39:56      
* @version     
*/
public interface SkuMapper extends Mapper<Sku>,IdListMapper<Sku, Long> {

}
