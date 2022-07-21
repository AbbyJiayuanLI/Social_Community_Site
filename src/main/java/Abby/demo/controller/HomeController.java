package Abby.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.dao.DiscussPostMapper;
import Abby.demo.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Autowired
	private UserService userService;
	
	@RequestMapping(path="/index", method=RequestMethod.GET)
	public String getIndexPage(Model model) {
		
		return "/index";
	}
	
}
