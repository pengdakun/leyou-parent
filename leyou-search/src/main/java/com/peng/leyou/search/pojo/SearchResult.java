package com.peng.leyou.search.pojo;

import java.util.List;
import java.util.Map;

import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.Category;
import com.peng.leyou.pojo.PageResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResult extends PageResult<Goods>{

    private List<Category> categories;//分类待选项

    private List<Brand> brands;
    
    private List<Map<String,Object>> specs; // 规格参数过滤条件

	public SearchResult(Long total, Integer totalPage, List<Goods> items,List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
		super(total, totalPage, items);
		this.categories = categories;
		this.brands = brands;
		this.specs = specs;
	}
    
    
    
    

}