package Abby.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import Abby.demo.dao.LoginTicketMapper;
import Abby.demo.dao.UserMapper;
import Abby.demo.entity.LoginTicket;
import Abby.demo.entity.User;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.MailClient;
import Abby.demo.util.demoUtil;

@Service
public class UserService implements DemoConstant {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	LoginTicketMapper loginTicketMapper;
	
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
		User u1 = userMapper.selectByName(user.getUsername());
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
	
	public int activation(int userId, String code) {
		User user = userMapper.selectById(userId);
		if (user.getStatus()==1) {
			return ACTIVATION_REPEAT;
		}else if (user.getActivationCode().equals(code)) {
			userMapper.updateStatus(userId, 1);
			return ACTIVATION_SUCCESS;
		} else {
			return ACTIVATION_FAILURE;
		}
	}
	
	public Map<String, Object> login(String username, String password, long expiredSec){
		Map<String, Object> map = new HashMap<>();
		
		if (StringUtils.isBlank(username)){
			map.put("usernameMsg", "用户名不能为空！");
			return map;
		}
		if (StringUtils.isBlank(password)){
			map.put("passwordMsg", "密码不能为空！");
			return map;
		}
		User user = userMapper.selectByName(username); 
		if (user==null) {
			map.put("usernameMsg", "该账号不存在！");
			return map;
		}
		if (user.getStatus()==0) {
			map.put("usernameMsg", "该账号未激活！");
			return map;
		}
		password = demoUtil.md5(password+user.getSalt());
		if (!user.getPassword().equals(password)) {
			map.put("passwordMsg", "密码不正确！");
			return map;
		}
		
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(user.getId());
		loginTicket.setTicket(demoUtil.genUUID());
		loginTicket.setStatus(0);
		loginTicket.setExpire(new Date(System.currentTimeMillis()+1000*expiredSec));
		loginTicketMapper.insertLoginTicket(loginTicket);
		map.put("ticket", loginTicket.getTicket());
		
		return map;
	}
	
	public void logout(String ticket) {
		loginTicketMapper.updateStatus(ticket, 1);
	}
	
	public LoginTicket findLoginTicket(String ticket) {
		return loginTicketMapper.selectByTicket(ticket);
	}
	
	
	
	
	
}
