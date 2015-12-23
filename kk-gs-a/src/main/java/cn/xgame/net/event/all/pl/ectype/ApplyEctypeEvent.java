package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Time;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.fighter.o.Attackattr;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ectype.EctypeControl;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 申请某个星球的副本信息
 * @author deng		
 * @date 2015-7-20 上午10:12:10
 */
public class ApplyEctypeEvent extends IEvent{
	
	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		byte fId = data.readByte();//舰队ID
		
		// 如果没有舰船直接返回
		FleetInfo fleet 		= player.getFleets().getFleetInfo( fId );
		if( fleet.isEmpty() )
			return;
		
		try {
			EctypeControl control = player.getEctypes();
			control.updateChanceEctype();
			
			List<FleetInfo> allfleets = getAllFleets( player, fleet );
			// 获取玩家战斗力
			Fighter fighter = wrapFighter( allfleets );
			
			List<ChapterInfo> all = Lists.newArrayList();
			// 常规副本
			List<ChapterInfo> generals = control.getGeneralEctype(fleet.getBerthSnid());
			for( ChapterInfo o : generals ){
				o.type = 1;
				EctypeInfo x = o.getEctypes().get(0);
				Fighter defender = x.fighter(o.getQuestions());
				LuaValue[] value = LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 3, fleet.getBerthSnid(), o.getSnid(), fleet.toShipDatas(), x, fighter,defender );
				o.wrate = value[2].getByte();
				all.add(o);
			}
			
			// 偶发副本
			List<ChapterInfo> chances = control.getChanceEctype(fleet.getBerthSnid());
			for( ChapterInfo o : chances ){
				o.type = 2;
				EctypeInfo x = o.getEctypes().get(0);
				Fighter defender = x.fighter(o.getQuestions());
				LuaValue[] value = LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 3, fleet.getBerthSnid(), o.getSnid(), fleet.toShipDatas(), x, fighter,defender );
				o.wrate = value[2].getByte();
				all.add(o);
			}
			
			// 在这里排序
			sort( player, fleet, all );
			
			// 开始塞入数据
			ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
			buffer.writeShort( all.size() );
			for( ChapterInfo o : all ){
				buffer.writeByte( o.type );
				buffer.writeInt( o.getSnid() );
				buffer.writeInt( o.getId() );
				buffer.writeInt( o.type == 1 ? (int)(Time.refTimeInMillis(24, 0, 0)/1000) : o.getEndtime() );
				buffer.writeByte( o.getTimes() );
				buffer.writeByte( o.getQuestions().size() );
				for( int id : o.getQuestions() ){
					buffer.writeInt( id );
				}
				List<EctypeInfo> ectypes = o.getEctypes();
				byte size = (byte) (o.type == 1 ? control.getSGEL(o.getSnid()).getCl(o.getId()).curIndex : ectypes.size());
				buffer.writeByte( size );
				for( int i = 0; i < size; i++ ){
					putEctypeInfo(fleet, buffer, fighter, o, ectypes.get(i));
				}
			}
			
			sendPackage( player.getCtx(), buffer );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 排序
	 * @param player
	 * @param all
	 */
	private void sort( final Player player, final FleetInfo fleet, final List<ChapterInfo> all ){
		final TaskControl tasks = player.getTasks();
	
		final Comparator<ChapterInfo> comparator = new Comparator<ChapterInfo>() {
			@Override
			public int compare(ChapterInfo o1, ChapterInfo o2) {
				// 1.任务优先 
				if( tasks.isHave(o1.getId()) )
					return -1;
				if( tasks.isHave(o2.getId()) )
					return 1;
				// 2.舰队所在星球副本优先
				if( o1.getSnid() == fleet.getBerthSnid() )
					return -1;
				if( o2.getSnid() == fleet.getBerthSnid() )
					return 1;
				// 3.新偶发出来优先
				if( o1.type == 2 && player.getNewChances().indexOf(o1.getId()) == -1 ){
					player.getNewChances().add(o1.getId());
					return -1;
				}
				if( o2.type == 2 && player.getNewChances().indexOf(o2.getId()) == -1 ){
					player.getNewChances().add(o2.getId());
					return 1;
				}
				// 4.成功率最大优先
				return o2.wrate - o1.wrate;
			}
		};
		Collections.sort( all, comparator );
	}
	
	private void putEctypeInfo(FleetInfo fleet, ByteBuf buffer, Fighter fighter, ChapterInfo o, EctypeInfo x) {
		Fighter defender = x.fighter(o.getQuestions());
		LuaValue[] value = LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 3, fleet.getBerthSnid(), o.getSnid(), fleet.toShipDatas(), x, fighter,defender );

		buffer.writeByte( x.getLevel() );
		buffer.writeInt( value[0].getInt() );
		buffer.writeInt( value[1].getInt() );
		buffer.writeByte( value[2].getByte() );
		buffer.writeByte( x.getAtks().size() );
		for( Attackattr attr : x.getAtks() )
			attr.buildTransformStream(buffer);
		buffer.writeByte( x.getDefs().size() );
		for( Attackattr attr : x.getDefs() )
			attr.buildTransformStream(buffer);
	}
	
	/**
	 * 获取要到达目的星球 玩家中最远的星球
	 * @param allfleets
	 * @param snid
	 * @return
	 */
//	private int getFarthestSid( List<FleetInfo> allfleets, int snid ) {
//		int ret = snid;
//		int temp = 0;
//		for( FleetInfo fleet : allfleets ){
//			int stime = LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), snid, fleet.toShipDatas() )[0].getInt();
//			if( stime > temp ){
//				temp 	= stime;
//				ret 	= fleet.getBerthSnid();
//			}
//		}
//		return ret;
//	}

	/**
	 * 包装战斗者
	 * @param allfleets
	 * @return
	 */
	private Fighter wrapFighter( List<FleetInfo> allfleets ) {
		Fighter ret = new Fighter();
		for( FleetInfo fleet : allfleets ){
			Fighter temp = fleet.fighter();
			ret.hp += temp.hp;
			ret.addAtkattr( temp.attacks );
			ret.addDefattr( temp.defends );
		}
		return ret;
	}
	
	/**
	 * 获取玩家包括组队的全部信息
	 * @param player
	 * @param fleet
	 * @return
	 */
	private List<FleetInfo> getAllFleets( Player player, FleetInfo fleet ) {
		List<FleetInfo> ret = Lists.newArrayList();
		// 如果有组队
		if( fleet.getAxnId() != -1 ){
			AxnInfo axn = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				TeamAxnCrew x 	= (TeamAxnCrew) crew;
				Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
				FleetInfo f 	= o.getFleets().getFleetInfo( x.getFid() );
				ret.add( f );
			}
		}
		ret.add( fleet );
		return ret;
	}
}
