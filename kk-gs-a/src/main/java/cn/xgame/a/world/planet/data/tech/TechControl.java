package cn.xgame.a.world.planet.data.tech;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.vote.Vote;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TechPo;

/**
 * 科技 操作中心
 * @author deng		
 * @date 2015-6-26 下午1:32:00
 */
public class TechControl implements IArrayStream{

	public final int SNID;
	
	// 已研究的科技列表
	private volatile List<Techs> techs = Lists.newArrayList();
	
	// 投票中的科技列表
	private volatile List<UnTechs> voTechs = Lists.newArrayList();
	
	// 研究中的科技列表
	private volatile List<UnTechs> unTechs = Lists.newArrayList();
	
	public TechControl(int id) {
		SNID = id;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		techs.clear();
		voTechs.clear();
		unTechs.clear();
		
		ByteBuf buf = Unpooled.copiedBuffer(data);
		// 已研究
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Techs o = new Techs( id );
			techs.add(o);
		}
		// 投票中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnTechs o = new UnTechs( id );
			o.setVote( new Vote( buf ) );
			voTechs.add(o);
		}
		// 研究中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnTechs o = new UnTechs( id );
			o.setEndtime( buf.readInt() );
			unTechs.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		// 已研究
		buf.writeByte( techs.size() );
		for( Techs o : techs ){
			buf.writeInt( o.templet().id );
		}
		// 投票中
		buf.writeByte( voTechs.size() );
		for( UnTechs o : voTechs ){
			buf.writeInt( o.templet().id );
			o.getVote().putBuffer( buf );
		}
		// 研究中
		buf.writeByte( unTechs.size() );
		for( UnTechs o : unTechs ){
			buf.writeInt( o.templet().id );
			buf.writeInt( o.getEndtime() );
		}
		return buf.array();
	}

	public void putTechs(ByteBuf buffer) {
		buffer.writeByte( techs.size() );
		for( Techs o : techs ){
			o.buildTransformStream(buffer);
		}
	}

	public void putUnTech(ByteBuf buffer) {
		buffer.writeByte( unTechs.size() );
		for( UnTechs o : unTechs ){
			o.buildTransformStream(buffer);
			buffer.writeInt( o.getEndtime() );
		}
	}
	
	public void putVoTech( Player player, ByteBuf buffer){
		buffer.writeByte( voTechs.size() );
		for( UnTechs o : voTechs ){
			o.buildTransformStream(buffer);
			o.getVote().buildTransformStream(buffer);
			buffer.writeByte( o.getVote().isParticipateVote( player.getUID() ) );
		}
	}

	/**
	 * 获取等待 研究 的科技
	 * @return
	 */
	public List<UnTechs> getWaitTech() {
		List<UnTechs> ret = Lists.newArrayList();
		for( UnTechs o : unTechs ){
			if( o.getEndtime() == -1 )
				ret.add(o);
		}
		return ret;
	}
	
	/** 获取最大科技等级 */
	public byte getMaxTechLevel() {
		byte ret = 0;
		for( Techs o : techs ){
			if( o.templet().level > ret )
				ret = o.templet().level;
		}
		return ret;
	}
	
	/**
	 * 获取科技 根据表格ID
	 * @param nid
	 * @return
	 */
	public Techs getTech( int nid ) {
		for( Techs o : techs ){
			if( o.templet().id == nid )
				return o;
		}
		return null;
	}
	/**
	 * 获取研究中科技 根据表格ID
	 * @param nid
	 * @return
	 */
	public Techs getUnTech( int nid ) {
		for( UnTechs o : unTechs ){
			if( o.templet().id == nid )
				return o;
		}
		return null;
	}
	/**
	 * 获取投票中科技 根据表格ID
	 * @param nid
	 * @return
	 */
	public UnTechs getVoTech( int nid ) {
		for( UnTechs o : voTechs ){
			if( o.templet().id == nid )
				return o;
		}
		return null;
	}
	
	/**
	 * 删除投票中的科技
	 * @param unTech
	 */
	public void removeVoTech(UnTechs o) {
		Iterator<UnTechs> it = voTechs.iterator();
		while( it.hasNext() ){
			UnTechs next = it.next();
			if( next.templet().id == o.templet().id ){
				it.remove();
				break;
			}
		}
	}
	
	/**
	 * 判断是否能研究
	 * @param nid
	 * @param techLevel 星球的最高科技等级
	 * @return
	 */
	public boolean isCanStudy( int nid, byte techLevel ) {
		TechPo t = CsvGen.getTechPo(nid);
		if( t == null ) return false;
		return getVoTech( nid ) == null // 正在参与投票的
				&& getTech( nid ) == null// 已经研究好的
				&& getUnTech( nid ) == null// 正在研究中的
				&& isHavePre( t.prevtech )  // 是否需要前置科技
				&& !isHaveMutex( t.Mutualtech ) // 是否有互斥科技
				&& t.needlevel <= techLevel; // 需要等级是否满足
	}

	/**
	 * 是否有互斥
	 * @param mutex
	 * @return
	 */
	private boolean isHaveMutex( String mutex ) {
		if( mutex == null || mutex.isEmpty() )
			return false;
		String[] ls = mutex.split(";");
		for( String x : ls ){
			if( x.isEmpty() )
				continue;
			Techs tech = getTech( Integer.parseInt(x) );
			if( tech != null )
				return true;
		}
		return false;
	}

	/**
	 * 是否有前置科技
	 * @param pre
	 * @return
	 */
	private boolean isHavePre( String pre ) {
		if( pre == null || pre.isEmpty() )
			return true;
		String[] ls = pre.split(";");
		for( String x : ls ){
			if( x.isEmpty() ) 
				continue;
			Techs tech = getTech( Integer.parseInt(x) );
			if( tech == null )
				return false;
		}
		return true;
	}

	public UnTechs appendVote(Player player, int nid, int time) {
		UnTechs unt = new UnTechs(nid);
		unt.setVote( new Vote( player, time ) );
		voTechs.add(unt);
		return unt;
	}
	
	
	public void appendUnTech(UnTechs unTech) {
		unTechs.add(unTech);
	}

	/**
	 * 研究中 线程
	 */
	public UnTechs runDevelopment() {
		Iterator<UnTechs> iter = unTechs.iterator();
		while( iter.hasNext() ){
			UnTechs o = iter.next();
			if( o.isComplete() ){
				// 放入已研究列表
				Techs tech = new Techs(o);
				techs.add(tech);
				// 然后才从列表中删除
				iter.remove();
				return o;
			}
		}
		return null;
	}

	/**
	 * 投票中 线程
	 * @return
	 */
	public UnTechs runVote() {
		Iterator<UnTechs> iter = voTechs.iterator();
		while( iter.hasNext() ){
			UnTechs o = iter.next();
			// 时间到了默认为不同意 直接从列表中删除
			if( o.getVote() == null || o.getVote().isComplete() ){
				iter.remove();
				return o;
			}
		}
		return null;
	}









}
