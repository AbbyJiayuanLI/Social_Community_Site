package Abby.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class LoggerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

}
