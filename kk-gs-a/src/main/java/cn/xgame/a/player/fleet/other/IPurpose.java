package cn.xgame.a.player.fleet.other;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.purpose.FightEctype;
import cn.xgame.a.player.fleet.purpose.Setsail;

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
	
}
