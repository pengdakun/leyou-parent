package com.peng.leyou.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.peng.leyou.pojo.Brand;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand>,IdListMapper<Brand, Long> {
	
	@Insert("insert into tb_category_brand values(#{cid},#{bid})")
	int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);

	@Select("SELECT t.* FROM tb_brand t INNER JOIN tb_category_brand cb ON t.id=cb.brand_id WHERE cb.category_id=#{cid}")
	List<Brand> findListByCategoryId(@Param("cid") Long cid);
	
}
