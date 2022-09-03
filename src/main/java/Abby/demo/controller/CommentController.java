package Abby.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.entity.Comment;
import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.Event;
import Abby.demo.event.EventProducer;
import Abby.demo.service.CommentService;
import Abby.demo.service.DiscussPostService;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.HostHolder;

@Controller
@RequestMapping("/comment")
public class CommentController implements DemoConstant {
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private EventProducer eventProducer;
	
	@Autowired
	private DiscussPostService discussPostService;
	
	@RequestMapping(path="/add/{postId}",method=RequestMethod.POST)
	public String addComment(@PathVariable("postId") int postId, Comment comment) {
		// 应该检查是否登陆？？
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);
		
		Event event = new Event();
		event.setEntityId(comment.getEntityId());
		event.setEntityType(comment.getEntityType());
		event.setMap("postId", postId);
		event.setTopic(TOPIC_COMMENT);
		event.setUserId(hostHolder.getUser().getId());
		
		// 对象可能是帖子也可能是评论
		// event.setEntiryUserId(entiryUserId);
		if(comment.getEntityType()==ENTITY_TYPE_POST) {
			DiscussPost target = discussPostService.findDisPostById(postId);
			event.setEntiryUserId(target.getUserId());
		} else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
			Comment target = commentService.findCommentById(comment.getEntityId());
			event.setEntiryUserId(target.getUserId());
		}
		
		eventProducer.pubEvent(event);
		
		return "redirect:/discuss/detail/"+postId;
	}
	

}
