package Abby.demo.util;

public class RedisKeyUtil {
	
	private static final String SPLIT = ":";
	private static final String PREFIX_LIKE = "like:entity";
	
	//实体的赞：like:entity:entityType:entityId -> set(userId)
	public static String getEntityLikeKey(int entityType, int entityId) {
		return PREFIX_LIKE+SPLIT+entityType+SPLIT+entityId;
	}
	
	//个体的赞：like:user:userId -> int
	public static String getUserLikeKey(int userId) {
		return PREFIX_LIKE+SPLIT+userId;
	}

}
