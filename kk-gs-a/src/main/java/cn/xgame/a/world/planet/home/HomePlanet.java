package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.string.StringUtil;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;


import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.exchange.ExchangeControl;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.a.world.planet.data.tech.Techs;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Institution;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet extends IPlanet {

	//////////////////////////////临时数据
	// 体制
	private Institution 	institution;
	
	// 科技等级
	private byte 			techLevel 	= 0;
	
	// 瞭望距离 
	private int 			qutlook 	= 0;
	private List<Integer> 	scopes		= Lists.newArrayList();
	
	// 可购买的领地列表
	private List<Integer> 	canBuyManor = Lists.newArrayList();
	
	// 商店列表
	private List<IProp> 	shops 		= Lists.newArrayList();
	
	//////////////////////////////数据库相关
	// 玩家列表
	private List<Child> 	childs 		= Lists.newArrayList();
	// 被驱逐投票中的元老列表
	private List<OustChild> oustChilds 	= Lists.newArrayList();
	
	// 科技列表
	private TechControl techControl ;
	
	// 交易所
	private ExchangeControl exchangeControl;

	public HomePlanet( StarsPo clone ) {
		super(clone);
		techControl 	= new TechControl( getId() );
		exchangeControl	= new ExchangeControl( getId() );
		updateShop();
	}

	@Override
	public Institution getInstitution() { return institution; }
	public void setInstitution(Institution institution) { this.institution = institution; }
	@Override
	public byte getTechLevel() { return techLevel; }
	public void setTechLevel(byte techLevel) { this.techLevel = techLevel; }
	public int getQutlook() { return qutlook; }
	public List<Integer> getScopePlanet(){ return scopes; }
	public void setQutlook(int qutlook) { this.qutlook = qutlook; }
	public ExchangeControl getExchange(){ return this.exchangeControl; }
	public TechControl getTech(){ return techControl; }
	
	@Override
	public int getPeople() { return childs.size(); }
	public List<Child> getPeoples() { return childs; }
	public List<OustChild> getOustChild(){ return oustChilds; }
	
	@Override
	public void init( PlanetDataDto dto ) {
		super.init(dto);
		// 初始体制
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
		exchangeControl.fromBytes( dto.getExchanges() );
		// 根据科技获取最大星球科技等级
		updateTechData( );
		// 从数据库 获取数据后 更新体制 - 顺便更新玩家是否元老
		updateInstitution();
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
		// 瞭望距离
		buffer.writeInt( qutlook );
		// 已建筑数据 and 建筑中数据
		buildingControl.putBuilding(buffer);
		// 所有科技数据
		techControl.putTechs(buffer);
		// 研究中科技
		techControl.putUnTech(buffer);
	}

	/**
	 * 塞入一个玩家的数据
	 * @param player
	 * @param response
	 */
	public void putPlyaerInfo( Player player, ByteBuf response ) {
		Child o = getChild( player.getUID() );
		response.writeShort( o == null ? 0 : o.getPrivilege() );
		response.writeInt(  o == null ? 0 : o.getContribution() );
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
		dto.setExchanges( exchangeControl.toBytes() );
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
	 * 获取玩家头衔 1.独裁 2.元老 3.平民
	 * @param temp
	 * @return
	 */
	public int getHonor( Player temp ) {
		Child child = getChild(temp.getUID());
		if( child == null ) return 3;
		if( institution == Institution.AUTARCHY && childs.indexOf(child) == 0 )
			return 1;
		if( child.isSenator() )
			return 2;
		return 3;
	}
	
	/**
	 * 处理捐献后的操作
	 * @param player
	 * @param prop
	 */
	public void handleDonateLater( Player player, IProp prop ) {
		
		// 添加玩家的贡献度
		Child child = getChild( player.getUID() );
		child.addContribution( prop.getContributions() );
		// 下面算出话语权 顺便对玩家进行排序
		int allcont = getAllContribution();
		short privilege = (short) (((float)child.getContribution() / (float)allcont) * 10000f);
		child.setPrivilege( privilege );
		// 更新一下是否元老
		updateIsSenator();
	}
	
	/**
	 * 获取该星球的 所有贡献值总和 - 对玩家进行标记是否可以发起投票
	 * @return
	 */
	public int getAllContribution() {
		int ret = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			ret += child.getContribution();
		}
		return ret;
	}
	
	// 更新是否元老
	private void updateIsSenator(){
		// 先排个序
		updateChildSequence();
		int privilege = 0;
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			if( child.getPrivilege() == 0 ){
				child.setSenator( false );
				continue;
			}
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
	
	
	/////////////////// =================科技====================
	
	// 刷新科技等级
	private void updateTechData() {
		techLevel = techControl.getMaxTechLevel();
	}
	
	/////////////////// =================元老====================
	public OustChild getOustChild( String uid ) {
		for( OustChild o : oustChilds ){
			if( o.getUid().equals( uid ) )
				return o;
		}
		return null;
	}
	public void removeVoteGenr( OustChild oust ) {
		Iterator<OustChild> it = oustChilds.iterator();
		while( it.hasNext() ){
			OustChild next = it.next();
			if( next.getUid().equals( oust.getUid() ) ){
				it.remove();
				break;
			}
		}
	}
	/////////////////// =================线程====================

	/** 线程 */
	public void run() {
		// 这里处理 特产
		List<Specialty> ls = getSpecialtyControl().getSpecialtys();
		for( Specialty spe : ls ){
			if( spe.run() )
				Logs.debug( "星球"+getId()+" 特产"+spe.toProp().getNid()+" 生产 "+spe.toProp().getCount()+"个" );
		}
	}
	
	/** 更新体制 */
	public void updateInstitution() {
		// 先排个序
		updateChildSequence();
		
		// 算出体制
		institution = Institution.REPUBLIC;
		int privilege = 0;
		for( int i = 0; i < childs.size() && i < 12; i++ ){
			Child child = childs.get(i);
			// 说明后面都是0  为0的是没有话语权的
			if( child.getPrivilege() == 0 )
				break;
			//独裁体质：一个人的话语权>50%，独裁体质形成。
			if( i == 0 && child.getPrivilege() > 5000 ){
				institution = Institution.AUTARCHY;
				break;
			}
			privilege += child.getPrivilege();
			//元老体质：12个话语权最高的人，话语总和权大于50%
			if( i == 11 && privilege > 5000 ){
				institution = Institution.SENATOR;
				break;
			}
		}
		// 更新一下是否元老
		for( int i = 0; i < childs.size(); i++ ){
			Child child = childs.get(i);
			child.setExpel( false );
			if( child.getPrivilege() == 0 ){
				child.setSenator( false );
				continue;
			}
			privilege += child.getPrivilege();
			child.setSenator( institution.isSenator( i, privilege) );
		}
		Logs.debug( "星球" + getId() + " 更新体制 " + institution );
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
			updateTechData(  );
			handleTech( tech );
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
	// 处理科技
	private void handleTech( Techs tech ) {
		if( tech.templet().type == 2 )// 如果 功能升级 
		{
			switch( tech.templet().efftype ){
			case 1:// 瞭望距离
				qutlook += Integer.parseInt( StringUtil.convertNumberString( tech.templet().value ) );
				scopes.clear();
				scopes.addAll( WorldManager.o.getDistanceConfineTo( qutlook, templet().x, templet().y, templet().z ) );
				Logs.debug( "添加了瞭望距离 - " + scopes );
				break;
			case 2:// 领地
				canBuyManor.add( Integer.parseInt( StringUtil.convertNumberString( tech.templet().value ) ) );
				break;
			}
		}
	}
	
	////////////------------------------------交易
	
	private void updateShop() {
		Lua lua = LuaUtil.getStarshop();
		lua.getField("generateShopData").call( 0, getId(), shops );
	}
	
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
	 * @throws Exception 
	 */
	public IProp getShopProp( int nid ) throws Exception{
		List<IProp> scty = specialtyControl.toProps();
		// 特产
		for( IProp o : scty ){
			if( o.getNid() == nid )
				return o;
		}
		// 商店基础道具
		for( IProp o : shops ){
			if( o.getNid() == nid )
				return o;
		}
		throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
	}


}
