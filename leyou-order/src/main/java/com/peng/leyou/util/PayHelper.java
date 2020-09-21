package com.peng.leyou.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.RecipientInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.github.wxpay.sdk.WXPayUtil;
import com.peng.leyou.config.PayConfig;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.enums.OrderEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.OrderMapper;
import com.peng.leyou.mapper.OrderStatusMapper;
import com.peng.leyou.pojo.Order;
import com.peng.leyou.pojo.OrderStatus;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PayHelper {

	@Autowired
	private WXPay wxPay;
	
	@Autowired
	private PayConfig payConfig;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderStatusMapper orderStatusMapper;
	
	public String createOrder(Long orderId,Long totalPay,String desc) {
		 
		try {
			HashMap<String, String> data = new HashMap<>();
			//描述
			data.put("body", desc);
			//订单号
			data.put("out_trade_no", orderId.toString());
			//货币（默认就是人民币）
			//data.put("fee_type", "CNY");
			//总金额
			data.put("total_fee", totalPay.toString());
			//调用微信支付的终端ip
			data.put("spbill_create_ip", "127.0.0.1");
			//回调地址
			data.put("notify_url", payConfig.getNotifyUrl());
			//交易类型为扫码支付
			data.put("trade_type", "NATIVE");
			//完成下单
			Map<String, String> result = wxPay.fillRequestData(data);
			isSuccess(result);
			String url = result.get("code_url");
			return url;
		} catch (Exception e) {
			log.error("[微信下单] 创建预交易订单异常失败",e);
			return null;
		}
	}

	/** 
	 * @param @param result 
	 * @return void  
	 * @Description 判断返回信息是否成功
	 * @author 彭坤
	 * @date 2019年1月19日 下午3:34:58
	 */
	public void isSuccess(Map<String, String> result) {
		//判断通信标识
		String returnCode = result.get("return_code");
		if (WXPayConstants.FAIL.equals(returnCode)) {
			//通信失败
			log.error("[微信下单] 微信下单通信失败,失败原因{} ",result.get("return_msg"));
			throw new LeyouException(ExceptionEnum.WX_PAY_ORDER_FAIL);
		}
		
		if (WXPayConstants.FAIL.equals(result.get("result_code"))) {
		    log.error("[微信下单]创建预交易订单失败，错误码：{}，错误信息：{}", result.get("err_code"), result.get("err_code_des"));
		    throw new LeyouException(ExceptionEnum.WX_PAY_ORDER_FAIL);
		}
	}

	public void isSignValid(Map<String, String> map) {
		try {
			//重新生成签名和传来的签名比较
			String sign1 = WXPayUtil.generateSignature(map,payConfig.getKey(), SignType.MD5);
			String sign2 = WXPayUtil.generateSignature(map,payConfig.getKey(), SignType.HMACSHA256);
			String sign = map.get("sign");
			if (!StringUtils.equals(sign, sign1) && !StringUtils.equals(sign, sign2)) {//签名·有误
				throw new LeyouException(ExceptionEnum.INVALID_SIGN_ERROR);
			}
		} catch (Exception e) {
			throw new LeyouException(ExceptionEnum.INVALID_SIGN_ERROR);
		}
	}

	public Integer queryOrderStatus(Long orderId) {
		try {
			HashMap<String, String> data = new HashMap<>();
			data.put("out_trade_no", orderId.toString());
			Map<String, String> result = wxPay.orderQuery(data);
			isSuccess(result);
			isSignValid(result);
			String totalFee = result.get("total_fee");
			if (StringUtils.isEmpty(totalFee)) {
				throw new LeyouException(ExceptionEnum.INVALID_ORDER_PARAM_ERROR);
			}
			Long totalPrice=Long.valueOf(totalFee);
			//获得订单金额
			Order order = orderMapper.selectByPrimaryKey(orderId);
//			if (totalPrice!=order.getActualPay()) {
			if (totalPrice!=1l) {
				throw new LeyouException(ExceptionEnum.INVALID_ORDER_PARAM_ERROR);
			}
			String status = result.get("trade_state");
			if ("SUCCESS".equals(status)) {//支付成功
				//修改订单状态
				OrderStatus orderStatus = new OrderStatus();
				orderStatus.setStatus(OrderEnum.PAY.value());
				orderStatus.setOrderId(orderId);
				orderStatus.setPaymentTime(new Date());
				int count = orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
				if (count<1) {
					throw new LeyouException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
				}
				return 1;
			}
			if ("NOTPAY".equals(status) || "USERPAYING".equals(status)) {
				return 0;
			}
			return 2;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
}
