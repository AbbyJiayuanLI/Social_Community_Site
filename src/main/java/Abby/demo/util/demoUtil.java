package Abby.demo.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class demoUtil {
	
	//generate random str
	public static String genUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	// MD5 encryption (salt在service加)
	public static String md5(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		} else {
			return DigestUtils.md5DigestAsHex(key.getBytes());
		}
		
	}
	

}
