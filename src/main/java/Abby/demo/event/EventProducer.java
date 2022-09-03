package Abby.demo.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import Abby.demo.entity.Event;

@Component
public class EventProducer {
	
	@Autowired
	private KafkaTemplate kafkaTemplate;
	
	public void pubEvent(Event event) {
		kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
	}

}
