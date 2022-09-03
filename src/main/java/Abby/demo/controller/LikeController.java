package Abby.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Abby.demo.entity.Event;
import Abby.demo.entity.User;
import Abby.demo.event.EventProducer;
import Abby.demo.service.LikeService;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.HostHolder;
import Abby.demo.util.demoUtil;

@Controller
public class LikeController implements DemoConstant{
	
	@Autowired
	private LikeService likeService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private EventProducer eventProducer;
	
	@RequestMapping(path="like",method = RequestMethod.POST)
	@ResponseBody
	public String like(int entityType, int entityId, int entityUserId, int postId) {
		User user = hostHolder.getUser();
		likeService.like(user.getId(), entityType, entityId, entityUserId);       //记得拦截
		long count = likeService.findEntityLikeCount(entityType, entityId);
		int status = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("likeCount", count);
		map.put("likeStatus", status);
		
		if (status==1) {
			Event event = new Event()
					.setTopic(TOPIC_LIKE)
					.setUserId(user.getId())
					.setEntityType(entityType)
					.setEntityId(entityId)
					.setEntiryUserId(entityUserId);
			event.setMap("postId", postId);
			eventProducer.pubEvent(event);
		}
		
		return demoUtil.getJSONString(0, null, map);
	}

}
