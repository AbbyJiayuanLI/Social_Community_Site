package Abby.demo.util;

public class RedisKeyUtil {
	
	private static final String SPLIT = ":";
	private static final String PREFIX_LIKE = "like:entity";
	private static final String PREFIX_FOLLOWER = "follower";
	private static final String PREFIX_FOLLOWEE = "followee";
	private static final String PREFIX_KAPTCHA = "kaptcha";
	private static final String PREFIX_REDIS_TICKET = "ticket";
	private static final String PREFIX_USER = "user";
	
	//实体的赞：like:entity:entityType:entityId -> set(userId)
	public static String getEntityLikeKey(int entityType, int entityId) {
		return PREFIX_LIKE+SPLIT+entityType+SPLIT+entityId;
	}
	
	//个体的赞：like:user:userId -> int
	public static String getUserLikeKey(int userId) {
		return PREFIX_LIKE+SPLIT+userId;
	}
	
	//用户关注的实体：followee:userId:entityType -> zset(entityId, now)
	public static String getFolloweeKey(int userId, int entityType) {
		return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
	}
	
	//实体的粉丝：follower:entityType:entityId -> zset(userId, now)
	public static String getFollowerKey(int entityType, int entityId) {
		return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
	}
	
	// 获取kaptcha的key
	public static String getKaptchaKey(String owner) {
		return PREFIX_KAPTCHA+SPLIT+owner;
	}
	
	// 获取loginticket的key
	public static String getTicketKey(String ticket) {
		return PREFIX_REDIS_TICKET+SPLIT+ticket;
	}
	
	// 获取user的key
	public static String getUserKey(int userId) {
		return PREFIX_USER+SPLIT+userId;
	}

}
