package Abby.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.User;
import Abby.demo.service.DiscussPostService;
import Abby.demo.util.HostHolder;
import Abby.demo.util.demoUtil;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@Autowired
	HostHolder hostHolder;
	
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

}
