package com.peng.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("leyou-item-service")
public interface GoodsClient extends GoodsApi{}
