package com.peng.leyou.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.UserMapper;
import com.peng.leyou.pojo.User;
import com.peng.leyou.util.CodecUtils;
import com.peng.leyou.utils.NumberUtils;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	private static final String KEY_PREFIX="user:verify:phone";
	

	public Boolean checkData(String data, Integer type) {
		User user = new User();
		switch (type) {
		case 1:
			user.setUsername(data);
			break;
		case 2:
			user.setPhone(data);
			break;
		default:
			throw new LeyouException(ExceptionEnum.USER_DATA_TYPE_ERROR);
		}
		int count = userMapper.selectCount(user);
		return count==0;
	}

	public void sendMsg(String phone) {
		Map<String,String> map = new HashMap<String,String>();
		String key=KEY_PREFIX+phone;
		String code=NumberUtils.generateCode(6);
		map.put("phone", phone);
		map.put("code", code);
		//保存验证码到redis，保存5分钟
		redisTemplate.opsForValue().set(key, code,5,TimeUnit.MINUTES);
		//通过rabbitmq发送消息
		rabbitTemplate.convertAndSend("leyou.sms.exchange", "sms.verify.code",map);
	}

	
	public void userService(User user, String code) {
		//校验验证码
		String string = redisTemplate.opsForValue().get(KEY_PREFIX+user.getPhone());
		if (!StringUtils.equals(string,code)) {
			throw new LeyouException(ExceptionEnum.INVALID_VERIFY_CODE);
		}
		String salt=CodecUtils.generateSalt();
		user.setSalt(salt);
		//对密码加密
		user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
		//保存数据
		user.setCreated(new Date());
		userMapper.insert(user);
	}

	public User queryUserByUserNameAndPassWord(String username, String password) {
		User record = new User();
		record.setUsername(username);
		User user = userMapper.selectOne(record);
		if (user==null) {
			throw new LeyouException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
		}
		
		password = CodecUtils.md5Hex(password, user.getSalt());
		if (!StringUtils.equals(password, user.getPassword())) {
			throw new LeyouException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
		}
		return user;
	}
	

}
