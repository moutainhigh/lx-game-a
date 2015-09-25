package cn.xgame.a.world.planet.data.building;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.vote.Vote;

/**
 * 星球建筑 操作中心
 * @author deng		
 * @date 2015-6-25 下午5:01:19
 */
public class BuildingControl implements IArrayStream{

	public final int SNID;
	
	// 已建筑的建筑列表
	private volatile List<Buildings> buildings = Lists.newArrayList();
	
	// 投票建筑列表
	private volatile List<UnBuildings> voBuildings = Lists.newArrayList();
	
	// 建筑中列表
	private volatile List<UnBuildings> unBuildings = Lists.newArrayList();
	
	
	public BuildingControl(int id) {
		SNID = id;
	}

	public void fromTemplet( String content ) {
		if( content.isEmpty() ) return;
		String[] ls = content.split("\\|");
		for( int i = 0; i < ls.length; i++ ){
			if( ls[i].isEmpty() ) continue;
			String[] x 	= ls[i].split( ";" );
			Buildings o = new Buildings( Integer.parseInt( x[0] ) );
			o.setIndex( Byte.parseByte( x[1] ) );
			buildings.add(o);
		}
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		
		buildings.clear();
		voBuildings.clear();
		unBuildings.clear();
		
		ByteBuf buf = Unpooled.copiedBuffer(data);
		// 已建筑
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Buildings o = new Buildings( id );
			o.wrapBuffer(buf);
			buildings.add(o);
		}
		// 投票中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnBuildings o = new UnBuildings( id );
			o.wrapBuffer(buf);
			o.setVote( new Vote( buf ) );
			voBuildings.add(o);
		}
		// 建筑中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnBuildings o = new UnBuildings( id );
			o.wrapBuffer(buf);
			o.setEndtime( buf.readInt() );
			unBuildings.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		// 已建筑
		buf.writeByte( buildings.size() );
		for( Buildings o : buildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
		}
		// 投票中
		buf.writeByte( voBuildings.size() );
		for( UnBuildings o : voBuildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
			o.getVote().putBuffer( buf );
		}
		// 建筑中
		buf.writeByte( unBuildings.size() );
		for( UnBuildings o : unBuildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
			buf.writeInt( o.getEndtime() );
		}
		return buf.array();
	}


	/** 塞入 所有建筑 */
	public void putBuilding( ByteBuf buffer ) {
		buffer.writeByte( buildings.size() + unBuildings.size() );
		for( Buildings o : buildings ){
			o.buildTransformStream( buffer );
			buffer.writeInt( 0 );
		}
		for( UnBuildings o : unBuildings ){
			o.buildTransformStream( buffer );
			buffer.writeInt( o.getEndtime() );
		}
	}
	/** 塞入 投票建筑 
	 * @param player */
	public void putVoBuilding( Player player, ByteBuf buffer ) {
		buffer.writeByte( voBuildings.size() );
		for( UnBuildings o : voBuildings ){
			o.buildTransformStream( buffer );
			o.getVote().buildTransformStream(buffer);
			buffer.writeByte( o.getVote().isParticipateVote( player.getUID() ) );
		}
	}

	// 获取所有 已建筑+建筑中+投票中
	private List<Buildings> getAllBuilding() {
		List<Buildings> ret = Lists.newArrayList();
		ret.addAll(buildings);
		ret.addAll(unBuildings);
		ret.addAll(voBuildings);
		return ret;
	}

	/**
	 * 获取等待中的建筑中 列表
	 * @return
	 */
	public List<UnBuildings> getWaitBuild() {
		List<UnBuildings> ret = Lists.newArrayList();
		for( UnBuildings o : unBuildings ){
			if( o.getEndtime() == -1 )
				ret.add(o);
		}
		return ret;
	}

	
	/**
	 * 判断index位置是否被占用
	 * @param index
	 * @param room 
	 * @return
	 */
	public boolean isOccupyInIndex( byte index, byte room ) {
		List<Buildings> ls = getAllBuilding();
		for( Buildings o : ls ){
			if( o.indexIsOverlap( index, room ) )
				return true;
		}
		return false;
	}

	/**
	 * 添加一个 投票中建筑
	 * @param player 发起人
	 * @param nid 建筑表格ID
	 * @param index 建筑要建筑的位置
	 * @param time 发起时间限制
	 * @return 
	 */
	public UnBuildings appendVoteBuild( Player player, int nid, byte index, int time ) {
		UnBuildings unb = new UnBuildings(nid);
		unb.setIndex(index);
		unb.setVote( new Vote( player, time ) );
		voBuildings.add(unb);
		return unb;
	}

	/**
	 * 获取一个投票中的建筑
	 * @param nid
	 * @return
	 */
	public UnBuildings getVoteBuild( int nid ) {
		for( UnBuildings o : voBuildings ){
			if( o.templet().id == nid )
				return o;
		}
		return null;
	}

	/**
	 * 删除一个投票中的建筑
	 * @param nid
	 */
	public void removeVoteBuild( UnBuildings o ) {
		Iterator<UnBuildings> it = voBuildings.iterator();
		while( it.hasNext() ){
			UnBuildings next = it.next();
			if( next.templet().id == o.templet().id && next.getIndex() == o.getIndex() ){
				it.remove();
				break;
			}
		}
	}

	/**
	 * 是否可以建筑 该建筑
	 * @param nid
	 * @return
	 */
	public boolean isCanBuild( int nid ) {
		return getVoteBuild( nid ) == null;
	}
	
	
	/**
	 * 添加一个 建筑到建造列表
	 * @param unBuild
	 */
	public void appendUnBuild( UnBuildings unBuild ) {
		unBuildings.add(unBuild);
	}

	/**  建造 线程 */
	public UnBuildings runDevelopment() {
		Iterator<UnBuildings> iter = unBuildings.iterator();
		while( iter.hasNext() ){
			UnBuildings o = iter.next();
			if( o.isComplete() ){
				// 放入建筑列表
				Buildings build = new Buildings(o);
				buildings.add(build);
				// 然后才从列表中删除
				iter.remove();
				return o;
			}
		}
		return null;
	}
	
	/** 投票线程 */
	public UnBuildings runVote(  ) {
		Iterator<UnBuildings> iter = voBuildings.iterator();
		while( iter.hasNext() ){
			UnBuildings o = iter.next();
			// 这里时间完了 默认不同意 直接删除
			if( o.getVote() == null || o.getVote().isComplete() ){
				iter.remove();
				return o;
			}
		}
		return null;
	}

}
