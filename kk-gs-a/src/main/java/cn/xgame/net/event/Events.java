package cn.xgame.net.event;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.all.TestEevet;
import cn.xgame.net.event.all.ls.ConnectEvent;
import cn.xgame.net.event.all.ls.RLastGsidEvent;
import cn.xgame.net.event.all.ls.UpdatePeopleEvent;
import cn.xgame.net.event.all.pl.CreateEvent;
import cn.xgame.net.event.all.pl.LoginEvent;
import cn.xgame.net.event.all.pl.planet.ApplyHomeEvent;
import cn.xgame.net.event.all.pl.planet.ApplyResEvent;
import cn.xgame.net.event.all.pl.planet.ApplySpeEvent;
import cn.xgame.net.event.all.pl.update.Update_2101;
import cn.xgame.net.event.all.pl.update.Update_2111;
import cn.xgame.net.event.all.pl.update.Update_2201;
import cn.xgame.net.event.all.pl.update.Update_2211;
import cn.xgame.net.event.all.pl.update.Update_2221;
import cn.xgame.net.event.all.pl.update.Update_2231;

/**
 * 通信 消息
 * @author deng		
 * @date 2015-6-11 下午3:34:41
 */
public enum Events {

	//-----------------登录服务器
	GS_CONNECT						( 201, 					new ConnectEvent() ),
	UPDATA_PEOPLE					( 210, 					new UpdatePeopleEvent() ),
	RLAST_GSID						( 220, 					new RLastGsidEvent() ),
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	//-----------------玩家
	PLAYER_LOGIN					( 1001, 				new LoginEvent() ),
	PLAYER_CREATE					( 1002, 				new CreateEvent() ),
	//-----------------星球
	APPLY_PLANET					( 1101, 				new ApplyHomeEvent() ),
	APPLY_PLANET_RES				( 1111, 				new ApplyResEvent() ),
	APPLY_PLANET_SPE				( 1112, 				new ApplySpeEvent() ),
	
	
	//-----------------更新包
	UPDATE_2101						( 2101, 				new Update_2101() ),
	UPDATE_2111						( 2111, 				new Update_2111() ),
	
	UPDATE_2201						( 2201, 				new Update_2201() ),
	UPDATE_2211						( 2211, 				new Update_2211() ),
	UPDATE_2221						( 2221, 				new Update_2221() ),
	UPDATE_2231						( 2231, 				new Update_2231() ),
	

	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	TEST							( Short.MAX_VALUE-1, 	new TestEevet() );
	
	
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
	public void run( Player player, ByteBuf buf ) throws IOException {
		eventInstance.run( player, buf );
	}

}
