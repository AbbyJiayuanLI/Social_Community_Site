package Abby.demo;

 import java.util.Date;
import java.util.List;

import org.apache.ibatis.javassist.expr.NewArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import Abby.demo.dao.UserMapper;
import Abby.demo.dao.DiscussPostMapper;
import Abby.demo.dao.LoginTicketMapper;
import Abby.demo.entity.DiscussPost;
import Abby.demo.entity.LoginTicket;
import Abby.demo.entity.User;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)		//peizhi lei
public class DemoMapperTest{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private DiscussPostMapper discussPostMapper;
	
	@Autowired
	LoginTicketMapper loginTicketMapper;
	
	@Test
	public void testSelect() {
		User user = userMapper.selectById(101);
		System.out.println(user);
//		System.out.println("111111");
	}
	
	@Test
	public void testSelectDisMapper() {
		List<DiscussPost> list = discussPostMapper.selectDisPosts(0,0,10);
		for (DiscussPost post : list) {
			System.out.println(post);
		}
		
		int row = discussPostMapper.selectDisPostRows(0);
		System.out.println(row);
	}
	
	@Test
	public void testLoginTicketMapper() {
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(101);
		loginTicket.setStatus(0);
		loginTicket.setTicket("abc");
		loginTicket.setExpire(new Date(System.currentTimeMillis()+1000*60*10));
		
		loginTicketMapper.insertLoginTicket(loginTicket);
	}

}
