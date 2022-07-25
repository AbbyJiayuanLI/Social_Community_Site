package Abby.demo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.PrettyPrinter;

import Abby.demo.util.demoUtil;

@Controller
@RequestMapping("/alpha")
public class controller1 {
	
	@RequestMapping("/hello")
	@ResponseBody
	public String sayHello() {
		return "Hello Spring Boot.";
	}
	
	@RequestMapping("/http")
	public void http(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(request.getMethod());
		System.out.println(request.getServletPath());
		Enumeration<String> enumeration =  request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			String value = request.getHeader(name);
			System.out.println(name+": "+value);
		}
		System.out.println(request.getParameter("code"));
		
		//返回响应数据
		response.setContentType("text/html;charset=utf-8");
		try (PrintWriter writer =  response.getWriter();){
			writer.write("<h1>testestest</h1>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//1-a chuli GET request
	// /students?current=1&limit=20
	@RequestMapping(path="/students",method=RequestMethod.GET)
	@ResponseBody
	public String getStudents(
			@RequestParam(name="current", required=false, defaultValue="10") int current, 
			@RequestParam(name="limit", required=false, defaultValue="10") int limit) {
		System.out.println(current);
		System.out.println(limit);
		return String.valueOf(current);
	}
	
	@RequestMapping(path="/students/{id}",method=RequestMethod.GET)
	@ResponseBody
	public String getStudents(@PathVariable("id") int id) {
		System.out.println(id);
		return String.valueOf(id);
	}
	
	//1-b chuli POST request 
	@RequestMapping(path="/student",method=RequestMethod.POST)
	@ResponseBody
	public String saveStudent(String name, int age) {
		System.out.println(name);
		System.out.println(age);
		return "success";
		
	}
	
	//2 xiangying html data
	@RequestMapping(path="/teacher",method=RequestMethod.GET)
	public ModelAndView getTeacher() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("name", "aaa");
		mav.addObject("age", "56");
		mav.setViewName("/demo/view");
		return mav;
		
	}
	
	@RequestMapping(path="/school",method=RequestMethod.GET)
	public String getSchool(Model model) {
		model.addAttribute("name", "beijing daxue");
		model.addAttribute("age", 80);
		return "/demo/view";
	}
	
	// 3 xiangying JSON
	// when handle unsynchronized request: 
	// page stay, but visit sever (username check)
	// Java object -> JSON zifuchuan -> js
	@RequestMapping(path="/emp", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getEmp(){
		Map<String, Object> emp = new HashMap<>();
		emp.put("111", "123");
		emp.put("222", "223");
		emp.put("333", "323");
		return emp;
	}
	
	// 4. cookies相关
	@RequestMapping(path="/cookies/set",method = RequestMethod.GET)
	@ResponseBody
	public String setCookies(HttpServletResponse response) {
		Cookie cookie = new Cookie("code", demoUtil.genUUID());
		cookie.setPath("/community/alpha");
		cookie.setMaxAge(60*10);
		response.addCookie(cookie);
		
		return "cookie set finished";
	}
	
	@RequestMapping(path="/cookies/get",method=RequestMethod.GET)
	@ResponseBody
	public String getCookie(@CookieValue("code") String code) {
		System.out.println(code);
		return code;
	}
	
	// 5. Session相关
	@RequestMapping(path="/session/set",method = RequestMethod.GET)
	@ResponseBody
	public String setSession(HttpSession session) {
		session.setAttribute("id", 1);
		session.setAttribute("name", "test");
		return "session set finished";
	}
	
	@RequestMapping(path="/session/get",method = RequestMethod.GET)
	@ResponseBody
	public String getSession(HttpSession session) {
		System.out.println(session.getAttribute("id"));
		System.out.println(session.getAttribute("name"));
		
		return "session get";
	}

}
