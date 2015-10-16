package cn.xgame.net.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息包描述
 * 用法
 * 	Class<?> c = event.getClass();
 * 	EventDescrip desc = c.getAnnotation(EventDescrip.class);
 * 	
 * @author deng		
 * @date 2015-10-16 下午5:47:11
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventDescrip {

	/**
	 * 包描述
	 * @return
	 */
	public String desc() default "";
	
	/**
	 * 包结构
	 * @return
	 */
	public String structure() default "";
	
	
}
