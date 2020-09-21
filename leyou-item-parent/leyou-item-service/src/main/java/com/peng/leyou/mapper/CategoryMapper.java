package com.peng.leyou.mapper;

import com.peng.leyou.pojo.Category;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**   
* 项目名称：leyou-item-service   
* 类名称：CategoryMapper   
* 类描述：   商品分类mapper
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午1:58:16      
* @version     
*/
public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category, Long>{

}
