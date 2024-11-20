package com.example.backend;

import com.example.entity.RestBean;
import com.example.entity.Test2;
import com.example.entity.dto.Account;
import com.example.entity.dto.Lbt;
import com.example.service.CompetitionService;
import com.example.service.FileService;
import com.example.service.PostService;
import com.example.service.impl.AccountServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SpringBootTest
class BackEndApplicationTests {

	@Resource
	CompetitionService service;
	@Resource
	FileService service2;
	@Resource
	AccountServiceImpl service3;
	@Resource
	PostService service4;

	@Value("${spring.security.oauth2.client.registration.qq.client-id}")
	String client_id;
	@Value("${spring.security.oauth2.client.registration.qq.client-secret}")
	String client_secret;

	@Test
	void contextLoads() throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
    String a="access_token=bbb123&expires_in=aaa&openid=zl";
		String[] params = a.split("&");
		System.out.println("1:"+ Arrays.toString(params));
		String openid = null;
		for (String param : params) {
			System.out.println("2:"+param);
			String[] keyvalue = param.split("=");
			System.out.println("3:"+ Arrays.toString(keyvalue));
			if (keyvalue[0].equals("openid")) {
				openid = keyvalue[1];
				break;
			}
		}
		System.out.println(openid);
		System.out.println(client_id);
		System.out.println(client_secret);
		//       Integer a= 199;
//       Integer b=199;
//	   Integer e=127;
//	   Integer f=127;
//		int c=199;
//		int d=199;
//		System.out.println(a.equals(b));
//		System.out.println(e.equals(f));
//		System.out.println(a==b);
//		System.out.println(e==f);
//		System.out.println(c==d);

//		int a = -8; // 假设只有8位表示，二进制：11111000（补码）
//		int b = a >> 2; // 结果为1073741820，二进制：00111111 11111100（无符号右移2位，左侧用0填充）
//		System.out.println(b);
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String rawPassword = "123456";
//		String encodedPassword = passwordEncoder.encode(rawPassword);
//		System.out.println(encodedPassword); // 打印加密后的密码
//		service3.deleteUserInfoFromRedis("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi6ZKf6Zu3MSIsImlkIjo3LCJleHAiOjE3MTQ4MTY0OTcsImlhdCI6MTcxNDIxMTY5NywianRpIjoiMzQzYTU5MGUtNDRhOS00MDE2LTg4NzMtZjRmNjkyY2IwMDc1IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9zdHVkZW50Il19.Q9NkjtfMm55ex6UC_WwCZaDTJFoG7GQHVfycxG_PcPw");
//		Map<String, Object> map =service3.objectToMap(service3.findAccountByTelOrEmail("2786696108@qq.com"));// 打印Map内容
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//		}
//        service3.updateTx("1","2.jpg");
//		service2.deleteFile("C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\tx\\6.png");
//		String date=service.getCompetitionInfo().get(0).getCompetitionTime();
//		System.out.println(date);

	}

}
