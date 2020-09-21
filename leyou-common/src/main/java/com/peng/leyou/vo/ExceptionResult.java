package com.peng.leyou.vo;

import com.peng.leyou.enums.ExceptionEnum;

import lombok.Data;

@Data
public class ExceptionResult {
	
	private int status;
	private String msg;
	private long timeScope;
	
	
	public ExceptionResult(ExceptionEnum em) {
		this.status=em.getCode();
		this.msg=em.getMsg();
		this.timeScope=System.currentTimeMillis();
	}
	
	

}
