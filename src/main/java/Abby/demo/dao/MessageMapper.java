package Abby.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import Abby.demo.entity.Message;

@Mapper
public interface MessageMapper {
	
	// 查询当前用户会话列表
	List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    int selectConversationCount(@Param("userId") int userId);

    List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    int selectLetterCount(@Param("conversationId") String conversationId);

    int selectLetterUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    int insertMessage(Message message);

    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    Message selectLatesNotice(@Param("userId") int userId, @Param("topic") String topic);

    int selectNoticeCount(@Param("userId") int userId, @Param("topic") String topic);

    int selectNoticeUnreadCount(@Param("userId") int userId, @Param("topic") String topic);

    List<Message> selectNotices(@Param("userId") int userId, @Param("topic") String topic, @Param("offset") int offset, @Param("limit") int limit);

}

	

