package Abby.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import Abby.demo.util.MailClient;


@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class MailClientTest {
	
	@Autowired
	MailClient mailClient;
	
	@Test
	public void testSendMail() {
		mailClient.sendMail("abbyjiayuanli@gmail.com", "test mailClient", "test mail content");
		
	}

}
