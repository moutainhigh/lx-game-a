package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.a.world.planet.data.tavern.TCaptain;
import cn.xgame.a.world.planet.data.tavern.TavernControl;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.data.vote.VotePlayer;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Institution;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Item;
import cn.xgame.config.o.Sbuilding;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_2252;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet extends IPlanet {

	public HomePlanet(Stars clone) {
		super(clone);
		qutlook = 100;
	}

	//////////////////////////////临时数据
	// 体制
	private Institution institution;
	
	// 科技等级
	private byte techLevel = 0;
	
	// 瞭望距离 
	private int qutlook = 0;
	
	// 酒馆
	private TavernControl tavernControl = new TavernControl();
	
	// 商店
	private List<IProp> shops = Lists.newArrayList();
	
	//////////////////////////////数据库相关
	// 玩家列表
	private List<Child> childs = Lists.newArrayList();
	// 被驱逐投票中的元老列表
	private List<OustChild> oustChilds = Lists.newArrayList();
	
	// 科技列表
	private TechControl techControl = new TechControl();

	
	@Override
	public Institution getInstitution() { return institution; }
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	@Override
	public byte getTechLevel() { return techLevel; }
	public void setTechLevel(byte techLevel) {
		this.techLevel = techLevel;
	}
	public int getQutlook() { return qutlook; }
	public void setQutlook(int qutlook) {
		this.qutlook = qutlook;
	}
	
	@Override
	public boolean isCanDonate() { return true; }
	
	public TavernControl getTavernControl() { return tavernControl; }
	
	@Override
	public int getPeople() { return childs.size(); }
	
	@Override
	public void init( PlanetDataDto dto ) {
		super.init(dto);
		setInstitution(Institution.REPUBLIC);
		updateTavern();
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
		// 从数据库 获取数据后 更新体制 - 顺便更新玩家是否元老
		updateInstitution();
		// 更新一下酒馆
		updateTavern();
		// 根据瞭望科技 更新副本信息
		updateEctype();
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
		// 体制
		buffer.writeByte( institution.toNumber() );
		// 科技等级
		buffer.writeByte( techLevel );
		// 已建筑数据 and 建筑中数据
		getBuildingControl().putBuilding(buffer);
		// 所有科技数据
		techControl.putTechs(buffer);
		// 研究中
		techControl.putUnTech(buffer);
	}
	
	@Override
	public void putAlllAffair( Player player, ByteBuf response ) { 
		// 投票中建筑
		getBuildingControl().putVoBuilding( player, response);
		// 投票中科技
		techControl.putVoTech( player, response);
		// 投票中驱逐元老
		response.writeByte( oustChilds.size() );
		for( OustChild o : oustChilds ){
			Child child = getChild( o.getUid() );
			child.buildTransformStream(response);
			RW.writeString( response, o.getExplain() );
			o.getVote().buildTransformStream(response);
			response.writeByte( o.getVote().isParticipateVote( player.getUID() ) );
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
	

	/**
	 * 刷新酒馆数据
	 */
	public void updateTavernCaptains(){
		
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
			if( getDepotControl().deductProp( unTech.templet().needres ) ){
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
		// 更新一下是否元老
		updateIsSenator();
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
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			ret += child.getContribution();
		}
		return ret;
	}
	
	// 更新是否元老
	private void updateIsSenator(){
		int privilege = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			privilege += child.getPrivilege();
			child.setSenator( institution.isSenator( i, privilege) );
		}
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
		if( unBuild == null )
			throw new Exception( ErrorCode.VOTE_NOTEXIST.name() );
		
		// 这里先将玩家已经投过的票清除
		unBuild.getVote().purgeVote( player.getUID() );
		
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
			Syn.build( childs, status+3, unBuild );
		}else{
			Syn.build( childs, 2, unBuild );
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
		if( unTech == null )
			throw new Exception( ErrorCode.VOTE_NOTEXIST.name() );
		
		// 这里先将玩家已经投过的票清除
		unTech.getVote().purgeVote( player.getUID() );
		
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
			Syn.tech( childs, status+3, unTech );
		}else{
			Syn.tech( childs, 2, unTech );
		}
	}
	// 开始研究科技
	private void startStudy(UnTechs unTech) {
		
		// 判断资源是否够 - 开始扣资源
		if( getDepotControl().deductProp( unTech.templet().needres ) )
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
		if( oust == null )
			throw new Exception( ErrorCode.VOTE_NOTEXIST.name() );
		
		// 这里先将玩家已经投过的票清除
		oust.getVote().purgeVote( player.getUID() );
		
		// 设置投票
		byte status = oust.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
		// 说明投票完成
		if( status != -1 ){
			child.setExpel( status == 1 ? true : false );
			// 最后不管怎样都要删掉的
			removeVoteGenr( oust );
			// 同步
			synchronizeGenrVote( childs, 0, oust );
		}
		
	}
	
	/////////////////// =================线程====================

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
		// 先排个序
		updateChildSequence();
		
		// 算出体制
		int status = 0;
		int privilege = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			privilege += child.getPrivilege();
			
			if( status == 0 ){
				Lua lua = LuaUtil.getGameData();
				LuaValue[] ret = lua.getField( "updateInstitution" ).call( 1, child, i, privilege );
				status = ret[0].getInt();
			}
			
			child.setExpel( false );
		}
		// 设置体制
		setInstitution( Institution.fromNumber( status == 0 ? 3 : status ) );
		// 更新一下是否元老
		updateIsSenator();
		
		Logs.debug( templet().name + " 更新体制 Institution=" + institution );
	}
	

	/**
	 * 研发-投票 线程 - 包括科技研究 建筑建造
	 */
	public void runDevelopment(){
		// ---------建筑
		// 建造完毕
		UnBuildings build = buildingControl.runDevelopment();
		if( build != null ){// 同步玩家
			Syn.build( childs, 5, build );
			Logs.debug( templet().name + " 建筑(" + build.templet().name + "," + build.templet().id + ") 修建完毕!" );
		}
		// 投票完毕
		build = buildingControl.runVote( );
		if( build != null ){
			Syn.build( childs, 3, build );
			Logs.debug( templet().name + " 建筑(" + build.templet().name + "," + build.templet().id + ") 投票完毕! - 不同意" );
		}
		// ---------科技
		// 研究完毕
		UnTechs tech = techControl.runDevelopment();
		if( tech != null ){
			Syn.tech( childs, 5, tech );
			Logs.debug( templet().name + " 科技(" + tech.templet().name + "," + tech.templet().id + ") 研究完毕!" );
		}
		// 投票完毕
		tech = techControl.runVote();
		if( tech != null ){
			Syn.tech( childs, 3, tech );
			Logs.debug( templet().name + " 科技(" + tech.templet().name + "," + tech.templet().id + ") 投票完毕! - 不同意" );
		}
	}
	
	/**
	 * 更新酒馆
	 */
	public void updateTavern(){
		tavernControl.updateTavern( templet().tavernCapTimes );
	}
	
	////////////------------------------------副本
	
	// 根据瞭望科技 更新副本信息
	private void updateEctype() {
		
	}
	
	
	////////////------------------------------交易
	
	/**
	 * 获取商店列表
	 * @return
	 */
	public List<IProp> getShopList() {
		List<IProp> ret = Lists.newArrayList();
		// 先加入特产
		ret.addAll( specialtyControl.toProps() );
		// 加入表格数据
		ret.addAll( shops );
		return ret;
	}
	
	/**
	 * 根据道具表格ID 获取道具
	 * @param nid
	 * @return
	 */
	public IProp getShopProp( int nid ){
		List<IProp> ls = getShopList();
		for( IProp o : ls ){
			if( o.getnId() == nid )
				return o;
		}
		return null;
	}
	
	/**
	 * 玩家购买商店道具
	 * @param player 
	 * @param nid 
	 * @param count  
	 * @return
	 * @throws Exception 
	 */
	public List<IProp> runShopBuy( Player player, int nid, int count ) throws Exception {
		// 道具是否存在
		IProp prop = getShopProp( nid );
		if( prop == null )
			throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
		
		// 数量是否足够 只有特产 才做这个判断
		if( prop.getCount() < count && prop.getuId() == 1 )
			throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() );
		
		Item item = CsvGen.getItem( prop.getnId() );
		int needGold = item.buygold <= 0 ? 1 : item.buygold;
		//先判断 玩家是否该星球的
		if( getChild( player.getUID() ) == null ){
			needGold += 1;
		}
	
		// 看金币是否足够
		if( player.changeCurrency( -needGold ) == -1 )
			throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
		
		// 加入玩家背包
		List<IProp> ret = player.getDepots().appendProp( nid, count );
		if( ret.isEmpty() )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		
		// 如果是特产 那么就要对应扣除数量
		if( prop.getuId() == 1 )
			specialtyControl.deduct( nid, count );
		
		// 及时更新数据库
		PlayerManager.o.update(player);
		
		return ret;
	}
	
	/**
	 * 玩家购买酒馆舰长
	 * @param player
	 * @param nid
	 * @return
	 * @throws Exception 
	 */
	public CaptainInfo runTavernBuy( Player player, int nid ) throws Exception {
		// 舰长是否存在
		TCaptain tcap = tavernControl.getTCaptain(nid);
		if( tcap == null )
			throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
		
		Item item = CsvGen.getItem( nid );
		int needGold = item.buygold <= 0 ? 1 : item.buygold;
		//先判断 玩家是否该星球的
		if( getChild( player.getUID() ) == null ){
			needGold += 1;
		}
		
		// 看金币是否足够
		if( player.changeCurrency( -needGold ) == -1 )
			throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
		
		// 放入舰长室
		CaptainInfo ret = player.getCaptains().createCaptain(nid);
		
		// 将舰长从酒馆里面删除掉
		tavernControl.remove( tcap );
		
		// 及时更新数据库
		PlayerManager.o.update(player);
		
		return ret;
	}
	
}
