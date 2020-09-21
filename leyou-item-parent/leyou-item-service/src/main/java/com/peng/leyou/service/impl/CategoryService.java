package com.peng.leyou.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.CategoryMapper;
import com.peng.leyou.pojo.Category;

/**   
* 项目名称：leyou-item-service   
* 类名称：CategoryService   
* 类描述：  商品分类service 
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午1:58:32      
* @version     
*/
@Service
public class CategoryService {
	
	@Autowired
	private CategoryMapper categoryMapper;

	public List<Category> queryCategorylistByPid(Long parentId) {
		Category category = new Category();
		category.setParentId(parentId);
		List<Category> select = categoryMapper.select(category);
		if (CollectionUtils.isEmpty(select)) {
			throw new LeyouException(ExceptionEnum.QUERY_CATEGORY_NOT_FOUND);
		}
		return select;
	}
	
	
	public List<Category> findByIds(List<Long> ids){
		List<Category> selectByIdList = categoryMapper.selectByIdList(ids);
		if (CollectionUtils.isEmpty(selectByIdList)) {
			throw new LeyouException(ExceptionEnum.QUERY_CATEGORY_NOT_FOUND);
		}
		return selectByIdList;
	}

}
