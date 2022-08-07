package Abby.demo.util;

public class RedisKeyUtil {
	
	private static final String SPLIT = ":";
	private static final String PREFIX_LIKE = "like:entity";
	
	//实体：like:entity:entityType:entityId -> set(userId)
	public String getEntityLikeKey(int entityType, int entityId) {
		return PREFIX_LIKE+SPLIT+entityType+SPLIT+entityId;
	}

}
