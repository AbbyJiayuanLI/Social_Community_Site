package Abby.demo.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import Abby.demo.entity.LoginTicket;
import Abby.demo.entity.User;
import Abby.demo.service.UserService;
import Abby.demo.util.CookieUtil;
import Abby.demo.util.HostHolder;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor{
	
	@Autowired
	UserService userService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ticket = CookieUtil.getVal(request, "ticket");
		if (ticket!=null) {
			LoginTicket loginTicket = userService.findLoginTicket(ticket);
			
			// 检查是否有效
			if (loginTicket!=null 
					&& loginTicket.getStatus()==0 
					&& loginTicket.getExpire().after(new Date())) {
				User user = userService.findById(loginTicket.getUserId());
				
				// 在本次请求中保存user
				hostHolder.setUser(user);
			}
		}
		
		return true;
	}
	
	// 储存数据
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		User user =  hostHolder.getUser();
		if (user==null || modelAndView==null) {
			return;
		}
		modelAndView.addObject("loginUser", user);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		hostHolder.clear();
	}
	

}
