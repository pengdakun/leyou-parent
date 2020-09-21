package com.peng.leyou.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.peng.leyou.client.BrandClient;
import com.peng.leyou.client.CategoryClient;
import com.peng.leyou.client.GoodsClient;
import com.peng.leyou.client.SpecParamClient;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.Category;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.pojo.Sku;
import com.peng.leyou.pojo.SpecParam;
import com.peng.leyou.pojo.Spu;
import com.peng.leyou.pojo.SpuDetail;
import com.peng.leyou.repository.GoodsRepository;
import com.peng.leyou.search.pojo.Goods;
import com.peng.leyou.search.pojo.SearchRequest;
import com.peng.leyou.search.pojo.SearchResult;
import com.peng.leyou.utils.JsonUtils;

@Service
public class SearchService {
	
	@Autowired
	private CategoryClient categoryClient;
	
	@Autowired
	private BrandClient brandClient;
	
	@Autowired
	private GoodsClient goodsClient;
	
	@Autowired
	private SpecParamClient specParamClient;
	
	@Autowired
	private GoodsRepository goodsRepository;
	
	@Autowired
	private ElasticsearchTemplate template;
	
	
	
	/** 
	 * @param @param spu
	 * @param @return 
	 * @return Goods  
	 * @Description 将spu对象封装为goods添加到索引库
	 * @author 彭坤
	 * @date 2018年12月22日 下午1:14:14
	 */
	public Goods buildGoods(Spu spu) {
		Goods goods = new Goods();
		
		goods.setBrandId(spu.getBrandId());
		goods.setCid1(spu.getCid1());
		goods.setCid2(spu.getCid2());
		goods.setCid3(spu.getCid3());
		goods.setCreateTime(spu.getCreateTime());
		goods.setId(spu.getId());
		goods.setSubTitle(spu.getSubTitle());
		
		List<Category> categories = categoryClient.queryCategoryListByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
		if (CollectionUtils.isEmpty(categories)) {
			throw new LeyouException(ExceptionEnum.QUERY_CATEGORY_NOT_FOUND);
		}
		List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
		Brand brand = brandClient.queryBrandById(spu.getBrandId());
		if (brand==null) {
			throw new LeyouException(ExceptionEnum.QUERY_BEAND_NOT_FOUND);
		}
		String all=spu.getTitle()+StringUtils.join(names," ")+brand.getName();
		goods.setAll(all);//搜索字段   包含标题,分类,品牌，规格
		
		
		List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
		if (CollectionUtils.isEmpty(skus)) {
			throw new LeyouException(ExceptionEnum.QUERY_SKU_ERROR);
		}
		//处理skus
		List<Map<String, Object>> listMap=new ArrayList<Map<String,Object>>();
		Set<Long> set = new TreeSet<Long>();
		for (Sku sku: skus) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("price", sku.getPrice());
			map.put("title", sku.getTitle());
			map.put("image", StringUtils.substringBefore(sku.getImages(),","));
			map.put("id", sku.getId());
			listMap.add(map);
			set.add(sku.getPrice());
		}
		goods.setPrice(set);//价格  sk
		goods.setSkus(JsonUtils.serialize(listMap));//所有sku的集合的json
		
