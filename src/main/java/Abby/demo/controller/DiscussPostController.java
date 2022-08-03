package Abby.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.User;
import Abby.demo.service.DiscussPostService;
import Abby.demo.service.UserService;
import Abby.demo.util.HostHolder;
import Abby.demo.util.demoUtil;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(path="/add",method = RequestMethod.POST)
	@ResponseBody
	public String addDiscussPost(String title, String content) {
		User user = hostHolder.getUser();
		if (user==null) {
			return demoUtil.getJSONString(403, "暂未登陆");
		}
		DiscussPost discussPost = new DiscussPost();
		discussPost.setUserId(user.getId());
		discussPost.setTitle(title);
		discussPost.setContent(content);
		discussPost.setCreateTime(new Date());
		discussPostService.addDiscussPost(discussPost);
		
		// 报错统一处理
		return demoUtil.getJSONString(0, "发布成功");
	}
	
	@RequestMapping(path="/detail/{postId}",method = RequestMethod.GET)
	public String getDiscussPost(@PathVariable("postId") int postId, Model model) {
		DiscussPost discussPost = discussPostService.findDisPostById(postId);
		model.addAttribute("post", discussPost);
		User user = userService.findById(discussPost.getUserId());
		model.addAttribute("user", user);
		return "/site/discuss-detail";
	}

}
