package com.peng.leyou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

@Table(name = "tb_brand")
@Data
public class Brand {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter;//首字母
}