package Abby.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import Abby.demo.dao.Dao1;
import net.bytebuddy.asm.Advice.This;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)		//peizhi lei

class DemoApplicationTests implements ApplicationContextAware{		// implement xxx

	private ApplicationContext applicationContext;
	
	@Test
	void contextLoads() {
		System.out.println(applicationContext);
		
		Dao1 dao1 = applicationContext.getBean("alpha_dao1", Dao1.class);
		System.out.println(dao1.select());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
		
	}
	
	

}
