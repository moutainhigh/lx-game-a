package cn.xgame.net.event;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.xgame.net.event.all.TestEevet;
import cn.xgame.net.event.all.gs.ConnectEvent;
import cn.xgame.net.event.all.gs.RLastGsidEvent;
import cn.xgame.net.event.all.gs.UpdatePeopleEvent;
import cn.xgame.net.event.all.user.GslistEvent;
import cn.xgame.net.event.all.user.LoginEvent;
import cn.xgame.net.event.all.user.SignupEvent;

/**
 * 通信 消息
 * @author deng		
 * @date 2015-6-11 下午3:34:41
 */
public enum Events {

	//-----------------用户
	USER_LOGIN					( 101, 					new LoginEvent() ),
	USER_SIGNUP					( 102, 					new SignupEvent() ),
	APPER_GSLIST				( 110, 					new GslistEvent() ),
	
	
	//-----------------游戏服务器
	GS_CONNECT					( 201, 					new ConnectEvent() ),
	UPDATA_PEOPLE				( 210, 					new UpdatePeopleEvent() ),
	RLAST_GSID					( 220, 					new RLastGsidEvent() ),
	
	
	
	
	//-----------------测试
	TEST						( Short.MAX_VALUE-1, 	new TestEevet() );
	
	
	private final short			number;
	private final IEvent 		eventInstance;
	
	Events( int value, IEvent eventInstance ) {
		if( value >= Short.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "包号不符合规范：" + value );
		}
		this.number 		= (short) value;
		this.eventInstance 	= eventInstance;
		this.eventInstance.setEventId( number );
	}
	
	private static final Map<Short, Events> numToEnum = new HashMap<Short, Events>();
	
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
	public short toNum() {
		return number;
	}
	public static Events fromNum( int n ){
		return numToEnum.get( (short)n );
	}
	
	/**
	 * 运行此枚举所对应的包的run方法
	 * @param user
	 * @param buf
	 * @throws IOException 
	 */
	public void run( ChannelHandlerContext ctx, ByteBuf buf ) throws IOException {
		eventInstance.run( ctx, buf );
	}

}
