package cn.xgame.a.player.tavern.o;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

/**
 * 一个酒馆数据
 * @author deng		
 * @date 2015-8-22 下午2:26:36
 */
public class TavernData implements IBufferStream,ITransformStream{

	// 星球ID
	private final int snid;
	
	// 记录结束时间
	private int endtime = -1;
	
	// 舰长列表
	private List<TavernCaptain> caps = Lists.newArrayList();

	public TavernData( int snid ) {
		this.snid = snid;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( endtime );
		buf.writeByte( caps.size() );
		for( TavernCaptain o : caps ){
			buf.writeInt( o.id );
			buf.writeByte( o.quality );
		}
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		endtime 		= buf.readInt();
		byte size 		= buf.readByte();
		for( int i = 0; i < size; i++ ){
			push( buf.readInt(), buf.readByte() );
		}
	}
	
	@Override
	public void buildTransformStream( ByteBuf response ) {
		int surplusTime = getSurplusTime();
		response.writeInt( surplusTime <= 0 ? 1 : surplusTime );
		response.writeByte( caps.size() );
		for( TavernCaptain o : caps ){
			response.writeInt( o.id );
			response.writeByte( o.quality );
		}
	}
	
	/**
	 * 获取一个酒馆舰长数据
	 * @param nid
	 * @return
	 */
	public TavernCaptain getCaptain(int nid) {
		for( TavernCaptain o : caps ){
			if( o.id == nid )
				return o;
		}
		return null;
	}

	/**
	 * 放入一个
	 * @param id
	 * @param quality
	 */
	public void push( int id, int quality ) {
		caps.add( new TavernCaptain( id, (byte) quality ) );
	}
	public void remove(int nid) {
		Iterator<TavernCaptain> iter = caps.iterator();
		while( iter.hasNext() ){
			if( iter.next().id == nid ){
				iter.remove();
				return;
			}
		}
	}
	
	/**
	 * 获取剩余时间
	 */
	public int getSurplusTime(){
		return endtime == -1 ? 0 : endtime - ((int) (System.currentTimeMillis()/1000));
	}
	
	/**
	 * 更新一下数据
	 */
	public void updateCaptain() {
		if( getSurplusTime() > 0 ) 
			return;
		caps.clear();
		endtime				= -1;
		Lua lua 			= LuaUtil.getGameData();
		LuaValue[] value 	= lua.getField( "updateTavern" ).call( 1, snid );
		String ret 			= value[0].getString();
		if( ret.isEmpty() ) return;
		
		try {
			IPlanet planet		= WorldManager.o.getPlanet(snid);
			endtime				= (int) (System.currentTimeMillis()/1000) + planet.getTavernUpdateTime();
			String[] content 	= ret.split("\\|");
			for( String str : content ){
				String[] v 		= str.split(";");
				push( Integer.parseInt( v[0] ), Integer.parseInt( v[1] ) );
			}
			
			Logs.debug( "星球"+snid+ " 更新酒馆 " + ret );
		} catch (Exception e) {
			Logs.error( "更新酒馆出错", e );
		}
	}
	
	public int getSnid() {
		return snid;
	}
	public int getEndtime() {
		return endtime;
	}
	
}

