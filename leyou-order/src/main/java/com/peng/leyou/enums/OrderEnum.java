package com.peng.leyou.enums;


public enum OrderEnum {

	UNPAY(1,"未付款"),
	PAY(2,"已付款,未发货"),
	DELIVERED(3,"已发货,未确认"),
	SUCCED(4,"交易成功"),
	CLOSED(5,"交易失败,已关闭"),
	RATED(6,"已评价")
	;
	private int code;
	private String desc;
	private OrderEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public int value() {
		return code;
	}
	
}
