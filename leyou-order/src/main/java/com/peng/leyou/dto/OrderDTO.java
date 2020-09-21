package com.peng.leyou.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	
	@NotNull
	private Long addressId;//接收地址id
	@NotNull
	private Integer paymentType;//支付方式
	@NotNull
	private List<CartDTO> carts;//商品集合
	

}
