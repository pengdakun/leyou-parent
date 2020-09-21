package com.peng.leyou.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.BrandMapper;
import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.PageResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**   
* 项目名称：leyou-item-service   
* 类名称：BrandService   
* 类描述：   品牌service
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午9:05:52      
* @version     
*/
@Service
public class BrandService {
	
	@Autowired
	private BrandMapper brandMapper;

	public PageResult<Brand> findByPage(String key, Integer page, Integer rows, String sortBy, boolean desc) {
		//分页
		PageHelper.startPage(page, rows);
		Example example = new Example(Brand.class);
		//过滤
		if(StringUtils.isNotBlank(key)) {
			Criteria createCriteria = example.createCriteria();
			createCriteria.orLike("name", "%"+key+"%").orEqualTo("letter", key.toUpperCase());
		}
		//排序
		if(StringUtils.isNotBlank(sortBy)) {
			String oredrBy=sortBy+(desc?" desc":" asc");
			example.setOrderByClause(oredrBy);
		}
		List<Brand> selectByExample = brandMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(selectByExample)) {
			throw new LeyouException(ExceptionEnum.QUERY_BEAND_NOT_FOUND);
		}
		PageInfo<Brand> pageInfo = new PageInfo<>(selectByExample);
		return new PageResult<Brand>(pageInfo.getTotal(),selectByExample);
	}

	/** 
	 * @param @param brand
	 * @param @param cids 
	 * @return void  
	 * @Description 添加品牌
	 * @author 彭坤
	 * @date 2018年12月9日 下午10:22:12
	 */
	@Transactional
	public void addBrand(Brand brand, List<Long> cids) {
		brand.setId(null);
		int insert = brandMapper.insert(brand);
		if(insert!=1) {
			throw new LeyouException(ExceptionEnum.INSERT_BEAND_ERROR);
		}
		for (Long cid : cids) {
			int insertCategoryBrand = brandMapper.insertCategoryBrand(cid, brand.getId());
			if (insertCategoryBrand!=1) {
				throw new LeyouException(ExceptionEnum.INSERT_BEAND_ERROR);
			}
		}
	}
	
	public Brand findById(Long id) {
		Brand selectByPrimaryKey = brandMapper.selectByPrimaryKey(id);
		if (selectByPrimaryKey==null) {
			throw new LeyouException(ExceptionEnum.QUERY_BEAND_NOT_FOUND);
		}
		return selectByPrimaryKey;
	}
	
	
	public List<Brand> findListByCategoryId(Long cid) {
		List<Brand> findListByCategoryId = brandMapper.findListByCategoryId(cid);
		if (CollectionUtils.isEmpty(findListByCategoryId)) {
			throw new LeyouException(ExceptionEnum.QUERY_BEAND_NOT_FOUND);
		}
		return findListByCategoryId;
	}

	public List<Brand> queryBrandByIds(List<Long> ids) {
		List<Brand> brands = brandMapper.selectByIdList(ids);
		if (CollectionUtils.isEmpty(brands)) {
			throw new LeyouException(ExceptionEnum.QUERY_BEAND_NOT_FOUND);
		}
		return brands;
	}
	

}
