package Abby.demo;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import Abby.demo.util.MailClient;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.StatusManager;


@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class MailClientTest {
	
	@Autowired
	MailClient mailClient;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Test
	public void testSendMail() {
		mailClient.sendMail("abbyjiayuanli@gmail.com", "test mailClient", "test mail content");
		
	}
	
	
	@Test
	public void testHtmlMail() {
		Context context = new Context();
		context.setVariable("username", "xxxname");
		
		String content = templateEngine.process("/mail/demo",context);
		System.out.println(content);
		
		mailClient.sendMail("abbyjiayuanli@gmail.com", "test html", content);
	}

}
