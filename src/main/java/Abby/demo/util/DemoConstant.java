package Abby.demo.util;

public interface DemoConstant {
	
	// 激活成功
	int ACTIVATION_SUCCESS = 0;
	
	// 激活重复
	int ACTIVATION_REPEAT = 1;
		
	// 激活失败
	int ACTIVATION_FAILURE = 2;
	
	// 默认登陆凭证超时时间
	int DEFAULT_EXPIRE_SEC = 12*60*60;
	
	// 记住之后登陆凭证超时时间
	int REMEMBER_EXPIRE_SEC = 100*12*60*60;
	
	// EntityType
	int ENTITY_TYPE_POST = 1;
	int ENTITY_TYPE_COMMENT = 2;
	
}
