package Abby.demo.controller;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.entity.User;
import Abby.demo.service.UserService;
import Abby.demo.util.DemoConstant;

@Controller
public class LoginController implements DemoConstant{
	
	@Autowired
	UserService userService;

	@RequestMapping(path="/register",method=RequestMethod.GET)
	public String getRegisterPage() {
		return "/site/register";
	}
	
	@RequestMapping(path="/login",method=RequestMethod.GET)
	public String getLoginPage() {
		return "/site/login";
	}
	
	@RequestMapping(path="/register",method=RequestMethod.POST)
	public String register(Model model, User user) {
		Map<String, Object> map = userService.register(user);
		if (map==null || map.isEmpty()) {
			// 转到operate-result进行提示
			model.addAttribute("msg", "已注册成功，激活邮件已发送到您的邮箱，请尽快激活！");
			model.addAttribute("target", "/index");
			return "/site/operate-result";
		}else {
			// 解析map内容，返回
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("mailMsg", map.get("mailMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			return "/site/register";
		}
	}
	
	@RequestMapping(path="/activation/{userId}/{code}", method=RequestMethod.GET)
	public String activation(Model model, @PathVariable("userId") int userId, 
							@PathVariable("code") String code) {
		int res = userService.activation(userId, code);
		if (res==ACTIVATION_SUCCESS) {
			model.addAttribute("msg", "恭喜您已激活成功！");
			model.addAttribute("target", "/login");
		}else if (res==ACTIVATION_REPEAT) {
			model.addAttribute("msg", "无效操作，该账号已激活过！");
			model.addAttribute("target", "/index");
		}else {
			model.addAttribute("msg", "激活失败，您提供的激活码不正确！");
			model.addAttribute("target", "/index");
		}
		return "/site/operate-result";
	}
	
	
}
