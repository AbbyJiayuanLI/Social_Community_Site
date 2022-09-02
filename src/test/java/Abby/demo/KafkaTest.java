package Abby.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)		//peizhi lei
public class KafkaTest {

	@Autowired
	private KafkaProducer kafkaProducer;
	
	@Test
	public void testKafka() {
		
		kafkaProducer.sendMsg("test", "123123123");
		kafkaProducer.sendMsg("test", "111111111");
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	
}

@Component
class KafkaProducer{
	
	@Autowired
	private KafkaTemplate kafkaTemplate;
	
	public void sendMsg(String topic, String msg) {
		kafkaTemplate.send(topic, msg);
	}
}

@Component
class KafkaConsumer{
	
	@KafkaListener(topics= {"test"})
	public void handleMessage(ConsumerRecord record) {
		 System.out.println(record.value());
	}

}