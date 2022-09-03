package Abby.demo.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import Abby.demo.entity.Event;
import Abby.demo.entity.Message;
import Abby.demo.service.MessageService;
import Abby.demo.util.DemoConstant;

@Component
public class EventConsumer implements DemoConstant{
	
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

	
	@Autowired
	private MessageService messageService;
	
	// 获取event，生成message
	@KafkaListener(topics= {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
	public void handleEvent(ConsumerRecord record) {
		if (record==null || record.value()==null) {
			logger.error("event消息为空！");
			return;
		}
		
		Event event = JSONObject.parseObject(record.value().toString(), Event.class);
		if (event==null) {
			logger.error("event消息格式不正确！");
			return;
		}
		
		Message msg = new Message();
		msg.setFromId(SYSTEM_USER_ID);
		msg.setToId(event.getEntiryUserId());
		msg.setConversationId(event.getTopic());
		msg.setCreateTime(new Date());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", event.getUserId());
		map.put("entityType", event.getEntityType());
		map.put("entityId", event.getEntityId());
		
		if (!event.getMap().isEmpty()) {
			map.putAll(event.getMap());
		}
		msg.setContent(JSONObject.toJSONString(map));
		
		messageService.addMessage(msg);
	}
	
	

}
