package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.ectype.o.ChapterEctype;
import cn.xgame.a.player.ectype.o.StarEctype;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 副本信息
 * @author deng		
 * @date 2015-7-17 上午3:23:52
 */
public class EctypeControl implements IArrayStream{

	private Player root;
	
	// 各个星球副本
	private List<StarEctype> sectypes 	= Lists.newArrayList();
	
	// 特殊限时副本次数记录
	private List<ChapterEctype> special = Lists.newArrayList();
	
	
	public EctypeControl( Player player ) {
		root = player;
	}
	
	/** 初始化副本信息 */
	public void initialize() {
		List<IPlanet> all = WorldManager.o.getAllPlanet();
		for( IPlanet planet : all ){
			StarEctype o = new StarEctype( planet.getId() );
			List<Integer> ls = planet.getItselfEctype();
			for( int id : ls ){
				ChapterEctype ectype = new ChapterEctype( planet.getId(), id );
				ectype.generateNextEctype();
				o.getGeneral().add(ectype);
			}
			o.getNormal().addAll( updateNormalEctype( planet.getId() ) );
			sectypes.add( o );
		}
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			StarEctype o = new StarEctype( buf.readInt() );
			o.wrapBuffer(buf);
			sectypes.add( o );
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( );
		buf.writeByte( sectypes.size() );
		for( StarEctype o : sectypes ){
			buf.writeInt( o.getSnid() );
			o.putBuffer(buf);
		}
		return buf.array();
	}
	
	public StarEctype getStarEctype(int snid) {
		for( StarEctype o : sectypes ){
			if( o.getSnid() == snid )
				return o;
		}
		return null;
	}
	
	
	/**
	 * 初始所有副本次数
	 */
	public void initAllTimes() {
		for( StarEctype stare : sectypes ){
			stare.initGeneralTimes();
			stare.getNormal().clear();
			stare.getNormal().addAll( updateNormalEctype( stare.getSnid() ) );
		}
	}
	
	/**
	 * 刷新普通限时副本
	 * @param snid
	 * @return
	 */
	private List<ChapterEctype> updateNormalEctype( int snid ) {
		List<ChapterEctype> ret = Lists.newArrayList();
		Lua lua = LuaUtil.getEctypeInfo();
		LuaValue[] value = lua.getField( "getStarRandomEctype" ).call( 1, snid );
		String content = value[0].getString();
		if( !content.isEmpty() ){
			String[] str = content.split("\\|");
			for( String x : str ){
				if( x.isEmpty() ) continue;
				String[] o = x.split( ";" );
				ChapterEctype ectype = new ChapterEctype( snid, Integer.parseInt( o[0] ) );
				ectype.generateNextEctype();
				ectype.setPersistTime( Integer.parseInt( o[1] ) );
				ectype.setRtime( (int) (System.currentTimeMillis()/1000) );
				ret.add(ectype);
			}
		}
		Logs.debug( root, "刷新普通限时副本 " + ret );
		return ret;
	}
	
	/**
	 * 根据星球获取对应星球的副本列表
	 * @param snid
	 * @return
	 */
	public List<StarEctype> getEctypeList( IPlanet planet ) {
		List<StarEctype> ret 	= Lists.newArrayList();
		List<Integer> scope 	= planet.getScopePlanet();
		for( StarEctype o : sectypes ){
			if( scope.indexOf( o.getSnid() ) != -1 || o.getSnid() == planet.getId() ){
				o.updateNormalEctype();
				ret.add(o);
			}
		}
		return ret;
	}
	
	/**
	 * 获取特殊限时副本
	 * @return
	 */
	public List<ChapterEctype> getSpecial() {
		return special;
	}
	
}
