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
	
	private String secret;
	
	private String pubKeyPath;
	
	private String priKeyPath;
	
	private Integer expire;
	
	private String cookieName;
	
	private PublicKey publicKey;//公钥
	
	private PrivateKey privateKey;//私钥
	
	//对象一旦实例化就读取公钥及私钥
	@PostConstruct
	public void init() throws Exception {
		File pubFile = new File(pubKeyPath);
		File priFile = new File(priKeyPath);
		if (!pubFile.exists() || !priFile.exists()) {//公钥私钥不存在就生成
			RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
		}
		//读取公钥私钥
		this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
		this.privateKey=RsaUtils.getPrivateKey(priKeyPath);
		
	}

}
