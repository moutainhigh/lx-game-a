package cn.xgame.a.player.fleet.other;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 舰队状态基类
 * @author deng		
 * @date 2015-9-11 上午12:35:31
 */
public abstract class IStatus implements ITransformStream, IBufferStream{

	private final StatusType type;
	
	public IStatus( StatusType type ){
		this.type = type;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public StatusType type(){ return type; }
	

	public static IStatus create( byte _type, ByteBuf buf ) {
		StatusType type = StatusType.fromNumber( _type );
		if( type == null )
			throw new RuntimeException( "创建舰队状态出错 type=null!" );
		IStatus status = type.create();
		status.wrapBuffer(buf);
		return status;
	}

	/**
	 * 是否完成
	 */
	public abstract boolean isComplete() ;

	/**
	 * 执行状态
	 * @param fleetInfo 
	 * @param player
	 * @return
	 */
	public abstract IStatus execut( FleetInfo fleetInfo, Player player ) ;
}
