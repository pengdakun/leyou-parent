package com.peng.leyou.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter//提供get方法
@NoArgsConstructor//提供空参构造方法
@AllArgsConstructor//提供全参构造方法
public enum ExceptionEnum {
	
	QUERY_CATEGORY_NOT_FOUND(404,"商品类目不存在"),
	QUERY_BEAND_NOT_FOUND(404,"商品品牌不存在"),
	INSERT_BEAND_ERROR(500,"新增商品失败"),
	FILE_UPLOAD_ERROR(500,"文件上传失败"),
	INVALID_FILE_TYPE(400,"文件类型错误"),
	QUERY_SPEC_GROUP_NULL(404,"商品规格组没查到"),
	QUERY_SPEC_PARAM_NULL(404,"商品规格参数没查到"),
	QUERY_SPU_NULL(404,"SPU未查询到"),
	SAVE_SPU_ERROR(500,"SPU新增失败"),
	SAVE_SKU_ERROR(500,"SKU新增失败"),
	SAVE_STOCK_ERROR(500,"STOCK新增失败"),
	QUERY_DETAIL_ERROR(404,"查询SPU描述错误"),
	QUERY_SKU_ERROR(404,"查询SKU错误"), 
	QUERY_STOCK_ERROR(404,"库存不存在"),
	USER_DATA_TYPE_ERROR(400,"请求参数有误"),
	INVALID_VERIFY_CODE(400,"验证码无效"),
	USERNAME_OR_PASSWORD_ERROR(400,"用户名或密码错误"),
	CREATE_TAKEN_ERROR(500,"生成用户凭证失败"),
	UNAUTHORIZED(403,"用户未登录"),
	CART_REDIS_NOT_FOUND(404,"用户购物车不存在"),
	CARTE_ORDER_ERROR(500,"创建订单失败"),
	STOCK_NIT_ENOUTH(500,"库存不足"),
	ORDER_NOT_FOUND(404,"订单不存在"),
	ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在"),
	ORDER_STATUS_NOT_FOUND(404,"订单状态不存在"),
	WX_PAY_ORDER_FAIL(500,"微信下单失败"),
	ORDER_STATUS_ERROR(500,"订单状态异常"),
	INVALID_SIGN_ERROR(500,"签名有误"),
	INVALID_ORDER_PARAM_ERROR(500,"订单支付成功回调参数有误"),
	UPDATE_ORDER_STATUS_ERROR(500,"更新订单状态错误")
	
	;
	private int code;
	private String msg;
	
	
	

}
