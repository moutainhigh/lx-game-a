package cn.xgame.a.player.fleet.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.info.FleetInfo;
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
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type().toNumber() );
	}
	
	/**
	 * 状态
	 * @return
	 */
	public StatusType type(){ 
		return type; 
	}
	
	public static IStatus create( byte _type, ByteBuf buf ) {
		StatusType type = StatusType.fromNumber( _type );
		if( type == null )
			throw new RuntimeException( "创建舰队状态出错 type=null!" );
		IStatus status = type.create();
		status.wrapBuffer(buf);
		return status;
	}
	
	/**
	 * 初始化
	 * @param objects
	 */
	public abstract void init( Object[] objects );
	
	/**
	 * 是否完成
	 */
	public abstract boolean isComplete() ;

	/**
	 * 刷新状态
	 * @param fleetInfo 
	 * @param player
	 * @return
	 */
	public abstract void update( FleetInfo fleetInfo, Player player ) ;
	
}
