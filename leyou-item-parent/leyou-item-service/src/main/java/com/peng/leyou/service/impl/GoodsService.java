package com.peng.leyou.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.peng.leyou.dto.CartDTO;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.SkuMapper;
import com.peng.leyou.mapper.SpuDetailMapper;
import com.peng.leyou.mapper.SpuMapper;
import com.peng.leyou.mapper.StockMapper;
import com.peng.leyou.pojo.Brand;
import com.peng.leyou.pojo.Category;
import com.peng.leyou.pojo.PageResult;
import com.peng.leyou.pojo.Sku;
import com.peng.leyou.pojo.Spu;
import com.peng.leyou.pojo.SpuDetail;
import com.peng.leyou.pojo.Stock;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**   
* 项目名称：leyou-item-service   
* 类名称：GoodsService   
* 类描述：   商品相关service
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午3:29:05      
* @version     
*/
@Service
public class GoodsService {
	
	@Autowired
	private SpuMapper spuMapper;
	
	@Autowired
	private SpuDetailMapper spuDetailMapper;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private SkuMapper skuMapper;
	
	@Autowired
	private StockMapper stockMapper;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	

	public PageResult<Spu> findSpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
		
		PageHelper.startPage(page, rows);
		
		System.out.println(page+"-------------"+rows+"-----------------"+saleable+"----------------------");
		
		Example example=new Example(Spu.class);
		Criteria createCriteria = example.createCriteria();
		if(StringUtils.isNotEmpty(key)) {
			createCriteria.andLike("title", "%"+key+"%");
		}
		if(saleable!=null) {//上下架
			createCriteria.andEqualTo("saleable", saleable);
		}
		//设置默认排序
		example.setOrderByClause("last_update_time DESC");
		
		List<Spu> selectByExample = spuMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(selectByExample)) {
			throw new LeyouException(ExceptionEnum.QUERY_SPU_NULL);
		}
		
		loadCategoryAndBrand(selectByExample);
		
		PageInfo<Spu> pageInfo = new PageInfo<>(selectByExample);
		return new PageResult<Spu>(pageInfo.getTotal(), selectByExample);
	}
	
	/** 
	 * @param @param spus 
	 * @return void  
	 * @Description 循环查询分类名称及品牌名称
	 * @author 彭坤
	 * @date 2018年12月16日 下午4:06:51
	 */
	private void loadCategoryAndBrand(List<Spu> spus) {
		
		for (Spu spu : spus) {
			//处理品牌名称
			List<String> names = categoryService.findByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3())).stream().map(Category::getName).collect(Collectors.toList());
			spu.setCname(StringUtils.join(names, "/"));
			//品牌名称
			Long brandId = spu.getBrandId();
			Brand findById = brandService.findById(brandId);
			spu.setBname(findById.getName());
		}
	}

	@Transactional
	public void addGoods(Spu spu) {
		//新增spu
		spu.setId(null);
		spu.setCreateTime(new Date());
		spu.setLastUpdateTime(spu.getCreateTime());
		spu.setSaleable(true);
		spu.setValid(false);
		int insert = spuMapper.insert(spu);
		if (insert!=1) {
			throw new LeyouException(ExceptionEnum.SAVE_SPU_ERROR);
		}
		//新增spuDetail
		SpuDetail spuDetail = spu.getSpuDetail();
		spuDetail.setSpuId(spu.getId());
		spuDetailMapper.insert(spuDetail);
		
		List<Stock> stocks =new ArrayList<Stock>();
		
		//新增sku
		List<Sku> skus = spu.getSkus();
		for (Sku sku : skus) {
			sku.setCreateTime(new Date());
			sku.setLastUpdateTime(sku.getCreateTime());
			sku.setSpuId(spu.getId());
			int count = skuMapper.insert(sku);
			if (count!=1) {
				throw new LeyouException(ExceptionEnum.SAVE_SPU_ERROR);
			}
			//新增stock 库存
			Stock stock = new Stock();
			stock.setSkuId(sku.getId());
			stock.setStock(sku.getStock());
			stocks.add(stock);
		}
		int insertList = stockMapper.insertList(stocks);
		if (insertList!=stocks.size()) {
			throw new LeyouException(ExceptionEnum.SAVE_STOCK_ERROR);
		}
		
		//发送mq消息
		amqpTemplate.convertAndSend("item.insert", spu.getId());
		
	}

	public SpuDetail queryDetailBySpuId(Long spuId) {
		SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
		if (spuDetail==null) {
			throw new LeyouException(ExceptionEnum.QUERY_DETAIL_ERROR);
		}
		return spuDetail;
	}

	public List<Sku> querySkuBySpuId(Long id) {
		Sku sku = new Sku();
		sku.setSpuId(id);
		List<Sku> select = skuMapper.select(sku);
		if (CollectionUtils.isEmpty(select)) {
			throw new LeyouException(ExceptionEnum.QUERY_SKU_ERROR);
		}
		loadStockInSku(select);
		return select;
	}

	public Spu querySpuById(Long id) {
		Spu spu = spuMapper.selectByPrimaryKey(id);
		if (spu==null) {
			throw new LeyouException(ExceptionEnum.QUERY_SPU_NULL);
		}
		//查询sku
		List<Sku> querySkuBySpuId = querySkuBySpuId(id);
		spu.setSkus(querySkuBySpuId);
		//查询详情
		SpuDetail queryDetailBySpuId = queryDetailBySpuId(id);
		spu.setSpuDetail(queryDetailBySpuId);;
		return spu;
	}

	public List<Sku> querySkuBySkuIds(List<Long> ids) {
		List<Sku> skus = skuMapper.selectByIdList(ids);
		if (CollectionUtils.isEmpty(skus)) {
			throw new LeyouException(ExceptionEnum.QUERY_SKU_ERROR);
		}
		loadStockInSku(skus);
		return skus;
	}

	
	private void loadStockInSku(List<Sku> skus) {
		//查询库存
		List<Long> collect = skus.stream().map(Sku::getId).collect(Collectors.toList());
		List<Stock> selectByIdList = stockMapper.selectByIdList(collect);
		if (CollectionUtils.isEmpty(selectByIdList)) {
			System.out.println(collect+"!!!!!!!!!!!!!!!!!!!!!!!!");
			throw new LeyouException(ExceptionEnum.QUERY_STOCK_ERROR);
		}
		Map<Long, Integer> collect2 = selectByIdList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
		skus.forEach(s->s.setStock(collect2.get(s.getId())));
	}

	@Transactional
	public void decreaseStock(List<CartDTO> cartDTOs) {
		for (CartDTO cartDTO : cartDTOs) {
			//减库存
			int count = stockMapper.decreaseStock(cartDTO.getSkuId(),cartDTO.getNum());
			if (count!=1) {
				throw new LeyouException(ExceptionEnum.STOCK_NIT_ENOUTH);
			}
		}
	}

	

}
