package Abby.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Abby.demo.entity.Message;
import Abby.demo.entity.Page;
import Abby.demo.entity.User;
import Abby.demo.service.MessageService;
import Abby.demo.service.UserService;
import Abby.demo.util.HostHolder;

@Controller
public class MessageController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;
    
    // 私信
    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
    	User user = hostHolder.getUser();
    	
    	page.setLimit(5);
    	page.setPath("/letter/list");
    	page.setRows(messageService.findConversationCount(user.getId()));
    	
    	List<Message> convList = messageService.findConversations(user.getId(),page.getOffset(), page.getLimit());
    	List<Map<String, Object>> convs = new ArrayList<Map<String,Object>>();
    	if (convList != null) {
    		for (Message msg : convList) {
    			Map<String,Object> map = new HashMap<String, Object>();
    			map.put("conversation", msg);
    			map.put("letterCount", messageService.findLetterCount(msg.getConversationId()));
    			map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), msg.getConversationId()));
    			int targetId = user.getId() == msg.getFromId() ? msg.getToId() : msg.getFromId();
                map.put("target", userService.findById(targetId));
                convs.add(map);
    		}
    	}
        model.addAttribute("conversations", convs);
	    
	    int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
	    model.addAttribute("letterUnreadCount", letterUnreadCount);
	
	    return "/site/letter";
    }
    
    

}
