package cn.xgame.a.player.fleet.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.info.result.CombatIn;
import cn.xgame.a.player.fleet.info.result.Settlement;

/**
 * 战斗 结果基类
 * @author deng		
 * @date 2015-11-3 下午5:58:34
 */
public abstract class IResult implements ITransformStream, IBufferStream{

	private byte type;
	
	public IResult(byte type) {
		this.type = type;
	}

	/**
	 * 类型 1.战斗中  2.结算中
	 * @return
	 */
	public byte type(){ return type; }

	public static IResult create( byte type, ByteBuf buf ) {
		switch( type ){
			case 1:
			{
				CombatIn o = new CombatIn();
				o.wrapBuffer(buf);
				return o;
			}
			case 2:
			{
				Settlement o = new Settlement();
				o.wrapBuffer(buf);
				return o;
			}
		}
		return null;
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte(type);
	}

	
	public abstract boolean isComplete() ;
	

}
