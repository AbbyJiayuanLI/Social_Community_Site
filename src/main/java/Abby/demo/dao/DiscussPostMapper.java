package Abby.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import Abby.demo.entity.DiscussPost;

@Mapper
public interface DiscussPostMapper {
	List<DiscussPost> selectDisPosts(@Param("userId") int userId,
									 @Param("offset") int offset, 
									 @Param("limit") int limit);
	
	// ???????
	int selectDisPostRows(@Param("userId") int userId);
	
	int insertDisPost(DiscussPost discussPost);
	
	DiscussPost selectDisPostById(int id);
}
