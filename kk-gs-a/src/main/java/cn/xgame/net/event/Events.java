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
import cn.xgame.net.event.all.pl.ApplyBagEvent;
import cn.xgame.net.event.all.pl.ApplyCaptainEvent;
import cn.xgame.net.event.all.pl.ApplyDockEvent;
import cn.xgame.net.event.all.pl.ApplyStaratlasEvent;
import cn.xgame.net.event.all.pl.CreateEvent;
import cn.xgame.net.event.all.pl.LoginEvent;
import cn.xgame.net.event.all.pl.ectype.ApplyAccEctypeEvent;
import cn.xgame.net.event.all.pl.ectype.StartAttackEvent;
import cn.xgame.net.event.all.pl.planet.ApplyAlllAffairEvent;
import cn.xgame.net.event.all.pl.planet.ApplyGenrsEvent;
import cn.xgame.net.event.all.pl.planet.ApplyHomeEvent;
import cn.xgame.net.event.all.pl.planet.ApplyResEvent;
import cn.xgame.net.event.all.pl.planet.ApplyShopEvent;
import cn.xgame.net.event.all.pl.planet.ApplyTavernEvent;
import cn.xgame.net.event.all.pl.planet.DonateStuffEvent;
import cn.xgame.net.event.all.pl.planet.ParticipateBuildVoEvent;
import cn.xgame.net.event.all.pl.planet.ParticipateExpelEvent;
import cn.xgame.net.event.all.pl.planet.ParticipateTechVoEvent;
import cn.xgame.net.event.all.pl.planet.SponsorBuildVoEvent;
import cn.xgame.net.event.all.pl.planet.SponsorExpelEvent;
import cn.xgame.net.event.all.pl.planet.SponsorTechVoEvent;
import cn.xgame.net.event.all.pl.transaction.ShopBuyEvent;
import cn.xgame.net.event.all.pl.transaction.TavernBuyEvent;
import cn.xgame.net.event.all.pl.update.Update_2101;
import cn.xgame.net.event.all.pl.update.Update_2111;
import cn.xgame.net.event.all.pl.update.Update_2201;
import cn.xgame.net.event.all.pl.update.Update_2211;
import cn.xgame.net.event.all.pl.update.Update_2221;
import cn.xgame.net.event.all.pl.update.Update_2231;
import cn.xgame.net.event.all.pl.update.Update_2241;
import cn.xgame.net.event.all.pl.update.Update_2252;

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
	APPLY_BAGS						( 1011, 				new ApplyBagEvent() ),
	APPLY_DOCKS						( 1012, 				new ApplyDockEvent() ),
	APPLY_CAPTAINS					( 1013, 				new ApplyCaptainEvent() ),
	APPLY_STARATLAS					( 1014, 				new ApplyStaratlasEvent() ),
	
	//-----------------星球
	APPLY_PLANET					( 1101, 				new ApplyHomeEvent() ),
	APPLY_PLANET_RES				( 1111, 				new ApplyResEvent() ),
	APPLY_PLANET_SPE				( 1112, 				new ApplyShopEvent() ),
	APPLY_ALLLAFFAIR				( 1113, 				new ApplyAlllAffairEvent() ),
	APPLY_GENRS						( 1114, 				new ApplyGenrsEvent() ),
	APPLY_TAVERN					( 1115, 				new ApplyTavernEvent() ),
	DONATE_STUFF					( 1121, 				new DonateStuffEvent() ),
	
	// 建筑
	SPONSOR_BUILDVOTE				( 1131, 				new SponsorBuildVoEvent() ),
	PARTICIPATE_BUILDVOTE			( 1132, 				new ParticipateBuildVoEvent() ),
	// 科技
	SPONSOR_TECHVOTE				( 1141, 				new SponsorTechVoEvent() ),
	PARTICIPATE_TECHVOTE			( 1142, 				new ParticipateTechVoEvent() ),
	// 元老
	SPONSOR_EXPEL					( 1151, 				new SponsorExpelEvent() ),
	PARTICIPATE_EXPEL				( 1152, 				new ParticipateExpelEvent() ),
	//-----------------副本
	APPLY_ACCECTYPE					( 1201, 				new ApplyAccEctypeEvent() ),
	START_ATTACK					( 1202, 				new StartAttackEvent() ),
	//-----------------交易
	SHOP_BUY						( 1301, 				new ShopBuyEvent() ),
	TAVERN_BUY						( 1311, 				new TavernBuyEvent() ),
	
	
	//-----------------更新包
	UPDATE_2101						( 2101, 				new Update_2101() ),
	UPDATE_2111						( 2111, 				new Update_2111() ),
	
	UPDATE_2201						( 2201, 				new Update_2201() ),
	UPDATE_2211						( 2211, 				new Update_2211() ),
	UPDATE_2221						( 2221, 				new Update_2221() ),
	UPDATE_2231						( 2231, 				new Update_2231() ),
	UPDATE_2241						( 2241, 				new Update_2241() ),
	UPDATE_2252						( 2252, 				new Update_2252() ),
	

	
	
	
	
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
