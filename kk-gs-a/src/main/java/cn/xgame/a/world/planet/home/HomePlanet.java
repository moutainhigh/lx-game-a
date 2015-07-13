package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.data.vote.VotePlayer;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Institution;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Sbuilding;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_2252;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet extends IPlanet {

	public HomePlanet(Stars clone) {
		super(clone);
	}

	//////////////////////////////临时数据
	// 体制
	private Institution institution;
	
	// 科技等级
	private byte techLevel = 0;
	
	//////////////////////////////数据库相关
	// 玩家列表
	private List<Child> childs = Lists.newArrayList();
	// 被驱逐投票中的元老列表
	private List<OustChild> oustChilds = Lists.newArrayList();
	
	// 科技列表
	private TechControl techControl = new TechControl();

	
	
	
	
	public Institution getInstitution() { return institution; }
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	public byte getTechLevel() {
		return techLevel;
	}
	public void setTechLevel(byte techLevel) {
		this.techLevel = techLevel;
	}
	@Override
	public boolean isCanDonate() { return true; }
	
	@Override
	public void init( PlanetDataDto dto ) {
		super.init(dto);
		setInstitution(Institution.REPUBLIC);
	}
	
	@Override
	public List<Child> getAllGenrs() { 
		List<Child> ret = Lists.newArrayList();
		for( Child c : childs ){
			if( c.isSenator() )
				ret.add(c);
		}
		return ret; 
	}
	
	@Override
	public void wrap( PlanetDataDto dto ) {
		super.wrap(dto);
		wrapPlayer( dto.getPlayers() );
		wrapOustPlayer( dto.getExpelGenr() );
		techControl.fromBytes( dto.getTechs() );
		// 根据科技获取最大星球科技等级
		updateTechLevel();
		// 先排个序
		updateChildSequence();
		// 从数据库 获取数据后 更新体制
		updateInstitution();
		// 对玩家进行排序 和标记是否可以投票
		updateAllContribution();
	}

	private void wrapPlayer( byte[] data ) {
		if( data == null ) return ;
		childs.clear();
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
	
	private void wrapOustPlayer( byte[] data ) {
		if( data == null ) return ;
		oustChilds.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			OustChild o = new OustChild( null, null, null );
			o.wrapBuffer(buf);
			oustChilds.add(o);
		}
	}
	private byte[] toOustPlayers(){
		if( oustChilds.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( oustChilds.size() );
		for( OustChild o : oustChilds ){
			o.putBuffer(buf);
		}
		return buf.array();
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		// 基础数据
		super.buildTransformStream(buffer);
		// 已建筑数据 and 建筑中数据
		getBuildingControl().putBuilding(buffer);
		// 所有科技数据
		techControl.putTechs(buffer);
		// 研究中
		techControl.putUnTech(buffer);
	}
	
	@Override
	public void putAlllAffair( ByteBuf response ) { 
		// 投票中建筑
		getBuildingControl().putVoBuilding(response);
		// 投票中科技
		techControl.putVoTech(response);
		// 投票中驱逐元老
		response.writeByte( oustChilds.size() );
		for( OustChild o : oustChilds ){
			Child child = getChild( o.getUid() );
			child.buildTransformStream(response);
			RW.writeString( response, o.getExplain() );
			o.getVote().buildTransformStream(response);
		}
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
		dto.setTechs( techControl.toBytes() );
		dto.setPlayers( toPlayers() );
		dto.setExpelGenr( toOustPlayers() );
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
		child.setName( player.getNickname() );
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
				Syn.specialty( childs, spe );
		}
	}
	
	/** 更新体制 */
	public void updateInstitution() {
		setInstitution(Institution.REPUBLIC);
	}
	
	@Override
	public void donateResource( Player player, IProp prop ) {
		
		// 添加资源
		List<IProp> update = getDepotControl().appendProp(prop);

		// 这里检查  建筑中列表 里面是否有材料不足而暂停的建筑中建筑
		List<UnBuildings> waitBuild = buildingControl.getWaitBuild();
		for( UnBuildings unBuild : waitBuild ){
			if( getDepotControl().deductProp( unBuild.templet().needres ) ){
				unBuild.setrTime( (int) (System.currentTimeMillis()/1000) );
			}
		}
		// 检测科技
		List<UnTechs> waitTech = techControl.getWaitTech();
		for( UnTechs unTech : waitTech ){
			if( getDepotControl().deductProp( unTech.templet().needStuff ) ){
				unTech.setrTime( (int) (System.currentTimeMillis()/1000) );
			}
		}
			
		// 添加玩家的贡献度
		Child child = getChild( player.getUID() );
		child.addContribution( prop.getContributions() );
		// 先排个序
		updateChildSequence();
		// 下面算出话语权 顺便对玩家进行排序
		int allcont = updateAllContribution();
		short privilege = (short) (((float)child.getContribution() / (float)allcont) * 10000f);
		child.setPrivilege( privilege );
		
		// 捐献成功后 同步消息
		Syn.res( childs, update );
	}
	
	
	/**
	 * 获取该星球的 所有贡献值总和 - 对玩家进行标记是否可以发起投票
	 * @return
	 */
	public int updateAllContribution() {
		// 下面标记是否可以发起投票
		int ret = 0;
		int privilege = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			ret += child.getContribution();
			privilege += child.getPrivilege();
			child.setSenator( institution.isSenator( i , privilege ) );
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
	
	/////////////////// =================建筑====================
	
	@Override
	public void sponsorBuivote( Player player, int nid, byte index, int time ) throws Exception { 
		
		// 判断是否有权限发起投票
		Child child = getChild( player.getUID() );
		if( child == null || !child.isSenator() )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 判断位置是否占用
		Sbuilding templet = CsvGen.getSbuilding(nid);
		if( buildingControl.isOccupyInIndex( index, templet.usegrid ) )
			throw new Exception( ErrorCode.INDEX_OCCUPY.name() );
		
		// 判断该建筑能不能建
		if( !buildingControl.isCanBuild( nid ) )
			throw new Exception( ErrorCode.YET_ATLIST.name() );
		
		// 添加到投票中
		UnBuildings voteBuild = buildingControl.appendVoteBuild( player, nid, index, time );
		
		// 记录玩家发起数
		child.addSponsors( 1 );
		
		// 下面同步消息给玩家
		Syn.build( childs, 1, voteBuild );
	}
	
	@Override
	public void participateBuildVote( Player player, int nid, byte isAgree ) throws Exception { 
		
		// 判断是否有权限投票 只要有话语权都可以投票
		Child child = getChild( player.getUID() );
		if( child == null || child.getPrivilege() == 0 )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 判断是否已经参与投票了
		UnBuildings unBuild = buildingControl.getVoteBuild( nid );
		if( unBuild == null || unBuild.getVote().isParticipateVote( player.getUID() ) )
			throw new Exception( ErrorCode.ALREADY_VOTE.name() );
		
		// 设置投票
		byte status = unBuild.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
		// 说明投票完成
		if( status != -1 ){
			if( status == 1 ) // 同意建筑
				startBuild( unBuild );
			if( status == 0 ){ // 反对建筑
				//...暂时没有处理
			}
			// 最后不管怎样都要删掉的
			buildingControl.removeVoteBuild( unBuild );
			
			// 同步
			Syn.build( childs, status+2, unBuild );
		}
	}
	
	// 开始建筑
	private void startBuild( UnBuildings unBuild )  {
		
		// 判断资源是否够 - 开始扣资源
		if( getDepotControl().deductProp( unBuild.templet().needres ) )
			unBuild.setrTime( (int) (System.currentTimeMillis()/1000) );
		else
			unBuild.setrTime( -1 );
		
		// 放入建筑中 列表
		buildingControl.appendUnBuild( unBuild );
		
		// 设置发起人的通过数
		Child sponsor = getChild( unBuild.getVote().getSponsorUid() );
		sponsor.addPasss( 1 );
	}
	
	/////////////////// =================科技====================
	@Override
	public void sponsorTechVote( Player player, int nid, int time ) throws Exception { 
		// 判断 有没有权限
		Child child = getChild( player.getUID() );
		if( child == null || !child.isSenator() )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 判断是否能研究
		if( !techControl.isCanStudy( nid, techLevel ) )
			throw new Exception( ErrorCode.CON_DISSATISFY.name() );
		
		// 添加到投票列表
		UnTechs unTechs = techControl.appendVote( player, nid, time );
		
		// 记录玩家发起数
		child.addSponsors( 1 );
		
		// 下面同步消息给玩家
		Syn.tech( childs, 1, unTechs );
	}
	
	@Override
	public void participateTechVote(Player player, int nid, byte isAgree) throws Exception { 
		// 判断是否有权限投票 只要有话语权都可以投票
		Child child = getChild( player.getUID() );
		if( child == null || child.getPrivilege() == 0 )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );

		// 判断是否已经参与投票了
		UnTechs unTech = techControl.getVoTech( nid );
		if( unTech == null || unTech.getVote().isParticipateVote( player.getUID() ) )
			throw new Exception( ErrorCode.ALREADY_VOTE.name() );
		
		// 设置投票
		byte status = unTech.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
		// 说明投票完成
		if( status != -1 ){
			if( status == 1 ) // 同意建筑
				startStudy( unTech );
			if( status == 0 ){ // 反对建筑
				//...暂时没有处理
			}
			// 最后不管怎样都要删掉的
			techControl.removeVoTech( unTech );
			
			// 同步
			Syn.tech( childs, status+2, unTech );
		}
	}
	// 开始研究科技
	private void startStudy(UnTechs unTech) {
		
		// 判断资源是否够 - 开始扣资源
		if( getDepotControl().deductProp( unTech.templet().needStuff ) )
			unTech.setrTime( (int) (System.currentTimeMillis()/1000) );
		else
			unTech.setrTime( -1 );
		// 放入建筑中 列表
		techControl.appendUnTech( unTech );
		// 设置发起人的通过数
		Child sponsor = getChild( unTech.getVote().getSponsorUid() );
		sponsor.addPasss( 1 );
	}
	
	// 刷新科技等级
	private void updateTechLevel() {
		techLevel = techControl.getMaxTechLevel();
	}
	
	/////////////////// =================元老====================
	@Override
	public void sponsorGenrVote( Player player, String uid, String explain ) throws Exception { 
		
		// 判断 有没有权限
		Child child = getChild( player.getUID() );
		if( child == null || !child.isSenator() )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		// 判断 被驱逐的是不是 元老
		child = getChild( uid );
		if( child == null || !child.isSenator() )
			throw new Exception( ErrorCode.NOT_SENATOR.name() );
		// 在看是不是已经在投票列表中
		OustChild oust = getOustChild( uid );
		if( oust != null )
			throw new Exception( ErrorCode.YET_ATLIST.name() );
		
		oust = new OustChild( player, uid, explain );
		oustChilds.add(oust);
		
		synchronizeGenrVote( childs, 1, oust );
	}
	
	private void synchronizeGenrVote( List<Child> childs, int isAdd, OustChild oust) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			Child x = getChild( oust.getUid() );
			((Update_2252)Events.UPDATE_2252.getEventInstance()).run( player, isAdd, oust, x );
		}
	}
	
	private OustChild getOustChild( String uid ) {
		for( OustChild o : oustChilds ){
			if( o.getUid().equals( uid ) )
				return o;
		}
		return null;
	}
	private void removeVoteGenr( OustChild oust ) {
		Iterator<OustChild> it = oustChilds.iterator();
		while( it.hasNext() ){
			OustChild next = it.next();
			if( next.getUid().equals( oust.getUid() ) ){
				it.remove();
				break;
			}
		}
	}
	@Override
	public void participateGenrVote( Player player, String uid, byte isAgree ) throws Exception {
		
		// 判断是否有权限投票  只有元老才能投票
		Child child = getChild( player.getUID() );
		if( child == null || child.isSenator() )
			throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
		
		// 判断是否已经参与投票了
		OustChild oust = getOustChild( uid );
		if( oust == null || oust.getVote().isParticipateVote( player.getUID() ) )
			throw new Exception( ErrorCode.ALREADY_VOTE.name() );
		
		// 设置投票
		byte status = oust.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
		// 说明投票完成
		if( status != -1 ){
			if( status == 1 ) { // 同意
	
			}
			if( status == 0 ){ // 反对
				//...暂时没有处理
			}
			// 最后不管怎样都要删掉的
			removeVoteGenr( oust );
			// 同步
			synchronizeGenrVote( childs, 0, oust );
		}
		
	}


	/**
	 * 研发 线程 - 包括科技研究 建筑建造
	 */
	public void runDevelopment(){
		// 建筑
		UnBuildings build = buildingControl.runDevelopment();
		if( build != null ){// 同步玩家
			Syn.build( childs, 4, build );
			Logs.debug( templet().name + " 建筑(" + build.templet().name + "," + build.templet().id + ") 修建完毕!" );
		}
		// 科技
		UnTechs tech = techControl.runDevelopment();
		if( tech != null ){
			Syn.tech( childs, 4, tech );
			Logs.debug( templet().name + " 科技(" + tech.templet().name + "," + tech.templet().id + ") 研究完毕!" );
		}
	}
	
	
}
