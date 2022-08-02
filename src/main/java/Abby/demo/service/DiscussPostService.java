package Abby.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import Abby.demo.dao.DiscussPostMapper;
import Abby.demo.entity.DiscussPost;
import Abby.demo.util.SensitiveFilter;

@Service
public class DiscussPostService {
	
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Autowired
	private SensitiveFilter sensitiveFilter;
	
	public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
		return discussPostMapper.selectDisPosts(userId, offset, limit);
	}
	
	public int findDiscussPostRows(int userId) {
		return discussPostMapper.selectDisPostRows(userId);
	}
	
	public int addDiscussPost(DiscussPost discussPost) {
		if (discussPost==null) {
			throw new IllegalArgumentException("参数不能为空！");
		}
		
		//转义html标记
		discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
		discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
		
		//过滤
		discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
		discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
		
		//插入
		return discussPostMapper.insertDisPost(discussPost);
	}
}
