package com.peng.leyou.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

/**   
* 项目名称：leyou-item-interface   
* 类名称：SpecParam   
* 类描述：   规格参数表
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午2:04:45      
* @version     
*/
@Table(name="tb_spec_param")
@Data
public class SpecParam {
	
	
	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	
	private Long cid;
	
	private Long groupId;
	
	private String name;
	
	@Column(name="`numeric`")
	private Boolean numeric;
	
	private String unit;
	
	private Boolean generic;
	
	private Boolean searching;
	
	private String segments;
	
	

}
