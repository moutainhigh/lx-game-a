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
//		putBuffer( buffer );
	}

	public List<Integer> getAirline() {
		return airline;
	}
	
	/**
	 * 重置 航线
	 * @param args
	 */
	public void resetAirline( List<Integer> args ){
		airline.clear();
		airline.addAll(args);
	}

	@Override
	public void execut( int endtime, int berthSnid, FleetInfo fleet, Player player ) {
		int berId = berthSnid;
		int aimId = 0;
		int temp  = endtime;
		int curtime = (int) (System.currentTimeMillis()/1000);
		do {
			if( aimId != 0 ) berId = aimId;

			if( airline.isEmpty() ){
				aimId = 0;
				break;
			}
			
			// 取出航线第一个目标星球
			aimId = airline.remove(0);
			// 算出航行时间
			temp += LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), aimId )[0].getInt();
		} while ( temp <= curtime );
		
		// 这里先直接设置当前船位置
		fleet.setBerthSnid( berId );
		
		// 表示所有航线都航行完了 设置悬停
		if( aimId == 0 ){
			fleet.setStatus( StatusType.HOVER.create() );
			
		// 否则 继续航行
		} else {
			fleet.changeSail( aimId, temp-curtime, new Setsail( airline ) );
		}
	}

	
}
