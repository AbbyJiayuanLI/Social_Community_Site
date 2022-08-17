package Abby.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import Abby.demo.entity.User;
import Abby.demo.util.DemoConstant;
import Abby.demo.util.RedisKeyUtil;

@Service
public class FollowService implements DemoConstant{
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private UserService userService;
	
	public void follow(int userId, int entityType, int entityId) {
		redisTemplate.execute(new SessionCallback() {

			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				
				operations.multi();
				operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
				operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
				return operations.exec();
			}
	
		});
	}
	
	public void unFollow(int userId, int entityType, int entityId) {
		redisTemplate.execute(new SessionCallback() {

			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				
				operations.multi();
				operations.opsForZSet().remove(followeeKey, entityId);
				operations.opsForZSet().remove(followerKey, userId );
				return operations.exec();
			}
	
		});
	}
	
	// 关注实体数量
	public long findFolloweeCount(int userId, int entityType) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return redisTemplate.opsForZSet().zCard(followeeKey);
	}
	
	// 粉丝数量
	public long findFollowerCount(int entityId, int entityType) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return redisTemplate.opsForZSet().zCard(followerKey);
	}
	
	// 当前用户是否关注该实体
	public boolean hasFollowed(int userId, int entityType, int entityId) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return redisTemplate.opsForZSet().score(followeeKey, entityId)!=null;
	}
	
	// 查询用户关注 (只指人)
	public List<Map<String,Object>> findFollowees(int userId, int offset, int limit){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset+limit-1);
		if (targetIds==null) {
			return null;
		}
		List<Map<String,Object>> res = new ArrayList<>();
		for (Integer id : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.findById(id);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followeeKey,id);
			map.put("followTime", new Date(score.longValue()));
			res.add(map);
		}
		return res;
	}
	
	// 查询用户粉丝
	public List<Map<String,Object>> findFollowers(int userId, int offset, int limit){
		String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
		Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset+limit-1);
		if (targetIds==null) {
			return null;
		}
		List<Map<String,Object>> res = new ArrayList<>();
		for (Integer id : targetIds) {
			Map<String, Object> map = new HashMap<>();
			User user = userService.findById(id);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followerKey,id);
			map.put("followTime", new Date(score.longValue()));
			res.add(map);
		}
		return res;
	}
}
