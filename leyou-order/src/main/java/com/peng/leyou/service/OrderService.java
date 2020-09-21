package com.peng.leyou.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peng.leyou.client.AddressClient;
import com.peng.leyou.client.GoodsClient;
import com.peng.leyou.dto.AddressDTO;
import com.peng.leyou.dto.CartDTO;
import com.peng.leyou.dto.OrderDTO;
import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.enums.OrderEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.interceptor.LoginInterceptor;
import com.peng.leyou.mapper.OrderDetailMapper;
import com.peng.leyou.mapper.OrderMapper;
import com.peng.leyou.mapper.OrderStatusMapper;
import com.peng.leyou.pojo.Order;
import com.peng.leyou.pojo.OrderDetail;
import com.peng.leyou.pojo.OrderStatus;
import com.peng.leyou.pojo.Sku;
import com.peng.leyou.util.PayHelper;
import com.peng.leyou.utils.IdWorker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderDetailMapper orderDetailMapper;
	
	@Autowired
	private OrderStatusMapper orderStatusMapper;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private GoodsClient goodsClient;
	
	@Autowired
	private PayHelper payHelper;
	
	@Transactional
	public Long createOrder(OrderDTO orderDTO) {
		//新增订单
		Order order = new Order();
		long orderId = idWorker.nextId();
		UserInfo userInfo = LoginInterceptor.getUserInfo();
		order.setOrderId(orderId);
		order.setCreateTime(new Date());
		order.setUserId(userInfo.getId());
		order.setPaymentType(orderDTO.getPaymentType());
		order.setBuyerNick(userInfo.getUsername());
		order.setBuyerRate(false);
		//设置收货人信息
		AddressDTO addressDTO = AddressClient.findById(orderDTO.getAddressId());
		order.setReceiver(addressDTO.getName());
		order.setReceiverAddress(addressDTO.getAddress());
		order.setReceiverCity(addressDTO.getCity());
		order.setReceiverDistrict(addressDTO.getDistrict());
		order.setReceiverMobile(addressDTO.getPhone());
		order.setReceiverState(addressDTO.getState());
		order.setReceiverZip(addressDTO.getZipCode());
		//设置金额
		Map<Long, Integer> map = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
		Set<Long> skuIdSet = map.keySet();
		List<Sku> skus = goodsClient.querySkuBySkuIds(new ArrayList<>(skuIdSet));
		Long totalPay=0l;
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for (Sku sku : skus) {
			totalPay+= sku.getPrice()*map.get(sku.getId());
			//设置订单详情
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
			orderDetail.setNum(map.get(sku.getId()));
			orderDetail.setOrderId(orderId);
			orderDetail.setOwnSpec(sku.getOwnSpec());
			orderDetail.setPrice(sku.getPrice());
			orderDetail.setSkuId(sku.getId());
			orderDetail.setTitle(sku.getTitle());
			orderDetails.add(orderDetail);
		}
		order.setTotalPay(totalPay);
		order.setActualPay(totalPay+order.getPostFee()-0);
		int count = orderMapper.insertSelective(order);
		if (count!=1) {
			log.error("[创建订单] 创建订单失败",orderId);
			throw new LeyouException(ExceptionEnum.CARTE_ORDER_ERROR);
		}
		//新增订单详情
		count = orderDetailMapper.insertList(orderDetails);
		if (count<=0) {
			log.error("[创建订单] 创建订单失败",orderId);
			throw new LeyouException(ExceptionEnum.CARTE_ORDER_ERROR);
		}
		//新增订单状态
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setCreateTime(new Date());
		orderStatus.setOrderId(orderId);
		orderStatus.setStatus(OrderEnum.UNPAY.value());
		count = orderStatusMapper.insertSelective(orderStatus);
		if (count!=1) {
			log.error("[创建订单] 创建订单失败",orderId);
			throw new LeyouException(ExceptionEnum.CARTE_ORDER_ERROR);
		}
		//减库存
		List<CartDTO> cartDTOs = orderDTO.getCarts();
		goodsClient.decreaseStock(cartDTOs);
		
		return orderId;
	}

	public Order findOrderById(Long oid) {
		Order order = orderMapper.selectByPrimaryKey(oid);
		if (order==null) {
			throw new LeyouException(ExceptionEnum.ORDER_NOT_FOUND);
		}
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(order.getOrderId());
		List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
		if (CollectionUtils.isEmpty(orderDetails)) {
			throw new LeyouException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
		}
		order.setOrderDetails(orderDetails);
		OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(order.getOrderId());
		if (orderStatus==null) {
			throw new LeyouException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
		}
		order.setOrderStatus(orderStatus);
		return order;
	}

	public String createPayUrl(Long id) {
		Order order = findOrderById(id);
		if (order.getOrderStatus().getStatus()!=OrderEnum.UNPAY.value()) {
			throw new LeyouException(ExceptionEnum.ORDER_STATUS_ERROR);
		}
		String title = order.getOrderDetails().get(0).getTitle();
		String url = payHelper.createOrder(id, 1L, title);
		return url;
	}

	public void handleNotify(Map<String, String> map) {
		//数据校验
		payHelper.isSuccess(map);
		//校验签名
		payHelper.isSignValid(map);
		//比较金额是否正确
		String totalFee = map.get("total_fee");
		//获得订单编号
		String outTradeNo = map.get("out_trade_no");
		if (StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(outTradeNo)) {
			throw new LeyouException(ExceptionEnum.INVALID_ORDER_PARAM_ERROR);
		}
		Long totalPrice=Long.valueOf(totalFee);
		//获得订单金额
		Long orderID=Long.valueOf(outTradeNo);
		Order order = orderMapper.selectByPrimaryKey(orderID);
//		if (totalPrice!=order.getActualPay()) {
		if (totalPrice!=1l) {
			throw new LeyouException(ExceptionEnum.INVALID_ORDER_PARAM_ERROR);
		}
		//修改订单状态
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setStatus(OrderEnum.PAY.value());
		orderStatus.setOrderId(orderID);
		orderStatus.setPaymentTime(new Date());
		int count = orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
		if (count<1) {
			throw new LeyouException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
		}
		log.info("[订单修改状态] 订单支付成功，订单编号{}",orderID);
	}

	public Integer queryOrderStatus(Long orderId) {
		
		//查询订单状态
		OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
		Integer status = orderStatus.getStatus();
		if (status!=OrderEnum.UNPAY.value()) {//已付款
			return 1;
		}
		//通过微信查询订单状态
		return payHelper.queryOrderStatus(orderId);
	}

}
