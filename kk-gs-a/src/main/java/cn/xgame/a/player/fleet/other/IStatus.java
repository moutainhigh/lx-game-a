package cn.xgame.a.player.fleet.other;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;

/**
 * 舰队状态基类
 * @author deng		
 * @date 2015-9-11 上午12:35:31
 */
public abstract class IStatus implements ITransformStream{

	private final StatusType type;
	
	public IStatus( StatusType type ){
		this.type = type;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public StatusType type(){ return type; }
	

	public static IStatus create( ByteBuf buf ) {
		StatusType type = StatusType.fromNumber( buf.readByte() );
		return type.create( buf );
	}
	
	public void putBuffer(ByteBuf buffer) {
		buffer.writeByte( type.toNumber() );
	}
	
	
}
