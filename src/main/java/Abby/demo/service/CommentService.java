package Abby.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Abby.demo.dao.CommentMapper;
import Abby.demo.entity.Comment;

@Service
public class CommentService {
	
	@Autowired
	CommentMapper commentMapper;
	
	public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit){
		return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
	}
	
	public int findCountByEntity(int entityType, int entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}
	

}
