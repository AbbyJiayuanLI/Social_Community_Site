package Abby.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import Abby.demo.entity.DiscussPost;

@Mapper
public class DiscussPostMapper {
	List<DiscussPost> selectDisPosts(int userId, int offset, int limit);
	
	// ???????
	int selectDisPostRows(@Param("userId") int userId);
}
