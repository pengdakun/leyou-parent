package com.peng.leyou.properties;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.peng.leyou.utils.RsaUtils;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="leyou.jwt")
public class JWTProperties {
	
	private String pubKeyPath;
	
	private PublicKey publicKey;//公钥
	
	private String cookieName;
	
	
	//对象一旦实例化就读取公钥及私钥
	@PostConstruct
	public void init() throws Exception {
		//读取公钥
		this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
	}

}