		Map<String,Object> specs = new HashMap<String,Object>();
		//查询规格参数
		List<SpecParam> params = specParamClient.findSpecParamList(null, spu.getCid3(), true);
		if (CollectionUtils.isEmpty(params)) {
			throw new LeyouException(ExceptionEnum.QUERY_SPEC_PARAM_NULL);
		}
		//查询商品描述
		SpuDetail spuDetail = goodsClient.queryDetailBySpuId(spu.getId());
		if (spuDetail==null) {
			throw new LeyouException(ExceptionEnum.QUERY_DETAIL_ERROR);
		}
		//获得通用规格参数
		Map<String, String> geneSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
		//获得特有规格参数
		Map<String, List<String>> specSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {});
		for (SpecParam specParam : params) {
			String key = specParam.getName();
			Object value=null;
			Boolean generic = specParam.getGeneric();
			if (generic) {//通用属性
				value=geneSpec.get(specParam.getId().toString());
				//判断是否是数字类型
				if (specParam.getNumeric()) {//如果是数字类型，处理成端       0.1-0.7   2.0-2.8
					value = chooseSegment(value.toString(), specParam);
				}
			}else {//特殊属性
				value=specSpec.get(specParam.getId().toString());
			}
			specs.put(key, value);
		}
		goods.setSpecs(specs);//所有的可搜索的规格参数
		
		return goods;
	}
	
	
	private String chooseSegment(String value, SpecParam p) {
	    double val = NumberUtils.toDouble(value);
	    String result = "其它";
	    // 保存数值段
	    for (String segment : p.getSegments().split(",")) {
	        String[] segs = segment.split("-");
	        // 获取数值范围
	        double begin = NumberUtils.toDouble(segs[0]);
	        double end = Double.MAX_VALUE;
	        if(segs.length == 2){
	            end = NumberUtils.toDouble(segs[1]);
	        }
	        // 判断是否在范围内
	        if(val >= begin && val < end){
	            if(segs.length == 1){
	                result = segs[0] + p.getUnit() + "以上";
	            }else if(begin == 0){
	                result = segs[1] + p.getUnit() + "以下";
	            }else{
	                result = segment + p.getUnit();
	            }
	            break;
	        }
	    }
	    return result;
	}


	public SearchResult search(SearchRequest searchRequest) {
		int page = searchRequest.getPage()-1;//当前页  es默认分页从0开始
		int size = searchRequest.getSize();//每页大小
		
		
		//创建查询构建器
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		
		//结果过滤
		queryBuilder.withSourceFilter(new FetchSourceFilter(new String[] {"id","subTitle","skus"}, null));
		
		//分页
		queryBuilder.withPageable(PageRequest.of(page, size));
		
		//过滤
		QueryBuilder matchQuery = buildBasicQuery(searchRequest);
		queryBuilder.withQuery(matchQuery);
		
		
		//聚合分类及品牌信息
		String categoryAggName="categoryAggName";
		String brandAggName="brandAggName";
		queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
		queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
		
		AggregatedPage<Goods> search = template.queryForPage(queryBuilder.build(), Goods.class);
		
		//获得分页结果
		List<Goods> content = search.getContent();
		long totalElements = search.getTotalElements();
		int totalPages = search.getTotalPages();
		
		//解析聚合结果
		Aggregations aggregations = search.getAggregations();
		List<Category> categorys=parseCategory(aggregations.get(categoryAggName));
		List<Brand> brands=parseBrand(aggregations.get(brandAggName));
		
		List<Map<String,Object>> specs = null;
		
		if (categorys!=null && categorys.size()==1) {//搜索结果自所为一个分类时才聚合规格参数
			//在原来搜索的基础上进行聚合
			specs=buildSpecifitionAgg(categorys.get(0).getId(),matchQuery);
		}
		
		SearchResult searchResult = new SearchResult(totalElements, totalPages, content, categorys, brands,specs);
		
		return searchResult;
	}




	public List<Brand> parseBrand(LongTerms aggregation) {
		try {
			//获得品牌的ids
			List<Bucket> buckets = aggregation.getBuckets();
			List<Long> ids = buckets.stream().map(b->b.getKeyAsNumber().longValue()).collect(Collectors.toList());
			List<Brand> brands = brandClient.queryBrandByIds(ids);
			return brands;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public List<Category> parseCategory(LongTerms aggregation) {
		try {
			//获得分类的ids
			List<Bucket> buckets = aggregation.getBuckets();
			List<Long> ids = buckets.stream().map(b->b.getKeyAsNumber().longValue()).collect(Collectors.toList());
			List<Category> categories = categoryClient.queryCategoryListByIds(ids);
			return categories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	private List<Map<String, Object>> buildSpecifitionAgg(Long cid, QueryBuilder matchQuery) {
		List<Map<String,Object>> specs = new ArrayList<Map<String, Object>>();
		
		//查询需要聚合的规格参数
		List<SpecParam> specParams= specParamClient.findSpecParamList(null, cid, true);
		//完成聚合
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		queryBuilder.withQuery(matchQuery);//在之前的基础上聚合
		for (SpecParam specParam : specParams) {
			String name=specParam.getName();
			queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
		}
		//获得结果并解析
		AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
		Aggregations aggregations = result.getAggregations();
		for (SpecParam specParam : specParams) {
			String name=specParam.getName();
			StringTerms aggregation = aggregations.get(name);
			List<String> options = aggregation.getBuckets().stream().map(b->b.getKeyAsString()).collect(Collectors.toList());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("k", name);
			map.put("options", options);
			specs.add(map);
		}
		return specs;
	}
	
	/** 
	 * @param @param searchRequest
	 * @param @return 
	 * @return QueryBuilder  
	 * @Description 构建查询条件
	 * @author 彭坤
	 * @date 2018年12月24日 下午10:23:00
	 */
	private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
		
		System.out.println(searchRequest+"@@@@@@@@@@@@@@@@@@@@@@@@");
		
		//创建boolean查询
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		//查询条件
		queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
		//过滤条件
		Map<String, String> filter = searchRequest.getFilter();
		
		System.out.println(filter+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		for (String key : filter.keySet()) {
			String value = filter.get(key);
			
			System.out.println(key+"-------"+value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			if (!"cid3".equals(key) && !"brandId".equals(key)) {//key为分类或品牌
				key="specs."+key+".keyword";
				System.out.println(key+"-------"+value+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
			queryBuilder.filter(QueryBuilders.termQuery(key, value));
		}
		return queryBuilder;
	}


	/** 
	 * @param @param spuId 
	 * @return void  
	 * @Description 新增或修改索引
	 * @author 彭坤
	 * @date 2019年1月1日 下午10:00:09
	 */
	public void createOrUpdateIndex(Long spuId) {
		Spu spu = goodsClient.querySpuById(spuId);
		Goods goods = buildGoods(spu);
		goodsRepository.save(goods);
	}


	/** 
	 * @param @param spuId 
	 * @return void  
	 * @Description 删除索引
	 * @author 彭坤
	 * @date 2019年1月1日 下午10:05:29
	 */
	public void deleteIndex(Long spuId) {
		goodsRepository.deleteById(spuId);
	}

}
