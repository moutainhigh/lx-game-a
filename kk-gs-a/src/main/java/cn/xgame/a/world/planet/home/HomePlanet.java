package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;


import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_2211;
import cn.xgame.net.netty.Netty.RW;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet extends IPlanet {

	public HomePlanet(Stars clone) {
		super(clone);
	}


	// 玩家列表
	private List<Child> childs = Lists.newArrayList();
	
	// 科技列表
	private TechControl techs = new TechControl();

	@Override
	public void wrap( PlanetDataDto dto ) {
		super.wrap(dto);
		wrapPlayer( dto.getPlayers() );
		techs.fromBytes( dto.getTechs() );
	}

	private void wrapPlayer( byte[] data ) {
		if( data == null ) return ;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		short size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			Child child = new Child( RW.readString(buf), buf.readShort() );
			childs.add( child );
		}
	}
	
	private byte[] toPlayers(){
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeShort( childs.size() );
		for( Child child : childs ){
			RW.writeString( buf, child.UID );
			buf.writeShort( child.gsid );
		}
		return buf.array();
	}
	

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		super.buildTransformStream(buffer);
		techs.buildTransformStream(buffer);
	}

	@Override
	public void updateDB() {
		PlanetDataDao dao = SqlUtil.getPlanetDataDao();
		PlanetDataDto dto = dao.update();
		dto.setId( getId() );
		dto.setMaxSpace( getMaxSpace() );
		dto.setBuildings( getBuildings().toBytes() );
		dto.setDepots( getDepots().toBytes() );
		dto.setSpecialtys( getSpecialtys().toBytes() );
		dto.setTechs( techs.toBytes() );
		dto.setPlayers( toPlayers() );
		dao.commit(dto);
	}

	/**
	 * 添加一个 玩家
	 * @param player
	 */
	public void add( Player player ) {
		Child child = getChild( player.getUID() );
		if( child == null )
			addChild( player );
	}
	
	private void addChild( Player player ) {
		Child child = new Child( player.getUID(), player.getGsid() );
		childs.add( child );
	}
	private Child getChild(String uid) {
		for( Child child : childs ){
			if( child.UID.equals( uid ) )
				return child;
		}
		return null;
	}
	
	/** 线程 */
	public void run() {
		// 这里处理 特产
		List<Specialty> ls = getSpecialtys().getSpecialtys();
		for( Specialty spe : ls ){
			if( spe.run() )
				synchronizeSpecialty( spe );
		}
	}

	// 同步特产信息
	private void synchronizeSpecialty( Specialty spe ) {
		
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.UID );
			if( player == null || !player.isOnline() )
				continue;
			
			((Update_2211)Events.UPDATE_2211.getEventInstance()).run( player, spe );
		}
	}
	
}
