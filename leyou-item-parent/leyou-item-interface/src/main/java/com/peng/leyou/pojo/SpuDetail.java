package com.peng.leyou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String genericSpec;// 通用规格参数数据
    private String specialSpec;// 特有规格参数及可选值信息  json格式
    private String packingList;// 包装清单
    private String afterService;// 售后服务
    // 省略getter和setter
}