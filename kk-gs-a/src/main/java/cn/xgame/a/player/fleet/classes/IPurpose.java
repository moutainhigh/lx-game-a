package cn.xgame.a.player.fleet.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.info.FleetInfo;
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
	 * 类型 1.打副本  2.出航
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
		}
		return null;
	}

	/**
	 * 执行结果
	 * @param endtime 
	 * @param berthSnid 
	 * @param fleet
	 * @param player
	 * @return
	 */
	public abstract void execut(int endtime, int berthSnid, FleetInfo fleet, Player player);
	
}
