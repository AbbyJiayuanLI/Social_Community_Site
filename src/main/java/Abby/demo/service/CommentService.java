package Abby.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import Abby.demo.dao.CommentMapper;
import Abby.demo.entity.Comment;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.SensitiveFilter;

@Service
public class CommentService implements DemoConstant{
	
	@Autowired
	CommentMapper commentMapper;
	
	@Autowired
	SensitiveFilter sensitiveFilter;
	
	@Autowired
	DiscussPostService discussPostService;
	
	public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit){
		return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
	}
	
	public int findCountByEntity(int entityType, int entityId) {
		return commentMapper.selectCountByEntity(entityType, entityId);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED,
					propagation = Propagation.REQUIRED)
	public int addComment(Comment comment) {
		// 添加评论
		if (comment==null) {
			throw new IllegalArgumentException("参数不能为空！");
		}
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(sensitiveFilter.filter(comment.getContent()));
		int rows = commentMapper.insertComment(comment);

		// 更新commentCount
		if (comment.getEntityType()==ENTITY_TYPE_POST) {
			int count = commentMapper.selectCountByEntity(ENTITY_TYPE_POST, comment.getEntityId());
			discussPostService.updateCommentCount(comment.getEntityId(), count);
		}
		
		return rows;
	}
	
	public Comment findCommentById(int id) {
		return commentMapper.selectCommentById(id);
	}
	

}
