package com.peng.leyou.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: HuYi.Zhang
 * @create: 2018-04-24 17:20
 **/
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }
    
    static class User{
    	String name;
    	int age;
    	
		public User() {
			super();
		}
		public User(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			return "User [name=" + name + ", age=" + age + "]";
		}
    }
    
    public static void main(String[] args) {
    	User user = new User();
    	user.setName("小红");
    	user.setAge(22);
		String json=serialize(user);
		System.out.println(json);
		User user1 = parse(json, User.class);
		System.out.println(user1);
		
		json="[20,10,5,15]";
		List<Integer> parseList = parseList(json, Integer.class);
		System.out.println(parseList);
		
		json="{\"name\":\"小红\",\"age\":22}";
		Map<String, String> parseMap = parseMap(json, String.class, String.class);
		System.out.println(parseMap.get("name"));
		
		json="[{\"name\":\"小花\",\"age\":23},{\"name\":\"小名\",\"age\":56}]";
		List<Map<String, String>> nativeRead = nativeRead(json, new TypeReference<List<Map<String,String>>>() {});
		System.out.println(nativeRead);
		
	}
    
}
