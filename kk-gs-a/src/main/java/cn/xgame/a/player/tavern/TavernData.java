package cn.xgame.a.player.tavern;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.system.LXConstants;
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
	private int snid;
	
	// 记录时间
	private int rtime = -1;
	// 更新时间 
	private int intervalTime = -1;
	
	// 舰长列表
	private List<TavernCaptain> caps = Lists.newArrayList();

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( snid );
		buf.writeInt( intervalTime );
		buf.writeInt( rtime );
		buf.writeByte( caps.size() );
		for( TavernCaptain o : caps ){
			buf.writeInt( o.id );
			buf.writeByte( o.quality );
		}
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		snid 			= buf.readInt();
		intervalTime 	= buf.readInt();
		rtime 			= buf.readInt();
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
		return intervalTime - (int) (System.currentTimeMillis()/1000 - rtime);
	}
	
	/**
	 * 更新一下数据
	 */
	public void updateCaptain() {
		caps.clear();
		rtime				= -1;
		intervalTime		= -1;
		Lua lua 			= LuaUtil.getGameData();
		LuaValue[] value 	= lua.getField( "updateTavern" ).call( 1, snid );
		String ret 			= value[0].getString();
		if( ret.isEmpty() ) return;
		
		Logs.debug( "更新酒馆  星球"+snid+ " " + ret );
		
		rtime				= (int) (System.currentTimeMillis()/1000);
		intervalTime		= LXConstants.TAVERN_UPDATE_TIME;// 这里到时候根据星球普惠改变
		String[] content 	= ret.split("\\|");
		for( String str : content ){
			if( str.isEmpty() ) continue;
			String[] v = str.split(",");
			push( Integer.parseInt( v[0] ), Integer.parseInt( v[1] ) );
		}
	}
	
	/**
	 * 是否需要更新
	 * @return
	 */
	public boolean isUpdate() {
		return getSurplusTime() <= 0;
	}
	
	public int getSnid() {
		return snid;
	}
	public void setSnid(int snid) {
		this.snid = snid;
	}
	public int getRtime() {
		return rtime;
	}
	public void setRtime(int rtime) {
		this.rtime = rtime;
	}
	public int getIntervalTime() {
		return intervalTime;
	}
	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}


	
	
}

