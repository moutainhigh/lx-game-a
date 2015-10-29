package cn.xgame.a.player.fleet.info.purpose;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.utils.LuaUtil;

/**
 *  出航
 * @author deng		
 * @date 2015-9-14 下午12:34:01
 */
public class Setsail extends IPurpose{

	// 航线
	private List<Integer> airline = Lists.newArrayList();
	
	
	public Setsail( List<Integer> args ) {
		super((byte) 2);
		airline.addAll(args);
	}

	public Setsail() {
		super((byte) 2);
	}

	@Override
	public void putBuffer( ByteBuf buf ) {
		buf.writeByte( airline.size() );
		for( int o : airline ){
			buf.writeInt( o );
		}
	}
	
	@Override
	public void wrapBuffer( ByteBuf buf ) {
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			airline.add( buf.readInt() );
		}
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( type() );
		putBuffer( buffer );
	}

	public List<Integer> getAirline() {
		return airline;
	}
	
	/**
	 * 重置 航线
	 * @param airline
	 */
	public void resetAirline( List<Integer> airline ){
		this.airline.clear();
		if( airline != null )
			this.airline.addAll(airline);
	}

	/**
	 * 追加 航线
	 * @param airline
	 */
	public void appendAirline( List<Integer> airline ) {
		this.airline.addAll(airline);
	}
	
	@Override
	public void execut( int endtime, int targetId, FleetInfo fleet, Player player ) {
		int startId 	= targetId; // 起始星球
		int aimId 		= targetId; // 目标星球
		int sailtime 	= 0; // 航行时间
		
		int temp  	= endtime;// 结束时间
		int curtime = (int) (System.currentTimeMillis()/1000);
		
		while( temp <= curtime ){
			startId = aimId;
			if( airline.isEmpty() ){ aimId = 0; break; }
			aimId 	= airline.remove(0);
			
			sailtime = LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, startId, aimId )[0].getInt();
			temp	+= sailtime;
		}
		
		// 这里先直接设置当前船位置
		fleet.setBerthSnid( startId );
		
		// 表示所有航线都航行完了 设置悬停
		if( aimId == 0 ){
			fleet.changeStatus( StatusType.HOVER );
		
		// 否则 继续航行
		} else {
			fleet.changeStatus( StatusType.SAIL, aimId, temp-sailtime, temp, new Setsail( airline ) );
		}
	}
	
}
