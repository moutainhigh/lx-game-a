package cn.xgame.a.player.fleet.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.Backsail;
import cn.xgame.a.player.fleet.info.purpose.FightEctype;
import cn.xgame.a.player.fleet.info.purpose.Setsail;
import cn.xgame.a.player.u.Player;

/**
 * 航行目的
 * 
 * 
 * 1.打副本
 * 2.出航
 * 
 * 
 * @author deng		
 * @date 2015-9-14 下午12:22:04
 */
public abstract class IPurpose implements ITransformStream, IBufferStream{
	
	private byte type;
	
	public IPurpose(byte type) {
		this.type = type;
	}

	/**
	 * 类型 1.打副本  2.出航 3.返航
	 * @return
	 */
	public byte type(){ return type; }
	
	public static IPurpose create( byte type, ByteBuf buf ) {
		switch( type ){
			case 1:
			{
				FightEctype o = new FightEctype();
				o.wrapBuffer(buf);
				return o;
			}
			case 2:
			{
				Setsail o = new Setsail();
				o.wrapBuffer(buf);
				return o;
			}
			case 3:
			{
				Backsail o = new Backsail();
				o.wrapBuffer(buf);
				return o;
			}
		}
		return null;
	}

	/**
	 * 执行结果
	 * @param starttime 航行起始时间
	 * @param continutime 航行结束时间
	 * @param targetId 航行目的星球
	 * @param fleet 舰队信息
	 * @param player 玩家信息
	 * @return
	 */
	public abstract void execut( int starttime, int continutime, int targetId, FleetInfo fleet, Player player);
	
}
