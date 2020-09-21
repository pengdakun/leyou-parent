package com.peng.leyou.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.peng.leyou.client.CategoryApi;
import com.peng.leyou.client.GoodsClient;
import com.peng.leyou.pojo.Category;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.pojo.Spu;
import com.peng.leyou.repository.GoodsRepository;
import com.peng.leyou.search.pojo.Goods;
import com.peng.leyou.service.SearchService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LeyouSearchTest {
	
	@Autowired
	private CategoryApi categoryClient;
	
	@Autowired
	private GoodsRepository repository;
	
	@Autowired
	private ElasticsearchTemplate template;
	
	@Autowired
	private GoodsClient goodsClient;
	
	@Autowired
	private SearchService searchService; 
	
	@Test
	public void test1() {
		List<Category> queryCategoryListByIds = categoryClient.queryCategoryListByIds(Arrays.asList(1L,2L,3L));
		for (Category category : queryCategoryListByIds) {
			System.out.println(category);
		}
	}
	
	//创建索引库
	@Test
	public void test2() {
		template.createIndex(Goods.class);
		template.putMapping(Goods.class);
	}
	
	//批量导入数据到索引库
	@Test
	public void loadData() {
		int page=1;
		int rows=100;
		int size=0;
		
//		PageResult<Spu> pageResult = goodsClient.findSpuByPage("", true, page, rows);
//		System.out.println(pageResult);

		do {
			PageResult<Spu> pageResult = goodsClient.findSpuByPage(null, true, page, rows);
			List<Spu> items = pageResult.getItems();
			if(CollectionUtils.isEmpty(items)) {
				break;
			}
			List<Goods> goods = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
			repository.saveAll(goods);
			page++;
			size=items.size();
		} while (size>0);
		
		
		
		
	}
	

}
