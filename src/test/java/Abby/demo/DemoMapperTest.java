package Abby.demo;

 import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import Abby.demo.dao.UserMapper;
import Abby.demo.entity.User;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)		//peizhi lei
public class DemoMapperTest{
	
	@Autowired
	private UserMapper UserMapper;
	
	@Test
	public void testSelect() {
		User user = UserMapper.selectById(101);
		System.out.println(user);
//		System.out.println("111111");
		
	}

}
