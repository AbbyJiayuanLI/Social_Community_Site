package Abby.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Abby.demo.dao.UserMapper;
import Abby.demo.entity.User;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	public User findById(int id) {
		return userMapper.selectById(id);
	}
	
}
