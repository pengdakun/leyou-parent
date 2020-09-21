package com.peng.leyou.test;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Before;
import org.junit.Test;

import com.peng.leyou.entity.UserInfo;
import com.peng.leyou.utils.JwtUtils;
import com.peng.leyou.utils.RsaUtils;

public class JwtTest {

	private static final String pubKeyPath = "E:\\rsa.pub";

    private static final String priKeyPath = "E:\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
    	
    	System.out.println("!!!!!!!!!!!!!!!!!!");
    	
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU0Njc2MTA1MX0.EMO5jpIYeOoOUEkm5k85FNhlco6SmiFHOxN0U58joMvnJVgGIiLfYRWIF1yq75S2R6iJvfGajDzphZIKslSM7WN-Z3290F_WDP_HwZAkO8MkhgLpLI4aNjVcuVuazwHKJ8JcSz1m__t8bSGxUelhtPazyQwXOgQPBJBGltavdkw";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}