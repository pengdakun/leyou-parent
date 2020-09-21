package com.peng.leyou.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.peng.leyou.pojo.Stock;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;



/**   
* 项目名称：leyou-item-service   
* 类名称：StockMapper   
* 类描述：   库存相关mapper
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午10:40:28      
* @version     
*/
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock>,IdListMapper<Stock, Long> {

	@Update("UPDATE tb_stock t SET t.stock=t.stock-#{num} WHERE t.sku_id=#{id} AND t.stock>=#{num}")
	int decreaseStock(@Param("id") Long id, @Param("num") Integer num);
	
}
