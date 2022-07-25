package Abby.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import Abby.demo.entity.User;

@Mapper 
public interface UserMapper {
	
	User selectById(int id);
	User selectByName(String username);
	User selectByEmail(String email);
	int insertUser(User user);
	int updateStatus(int id, int status);
	int updateHeader(int id, String headerUrl);
	int updatePassword(int id, String password);

}
