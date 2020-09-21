package com.peng.leyou.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.peng.leyou.search.pojo.Goods;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
