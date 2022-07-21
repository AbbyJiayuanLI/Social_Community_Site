package Abby.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Abby.demo.dao.DiscussPostMapper;
import Abby.demo.entity.DiscussPost;

@Service
public class DiscussPostService {
	
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
		return discussPostMapper.selectDisPosts(userId, offset, limit);
	}
	
	public int findDiscussPostRows(int userId) {
		return discussPostMapper.selectDisPostRows(userId);
	}

}
