package cn.xgame.net.event.all.gm;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import cn.xgame.net.http.classes.IEvent;

public enum Events {

	//-----------------用户
	USER_LOGIN					( 101, 					new LoginEvent() ),
	USER_SIGNUP					( 102, 					new SignupEvent() );
	
	
	private final int			number;
	private final IEvent 		eventInstance;
	
	Events( int value, IEvent eventInstance ) {
		if( value >= Short.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "包号不符合规范：" + value );
		}
		this.number 		= value;
		this.eventInstance 	= eventInstance;
		this.eventInstance.setEventId( number );
	}
	
	private static final Map<Integer, Events> numToEnum = new HashMap<Integer, Events>();
	
	static{
		for( Events a : values() ){
			
			Events p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( "通信包" + a.number + "重复了" );
			}
		}
	}
	
	public IEvent getEventInstance() {
		return eventInstance;
	}
	public int toNumber() {
		return number;
	}
	public static Events fromNum( int n ){
		return numToEnum.get( n );
	}
	
	/**
	 * 运行此枚举所对应的包的run方法
	 * @throws IOException
	 */
	public void run(ChannelHandlerContext ctx, JSONObject data, HttpRequest request) throws IOException {
		eventInstance.run( ctx, data, request );
	}
}
