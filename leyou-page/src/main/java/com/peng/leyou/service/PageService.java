package com.peng.leyou.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.peng.leyou.client.BrandClient;
import com.peng.leyou.client.CategoryClient;
import com.peng.leyou.client.GoodsClient;
import com.peng.leyou.client.SpecParamClient;
import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.Category;
import com.peng.leyou.pojo.SpecGroup;
import com.peng.leyou.pojo.Spu;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PageService {
	
	@Autowired
	private GoodsClient goodsClient;
	
	@Autowired
	private CategoryClient categoryClient;
	
	@Autowired
	private BrandClient brandClient;
	
	@Autowired
	private SpecParamClient specParamClient;
	
	@Autowired
	private TemplateEngine templateEngine;//模板引擎

	/** 
	 * @param @param spuId
	 * @param @return 
	 * @return Map<String,Object>  
	 * @Description 加载商品详情页所需数据
	 * @author 彭坤
	 * @date 2018年12月27日 下午10:22:01
	 */
	public Map<String, Object> loadData(Long spuId) {
		Map<String,Object> map = new HashMap<String,Object>();
		Spu spu = goodsClient.querySpuById(spuId);
		List<Category> categories = categoryClient.queryCategoryListByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
		Brand brand = brandClient.queryBrandById(spu.getBrandId());
		List<SpecGroup> specs = specParamClient.querySpecGroupByCid(spu.getCid3());
		map.put("title", spu.getTitle());
		map.put("subTitle", spu.getSubTitle());
		map.put("skus", spu.getSkus());
		map.put("detail",spu.getSpuDetail());
		map.put("brand", brand);
		map.put("categories", categories);
		map.put("specs", specs);
		return map;
	}
	
	
	/** 
	 * @param @param spuId 
	 * @return void  
	 * @Description 生成商品详情页静态html
	 * @author 彭坤
	 * @throws IOException 
	 * @date 2018年12月31日 下午1:35:35
	 */
	public void createHtml(Long spuId){
		//准备上下文
		Context context = new Context();
		context.setVariables(loadData(spuId));
		//输出流
		File file = new File("D:\\"+spuId+".html");
		
		if (file.exists()) {//文件存在就删除
			file.delete();
		}
		
		try(PrintWriter printWriter=new PrintWriter(file,"UTF-8")) {
			//输出文件
			templateEngine.process("item", context,printWriter);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("静态页创建失败!!!!!!");
		}
	}


	/** 
	 * @param @param spuId 
	 * @return void  
	 * @Description 删除静态页
	 * @author 彭坤
	 * @date 2019年1月1日 下午10:12:26
	 */
	public void deleteHtml(Long spuId) {
		File file = new File("D:\\"+spuId+".html");
		if (file.exists()) {//文件存在就删除
			file.delete();
		}
	}
	

}
