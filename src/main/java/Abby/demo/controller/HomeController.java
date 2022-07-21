package Abby.demo.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.dao.DiscussPostMapper;
import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.User;
import Abby.demo.service.UserService;

@Controller
public class HomeController {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Autowired
	private UserService userService;
	
	@RequestMapping(path="/index", method=RequestMethod.GET)
	public String getIndexPage(Model model) {
		List<DiscussPost> list = discussPostMapper.selectDisPosts(0, 0, 0);
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if (list!=null) {
			for (DiscussPost post:list) {
				Map<String,Object> map = new HashMap<>();
				map.put("post",post);
				User user = userService.findById(post.getUserId());
				map.put("user", user);
				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		return "/index";
	}
	
}
