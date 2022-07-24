package Abby.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import Abby.demo.dao.UserMapper;
import Abby.demo.entity.User;
import Abby.demo.util.MailClient;
import Abby.demo.util.demoUtil;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Value("${demo.path.domain}")
	private String domain;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	public User findById(int id) {
		return userMapper.selectById(id);
	}
	
	public Map<String, Object> register(User user){
		Map<String, Object> map = new HashMap<String, Object>();
		if (user==null) {
			throw new IllegalArgumentException("user 参数不能为空！");
		}
		if (StringUtils.isBlank(user.getUsername())) {
			map.put("usernameMsg", "账号不能为空！");
			return map;
		}
		if (StringUtils.isBlank(user.getPassword())) {
			map.put("passwordMsg", "密码不能为空！");
			return map;
		}
		if (StringUtils.isBlank(user.getEmail())) {
			map.put("mailMsg", "邮箱不能为空！");
			return map;
		}
		
		// 注册前判断是否已存在
		User u1 = userMapper.selectByUserName(user.getUsername());
		if (u1!=null) {
			map.put("usernameMsg", "账号已存在！");
			return map;
		}
		User u2 = userMapper.selectByEmail(user.getEmail());
		if (u2!=null) {
			map.put("mailMsg", "邮箱已存在！");
			return map;
		}
		
		// 注册
		user.setSalt(demoUtil.genUUID().substring(0, 5));
		user.setPassword(demoUtil.md5(user.getPassword()+user.getSalt()));
		user.setType(0);
		user.setStatus(0);
		user.setActivationCode(demoUtil.genUUID());
		user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
		user.setCreateTime(new Date());
		userMapper.insertUser(user);
		
		//发送激活邮箱
		Context context = new Context();
		context.setVariable("email", user.getEmail());
		// "http://localhost:8080/community/activation/101/code"
		context.setVariable("url", domain+contextPath+
							"/activation/"+user.getId()+"/"+user.getActivationCode());
		
		String content = templateEngine.process("/mail/activation", context);
		mailClient.sendMail(user.getEmail(), "Account Activation Email", content);
		
		// 检查密码重复？？
		return map;
	}
	
	
	
	
	
	
	
}
