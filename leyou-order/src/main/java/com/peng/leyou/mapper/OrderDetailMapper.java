package com.peng.leyou.mapper;

import com.peng.leyou.pojo.OrderDetail;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderDetailMapper extends Mapper<OrderDetail>,InsertListMapper<OrderDetail> {

}
