package Abby.demo.util;

import org.springframework.stereotype.Component;

import Abby.demo.entity.User;

// 用于持有用户信息，替代session
@Component
public class HostHolder {
	private ThreadLocal<User> users = new ThreadLocal<User>();
	
	public void setUser(User user) {
		users.set(user);
	}
	
	public User getUser() {
		return users.get();
	}
	
	public void clear() {
		users.remove();
	}
}
