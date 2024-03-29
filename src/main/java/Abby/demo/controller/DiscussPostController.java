package Abby.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Abby.demo.entity.Comment;
import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.Page;
import Abby.demo.entity.User;
import Abby.demo.service.CommentService;
import Abby.demo.service.DiscussPostService;
import Abby.demo.service.LikeService;
import Abby.demo.service.UserService;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.HostHolder;
import Abby.demo.util.demoUtil;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements DemoConstant{
	
	@Autowired
	private DiscussPostService discussPostService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(path="/add",method = RequestMethod.POST)
	@ResponseBody
	public String addDiscussPost(String title, String content) {
		System.out.println("1111111111111111111");
		User user = hostHolder.getUser();
		if (user==null) {
			return demoUtil.getJSONString(403, "暂未登陆");
		}
		System.out.println(user);
		DiscussPost discussPost = new DiscussPost();
		discussPost.setUserId(user.getId());
		discussPost.setTitle(title);
		discussPost.setContent(content);
		discussPost.setCreateTime(new Date());
		discussPostService.addDiscussPost(discussPost);
		System.out.println(discussPost);
		// 报错统一处理
		return demoUtil.getJSONString(0, "发布成功");
	}
	
	@RequestMapping(path="/detail/{postId}",method = RequestMethod.GET)
	public String getDiscussPost(@PathVariable("postId") int postId, Model model, Page page) {
		DiscussPost discussPost = discussPostService.findDisPostById(postId);
		model.addAttribute("post", discussPost);
		User user = userService.findById(discussPost.getUserId());
		model.addAttribute("user", user);
		long count = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
		int status = hostHolder.getUser()==null ? 0 :
				likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST,  discussPost.getId());
		model.addAttribute("likeCount", count);
		model.addAttribute("likeStatus", status);
				
		page.setLimit(5);
		page.setPath("/discuss/detail/"+postId);
		page.setRows(discussPost.getCommentCount());
		
		// comment评论：给帖子的评论
		//  reply回复： 给评论的评论
		List<Comment> commentList = commentService.findCommentByEntity(
				ENTITY_TYPE_POST, discussPost.getId(), 
				page.getOffset(), page.getLimit());
		List<Map<String, Object>> commentVOList = new ArrayList<Map<String,Object>>();
		if (commentList!=null) {
			for (Comment comment : commentList) {
				Map<String, Object> commentVO = new HashMap<String, Object>();
				commentVO.put("comment", comment);
				commentVO.put("user", userService.findById(comment.getUserId()));
				
				// 查找like
				count = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
				status = hostHolder.getUser()==null ? 0 :
						likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
				commentVO.put("likeCount", count);
				commentVO.put("likeStatus", status);
				
				// 查找回复
				List<Comment> replyList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT, 
						comment.getId(), 0, Integer.MAX_VALUE);
				List<Map<String, Object>> replyVOList = new ArrayList<Map<String,Object>>();
				if (replyList!=null) {
					for (Comment reply : replyList) {
						Map<String, Object> replyVO = new HashMap<String, Object>();
						replyVO.put("reply", reply);
						replyVO.put("user", userService.findById(reply.getUserId()));
						User target = reply.getTargetId() == 0 ? null : userService.findById(reply.getTargetId());
						replyVO.put("target", target);
						
						count = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
						status = hostHolder.getUser()==null ? 0 :
								likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
						replyVO.put("likeCount", count);
						replyVO.put("likeStatus", status);
						replyVOList.add(replyVO);
					}
				}
				commentVO.put("replys", replyVOList);
				commentVO.put("replyCount", commentService.findCountByEntity(
						ENTITY_TYPE_COMMENT, comment.getEntityId()));
				
				
				commentVOList.add(commentVO);
			}
			
		}
		model.addAttribute("comments", commentVOList);
		return "/site/discuss-detail";
	}

}
