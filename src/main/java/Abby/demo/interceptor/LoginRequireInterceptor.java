package Abby.demo.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import Abby.demo.Annotation.LoginRequire;
import Abby.demo.util.HostHolder;

@Component
public class LoginRequireInterceptor implements HandlerInterceptor {

	@Autowired
	HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			LoginRequire loginRequire = method.getAnnotation(LoginRequire.class);
			if (loginRequire!=null && hostHolder.getUser()!=null) {
				System.out.println(request.getContextPath());
				response.sendRedirect(request.getContextPath()+"/login");
				return false;
			}
		}
		return true;
	}
	
	
}
