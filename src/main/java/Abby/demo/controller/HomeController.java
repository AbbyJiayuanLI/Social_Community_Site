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
import Abby.demo.entity.Page;
import Abby.demo.entity.User;
import Abby.demo.service.DiscussPostService;
import Abby.demo.service.LikeService;
import Abby.demo.service.UserService;
import Abby.demo.util.DemoConstant;

@Controller
public class HomeController implements DemoConstant{
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DiscussPostService discussPostService;
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(path="/index", method=RequestMethod.GET)
	public String getIndexPage(Model model, Page page) {
		//SpringMVC自动实例化Model和Page
		page.setRows(discussPostService.findDiscussPostRows(0));
		page.setPath("/index");
		
		List<DiscussPost> list = discussPostMapper.selectDisPosts(0, page.getOffset(), page.getLimit());
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if (list!=null) {
			for (DiscussPost post:list) {
				Map<String,Object> map = new HashMap<>();
				map.put("post",post);
				User user = userService.findById(post.getUserId());
				map.put("user", user);
				
				long count = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
				map.put("likeCount", count);
				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		//可以省略.addAttribute ？？？？
		return "/index";
	}
	
}
