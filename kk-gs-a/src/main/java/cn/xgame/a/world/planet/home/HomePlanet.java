package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;


import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Building;
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

	// 体制
	private Institution institution;
	
	// 玩家列表
	private List<Child> childs = Lists.newArrayList();
	
	// 科技列表
	private TechControl techs = new TechControl();

	public Institution getInstitution() { return institution; }
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	@Override
	public void init( PlanetDataDto dto ) {
		super.init(dto);
		setInstitution(Institution.REPUBLIC);
	}
	
	@Override
	public boolean isCanDonate() { return true; }
	
	@Override
	public void wrap( PlanetDataDto dto ) {
		super.wrap(dto);
		wrapPlayer( dto.getPlayers() );
		techs.fromBytes( dto.getTechs() );
		// 先排个序
		updateChildSequence();
		// 从数据库 获取数据后 更新体制
		updateInstitution();
		// 对玩家进行排序 和标记是否可以投票
		updateAllContribution();
	}

	private void wrapPlayer( byte[] data ) {
		if( data == null ) return ;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		short size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			Child child = new Child( RW.readString(buf), buf.readShort() );
			child.wrapBuffer(buf);
			childs.add( child );
		}
	}
	
	private byte[] toPlayers(){
		if( childs.isEmpty() )
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeShort( childs.size() );
		for( Child child : childs ){
			RW.writeString( buf, child.getUID() );
			buf.writeShort( child.getGsid() );
			child.putBuffer(buf);
		}
		return buf.array();
	}
	

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		// 基础数据
		super.buildTransformStream(buffer);
		// 所有建筑数据
		getBuildingControl().putBuilding(buffer);
		// 所有科技数据
		techs.putTechs(buffer);
		// 建筑中
		getBuildingControl().putUnBuilding(buffer);
		// 研究中
		techs.putUnTech(buffer);
	}

	@Override
	public void updateDB() {
		PlanetDataDao dao = SqlUtil.getPlanetDataDao();
		PlanetDataDto dto = dao.update();
		dto.setId( getId() );
		dto.setMaxSpace( getMaxSpace() );
		dto.setBuildings( getBuildingControl().toBytes() );
		dto.setDepots( getDepotControl().toBytes() );
		dto.setSpecialtys( getSpecialtyControl().toBytes() );
		dto.setTechs( techs.toBytes() );
		dto.setPlayers( toPlayers() );
		dao.commit(dto);
	}

	/**
	 * 添加一个 玩家
	 * @param player
	 */
	public void appendPlayer( Player player ) {
		Child child = getChild( player.getUID() );
		if( child == null )
			addChild( player );
	}
	
	private void addChild( Player player ) {
		Child child = new Child( player.getUID(), player.getGsid() );
		childs.add( child );
	}
	public Child getChild(String uid) {
		for( Child child : childs ){
			if( child.getUID().equals( uid ) )
				return child;
		}
		return null;
	}
	
	/** 线程 */
	public void run() {
		// 这里处理 特产
		List<Specialty> ls = getSpecialtyControl().getSpecialtys();
		for( Specialty spe : ls ){
			if( spe.run() )
				synchronizeSpecialty( spe );
		}
	}
	
	// 同步特产信息
	private void synchronizeSpecialty( Specialty spe ) {
		
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() )
				continue;
			
			((Update_2211)Events.UPDATE_2211.getEventInstance()).run( player, spe );
		}
	}

	/** 更新体制 */
	public void updateInstitution() {
		setInstitution(Institution.REPUBLIC);
	}
	
	@Override
	public void donateResource( Player player, IProp prop ) {
		
		// 添加资源
		getDepotControl().appendProp(prop);
		
		// 添加玩家的贡献度
		Child child = getChild( player.getUID() );
		child.addContribution( prop.getContributions() );
		// 先排个序
		updateChildSequence();
		// 下面算出话语权 顺便对玩家进行排序
		int allcont = updateAllContribution();
		short privilege = (short) (((float)child.getContribution() / (float)allcont) * 10000f);
		child.setPrivilege( privilege );
	}

	/**
	 * 获取该星球的 所有贡献值总和 - 对玩家进行标记是否可以发起投票
	 * @return
	 */
	public int updateAllContribution() {
		// 下面标记是否可以发起投票
		int ret = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			child.setCanSponsorVote( true );
			ret += child.getContribution();
		}
		return ret;
	}
	
	// 根据玩家的贡献度 排序
	private void updateChildSequence() {
		Collections.sort( childs, comparator );
	}
	/** 根据贡献度 从大到小 **/
	private static final Comparator<Child> comparator = new Comparator<Child>() {
		@Override
		public int compare(Child o1, Child o2) {
			return o2.getContribution() - o1.getContribution();
		}
	};
	
	@Override
	public void sponsorBuivote( Player player, int nid, byte index, int time ) throws Exception { 
		
		// 判断是否有权限发起投票
		Child child = getChild( player.getUID() );
		if( child == null || !child.isCanSponsorVote() )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 判断位置是否占用
		Building templet = CsvGen.getBuilding(nid);
		if( buildingControl.isOccupyInIndex( index, templet.room ) )
			throw new Exception( ErrorCode.INDEX_OCCUPY.name() );
		// 添加到投票中
		buildingControl.appendVoteBuild( nid, index, time );
		// 记录玩家发起数
		child.addSponsors( 1 );
	}
	
	@Override
	public void participateBuildVote( Player player, int nid, byte isAgree ) throws Exception { 
		
		// 判断是否有权限投票
		Child child = getChild( player.getUID() );
		if( child == null || child.getPrivilege() == 0 )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 设置投票
		UnBuildings unBuild = buildingControl.getVoteBuild( nid );
		unBuild.getVote().setIsAgrees( isAgree );
		// 下面判断 票是否投完
		// TODO
		
	}
	
	@Override
	public void participateTechVote(Player player, int nid, byte isAgree) throws Exception { }
	@Override
	public void participateGenrVote(Player player, String uid,byte isAgree) throws Exception { }


}
