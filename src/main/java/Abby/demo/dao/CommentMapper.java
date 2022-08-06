package Abby.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import Abby.demo.entity.Comment;

@Mapper
public interface CommentMapper {
	
	public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
	
	public int selectCountByEntity(int entityType, int entityId);
	
	public int insertComment(Comment comment);

}
