package Abby.demo.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.google.code.kaptcha.Producer;
import com.sun.mail.util.LogOutputStream;

import Abby.demo.entity.User;
import Abby.demo.service.UserService;
import Abby.demo.util.DemoConstant;

@Controller
public class LoginController implements DemoConstant{
	
	@Autowired
	UserService userService;
	
	@Autowired
	Producer kaptchaProducer;
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

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
	
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public String Login(String username, String password, String code, Boolean remembered,
						HttpSession session, HttpServletResponse response, Model model) {
		String kaptcha = (String) session.getAttribute("kaptcha");
		if (StringUtils.isBlank(kaptcha)|| StringUtils.isBlank(code)
				||!code.equalsIgnoreCase(kaptcha)) {
			model.addAttribute("codeMSG","验证码不正确！");
			return "/site/login";
		}
		
		int expirySec = remembered ? REMEMBER_EXPIRE_SEC : DEFAULT_EXPIRE_SEC;
		Map<String, Object> map = userService.login(username, password, expirySec);
		
		if (map.containsKey("ticket")) {
			Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
			cookie.setPath(contextPath);
			cookie.setMaxAge(expirySec);
			response.addCookie(cookie);
			return "reirect:/index";
		}else {
			model.addAttribute("usernameMSG",map.get("usernameMSG"));
			model.addAttribute("passwordMSG",map.get("passwordMSG"));
			return "/site/login";
		}
			
	}
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public String Logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);
		return "redirect:/login";
	}
	
	
	
	
	@RequestMapping(path="/kaptcha",method = RequestMethod.GET)
	public void getKaptcha(HttpServletResponse response, HttpSession session) {
		String text = kaptchaProducer.createText();
		BufferedImage img =  kaptchaProducer.createImage(text);
		
		// session 存储
		session.setAttribute("kaptch", text);
		
		// 输出图片
		response.setContentType("image/png");
		try {
			OutputStream out = response.getOutputStream();
			ImageIO.write(img, "png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("验证错误！"+e.getMessage());
		}
		
	}
	
}
