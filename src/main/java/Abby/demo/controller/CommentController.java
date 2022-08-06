package Abby.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.entity.Comment;
import Abby.demo.service.CommentService;
import Abby.demo.util.HostHolder;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(path="/add/{postId}",method=RequestMethod.POST)
	public String addComment(@PathVariable("postId") int postId, Comment comment) {
		// 应该检查是否登陆？？
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);
		
		return "redirect:/discuss/detail/"+postId;
	}
	

}
