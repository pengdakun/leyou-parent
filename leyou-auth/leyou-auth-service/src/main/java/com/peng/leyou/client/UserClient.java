package com.peng.leyou.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("leyou-user-service")
public interface UserClient extends UserApi {}
