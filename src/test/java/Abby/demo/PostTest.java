package Abby.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import Abby.demo.util.SensitiveFilter;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class PostTest {
	
	@Autowired
	SensitiveFilter sensitiveFilter;
	
	@Test
	public void testFilter() {
		String text = "这是吸@毒一条嫖娼测试赌博";
		System.out.println(sensitiveFilter.filter(text));
	}

}
