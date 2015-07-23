package cn.xgame.gen.dto;import java.sql.SQLException;import java.util.List;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				/**		 * 获取数据库某张表的最大id		 * @param tableName		 * @param col		 * @param criteria 条件		 * @return		 * @throws SQLException		 */		public static int getMaxId( String tableName, String col, String criteria ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select max(" + col + ") from " + tableName + " where " + criteria );				db.executeQuery();				if ( db.next())					return db.getInt( 1 );			} catch (Exception e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				/**		 * 获取某张表的数据总数		 * @param tableName		 * @return		 */		public static long getCount( String tableName ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select COUNT(*) from " + tableName );				db.executeQuery();				if ( db.next() )					return db.getLong( 1 );			} catch (SQLException e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				@SuppressWarnings("rawtypes")		public static String getClassName( Class clzss ) {			return clzss.getSimpleName().replaceAll("Dto", "").toLowerCase();		}		public static CaptainsDao getCaptainsDao() {			return new CaptainsDao( "captains" );		}
		public static PlanetDataDao getPlanetDataDao() {			return new PlanetDataDao( "planetdata" );		}
		public static PlayerDataDao getPlayerDataDao() {			return new PlayerDataDao( "playerdata" );		}
		public static PropsDao getPropsDao() {			return new PropsDao( "props" );		}
		public static ShipsDao getShipsDao() {			return new ShipsDao( "ships" );		}

	}			public static class CaptainsDao extends SqlDao{				public CaptainsDao( String tableName ) {			super( tableName );		}				public CaptainsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				CaptainsDto x = new CaptainsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<CaptainsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<CaptainsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<CaptainsDto> getLs() {			List<CaptainsDto> ls = Lists.newArrayList();			while( next() ){				CaptainsDto x = new CaptainsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public CaptainsDto update(){			_update( );			return new CaptainsDto();		}		public CaptainsDto updateByExact( String arg ){			_updateByExact( arg );			return new CaptainsDto();		}				public CaptainsDto create() {			insert();			return new CaptainsDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( CaptainsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );

			super.commit( true );		}	}
	public static class PlanetDataDao extends SqlDao{				public PlanetDataDao( String tableName ) {			super( tableName );		}				public PlanetDataDto get( Integer id ) {			super.select( String.valueOf(id), true );			if( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlanetDataDto> getAll( Integer id ) {			super.select( String.valueOf(id), false );			return getLs();		}		public List<PlanetDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlanetDataDto> getLs() {			List<PlanetDataDto> ls = Lists.newArrayList();			while( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlanetDataDto update(){			_update( );			return new PlanetDataDto();		}		public PlanetDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlanetDataDto();		}				public PlanetDataDto create() {			insert();			return new PlanetDataDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlanetDataDto dto ) {			setObject( "id", dto.getId() );
			setObject( "maxSpace", dto.getMaxSpace() );
			setObject( "players", dto.getPlayers() );
			setObject( "expelGenr", dto.getExpelGenr() );
			setObject( "buildings", dto.getBuildings() );
			setObject( "depots", dto.getDepots() );
			setObject( "specialtys", dto.getSpecialtys() );
			setObject( "techs", dto.getTechs() );

			super.commit( true );		}	}
	public static class PlayerDataDao extends SqlDao{				public PlayerDataDao( String tableName ) {			super( tableName );		}				public PlayerDataDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlayerDataDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<PlayerDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlayerDataDto> getLs() {			List<PlayerDataDto> ls = Lists.newArrayList();			while( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlayerDataDto update(){			_update( );			return new PlayerDataDto();		}		public PlayerDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlayerDataDto();		}				public PlayerDataDto create() {			insert();			return new PlayerDataDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlayerDataDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uid", dto.getUid() );
			setObject( "createTime", dto.getCreateTime() );
			setObject( "lastLogoutTime", dto.getLastLogoutTime() );
			setObject( "nickname", dto.getNickname() );
			setObject( "headIco", dto.getHeadIco() );
			setObject( "adjutantId", dto.getAdjutantId() );
			setObject( "countryId", dto.getCountryId() );
			setObject( "currency", dto.getCurrency() );
			setObject( "gold", dto.getGold() );
			setObject( "accEctypes", dto.getAccEctypes() );
			setObject( "manors", dto.getManors() );

			super.commit( true );		}	}
	public static class PropsDao extends SqlDao{				public PropsDao( String tableName ) {			super( tableName );		}				public PropsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				PropsDto x = new PropsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PropsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<PropsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PropsDto> getLs() {			List<PropsDto> ls = Lists.newArrayList();			while( next() ){				PropsDto x = new PropsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PropsDto update(){			_update( );			return new PropsDto();		}		public PropsDto updateByExact( String arg ){			_updateByExact( arg );			return new PropsDto();		}				public PropsDto create() {			insert();			return new PropsDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PropsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );
			setObject( "attach", dto.getAttach() );

			super.commit( true );		}	}
	public static class ShipsDao extends SqlDao{				public ShipsDao( String tableName ) {			super( tableName );		}				public ShipsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				ShipsDto x = new ShipsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<ShipsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<ShipsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<ShipsDto> getLs() {			List<ShipsDto> ls = Lists.newArrayList();			while( next() ){				ShipsDto x = new ShipsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public ShipsDto update(){			_update( );			return new ShipsDto();		}		public ShipsDto updateByExact( String arg ){			_updateByExact( arg );			return new ShipsDto();		}				public ShipsDto create() {			insert();			return new ShipsDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( ShipsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "captainUid", dto.getCaptainUid() );
			setObject( "starId", dto.getStarId() );
			setObject( "holds", dto.getHolds() );
			setObject( "weapons", dto.getWeapons() );
			setObject( "assists", dto.getAssists() );

			super.commit( true );		}	}

	public static class CaptainsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;

		public CaptainsDto() {		}				/**		 * Copy new one		 */		public CaptainsDto(CaptainsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x) {			return "nid=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid;		}	}
	public static class PlanetDataDto implements SqlDto{		private Integer id = null;
		private Short maxSpace = null;
		private byte[] players = null;
		private byte[] expelGenr = null;
		private byte[] buildings = null;
		private byte[] depots = null;
		private byte[] specialtys = null;
		private byte[] techs = null;

		public PlanetDataDto() {		}				/**		 * Copy new one		 */		public PlanetDataDto(PlanetDataDto src) {			this.id = src.id;
			this.maxSpace = src.maxSpace;
			this.players = src.players;
			this.expelGenr = src.expelGenr;
			this.buildings = src.buildings;
			this.depots = src.depots;
			this.specialtys = src.specialtys;
			this.techs = src.techs;

		}		/**  星球ID  */		public Integer getId(){			return this.id;		}
		/**  星球总空间  */		public Short getMaxSpace(){			return this.maxSpace;		}
		/**  玩家列表  */		public byte[] getPlayers(){			return this.players;		}
		/**  驱逐元老列表  */		public byte[] getExpelGenr(){			return this.expelGenr;		}
		/**  星球建筑  */		public byte[] getBuildings(){			return this.buildings;		}
		/**  星球仓库  */		public byte[] getDepots(){			return this.depots;		}
		/**  星球特产  */		public byte[] getSpecialtys(){			return this.specialtys;		}
		/**  星球科技  */		public byte[] getTechs(){			return this.techs;		}

		/**  星球ID  */		public void setId( Integer id ){			this.id = id;		}
		/**  星球总空间  */		public void setMaxSpace( Short maxSpace ){			this.maxSpace = maxSpace;		}
		/**  玩家列表  */		public void setPlayers( byte[] players ){			this.players = players;		}
		/**  驱逐元老列表  */		public void setExpelGenr( byte[] expelGenr ){			this.expelGenr = expelGenr;		}
		/**  星球建筑  */		public void setBuildings( byte[] buildings ){			this.buildings = buildings;		}
		/**  星球仓库  */		public void setDepots( byte[] depots ){			this.depots = depots;		}
		/**  星球特产  */		public void setSpecialtys( byte[] specialtys ){			this.specialtys = specialtys;		}
		/**  星球科技  */		public void setTechs( byte[] techs ){			this.techs = techs;		}

		public static String idChangeSql( Integer x) {			return "id=" + x;		}
		public static String maxSpaceChangeSql( Short x) {			return "maxSpace=" + x;		}
		public static String playersChangeSql( byte[] x) {			return "players=" + x;		}
		public static String expelGenrChangeSql( byte[] x) {			return "expelGenr=" + x;		}
		public static String buildingsChangeSql( byte[] x) {			return "buildings=" + x;		}
		public static String depotsChangeSql( byte[] x) {			return "depots=" + x;		}
		public static String specialtysChangeSql( byte[] x) {			return "specialtys=" + x;		}
		public static String techsChangeSql( byte[] x) {			return "techs=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getInt( "id" );
			maxSpace = o.getShort( "maxSpace" );
			players = o.getBytes( "players" );
			expelGenr = o.getBytes( "expelGenr" );
			buildings = o.getBytes( "buildings" );
			depots = o.getBytes( "depots" );
			specialtys = o.getBytes( "specialtys" );
			techs = o.getBytes( "techs" );

		}				@Override		public String toString() {			return "id="+id+","+"maxSpace="+maxSpace+","+"players="+players+","+"expelGenr="+expelGenr+","+"buildings="+buildings+","+"depots="+depots+","+"specialtys="+specialtys+","+"techs="+techs;		}	}
	public static class PlayerDataDto implements SqlDto{		private Short gsid = null;
		private String uid = null;
		private Long createTime = null;
		private Long lastLogoutTime = null;
		private String nickname = null;
		private Integer headIco = null;
		private Integer adjutantId = null;
		private Integer countryId = null;
		private Integer currency = null;
		private Integer gold = null;
		private byte[] accEctypes = null;
		private byte[] manors = null;

		public PlayerDataDto() {		}				/**		 * Copy new one		 */		public PlayerDataDto(PlayerDataDto src) {			this.gsid = src.gsid;
			this.uid = src.uid;
			this.createTime = src.createTime;
			this.lastLogoutTime = src.lastLogoutTime;
			this.nickname = src.nickname;
			this.headIco = src.headIco;
			this.adjutantId = src.adjutantId;
			this.countryId = src.countryId;
			this.currency = src.currency;
			this.gold = src.gold;
			this.accEctypes = src.accEctypes;
			this.manors = src.manors;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  唯一ID  */		public String getUid(){			return this.uid;		}
		/**  创建时间  */		public Long getCreateTime(){			return this.createTime;		}
		/**  上次下线时间  */		public Long getLastLogoutTime(){			return this.lastLogoutTime;		}
		/**  名字  */		public String getNickname(){			return this.nickname;		}
		/**  头像图标ID  */		public Integer getHeadIco(){			return this.headIco;		}
		/**  副官ID  */		public Integer getAdjutantId(){			return this.adjutantId;		}
		/**  区域  */		public Integer getCountryId(){			return this.countryId;		}
		/**  游戏币  */		public Integer getCurrency(){			return this.currency;		}
		/**  充值币  */		public Integer getGold(){			return this.gold;		}
		/**  偶发副本  */		public byte[] getAccEctypes(){			return this.accEctypes;		}
		/**  领地  */		public byte[] getManors(){			return this.manors;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  唯一ID  */		public void setUid( String uid ){			this.uid = uid;		}
		/**  创建时间  */		public void setCreateTime( Long createTime ){			this.createTime = createTime;		}
		/**  上次下线时间  */		public void setLastLogoutTime( Long lastLogoutTime ){			this.lastLogoutTime = lastLogoutTime;		}
		/**  名字  */		public void setNickname( String nickname ){			this.nickname = nickname;		}
		/**  头像图标ID  */		public void setHeadIco( Integer headIco ){			this.headIco = headIco;		}
		/**  副官ID  */		public void setAdjutantId( Integer adjutantId ){			this.adjutantId = adjutantId;		}
		/**  区域  */		public void setCountryId( Integer countryId ){			this.countryId = countryId;		}
		/**  游戏币  */		public void setCurrency( Integer currency ){			this.currency = currency;		}
		/**  充值币  */		public void setGold( Integer gold ){			this.gold = gold;		}
		/**  偶发副本  */		public void setAccEctypes( byte[] accEctypes ){			this.accEctypes = accEctypes;		}
		/**  领地  */		public void setManors( byte[] manors ){			this.manors = manors;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String uidChangeSql( String x) {			return "uid=" + "'"+x+"'";		}
		public static String createTimeChangeSql( Long x) {			return "createTime=" + x;		}
		public static String lastLogoutTimeChangeSql( Long x) {			return "lastLogoutTime=" + x;		}
		public static String nicknameChangeSql( String x) {			return "nickname=" + "'"+x+"'";		}
		public static String headIcoChangeSql( Integer x) {			return "headIco=" + x;		}
		public static String adjutantIdChangeSql( Integer x) {			return "adjutantId=" + x;		}
		public static String countryIdChangeSql( Integer x) {			return "countryId=" + x;		}
		public static String currencyChangeSql( Integer x) {			return "currency=" + x;		}
		public static String goldChangeSql( Integer x) {			return "gold=" + x;		}
		public static String accEctypesChangeSql( byte[] x) {			return "accEctypes=" + x;		}
		public static String manorsChangeSql( byte[] x) {			return "manors=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uid = o.getString( "uid" );
			createTime = o.getLong( "createTime" );
			lastLogoutTime = o.getLong( "lastLogoutTime" );
			nickname = o.getString( "nickname" );
			headIco = o.getInt( "headIco" );
			adjutantId = o.getInt( "adjutantId" );
			countryId = o.getInt( "countryId" );
			currency = o.getInt( "currency" );
			gold = o.getInt( "gold" );
			accEctypes = o.getBytes( "accEctypes" );
			manors = o.getBytes( "manors" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uid="+uid+","+"createTime="+createTime+","+"lastLogoutTime="+lastLogoutTime+","+"nickname="+nickname+","+"headIco="+headIco+","+"adjutantId="+adjutantId+","+"countryId="+countryId+","+"currency="+currency+","+"gold="+gold+","+"accEctypes="+accEctypes+","+"manors="+manors;		}	}
	public static class PropsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;
		private byte[] attach = null;

		public PropsDto() {		}				/**		 * Copy new one		 */		public PropsDto(PropsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.count = src.count;
			this.attach = src.attach;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}
		/**  数量  */		public Integer getCount(){			return this.count;		}
		/**  附加数据  */		public byte[] getAttach(){			return this.attach;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}
		/**  数量  */		public void setCount( Integer count ){			this.count = count;		}
		/**  附加数据  */		public void setAttach( byte[] attach ){			this.attach = attach;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x) {			return "nid=" + x;		}
		public static String countChangeSql( Integer x) {			return "count=" + x;		}
		public static String attachChangeSql( byte[] x) {			return "attach=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			count = o.getInt( "count" );
			attach = o.getBytes( "attach" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"count="+count+","+"attach="+attach;		}	}
	public static class ShipsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer captainUid = null;
		private Integer starId = null;
		private byte[] holds = null;
		private byte[] weapons = null;
		private byte[] assists = null;

		public ShipsDto() {		}				/**		 * Copy new one		 */		public ShipsDto(ShipsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.captainUid = src.captainUid;
			this.starId = src.starId;
			this.holds = src.holds;
			this.weapons = src.weapons;
			this.assists = src.assists;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}
		/**  舰长唯一ID  */		public Integer getCaptainUid(){			return this.captainUid;		}
		/**  停靠的星球ID  */		public Integer getStarId(){			return this.starId;		}
		/**  货仓  */		public byte[] getHolds(){			return this.holds;		}
		/**  武器  */		public byte[] getWeapons(){			return this.weapons;		}
		/**  辅助  */		public byte[] getAssists(){			return this.assists;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}
		/**  舰长唯一ID  */		public void setCaptainUid( Integer captainUid ){			this.captainUid = captainUid;		}
		/**  停靠的星球ID  */		public void setStarId( Integer starId ){			this.starId = starId;		}
		/**  货仓  */		public void setHolds( byte[] holds ){			this.holds = holds;		}
		/**  武器  */		public void setWeapons( byte[] weapons ){			this.weapons = weapons;		}
		/**  辅助  */		public void setAssists( byte[] assists ){			this.assists = assists;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x) {			return "nid=" + x;		}
		public static String captainUidChangeSql( Integer x) {			return "captainUid=" + x;		}
		public static String starIdChangeSql( Integer x) {			return "starId=" + x;		}
		public static String holdsChangeSql( byte[] x) {			return "holds=" + x;		}
		public static String weaponsChangeSql( byte[] x) {			return "weapons=" + x;		}
		public static String assistsChangeSql( byte[] x) {			return "assists=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			captainUid = o.getInt( "captainUid" );
			starId = o.getInt( "starId" );
			holds = o.getBytes( "holds" );
			weapons = o.getBytes( "weapons" );
			assists = o.getBytes( "assists" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"captainUid="+captainUid+","+"starId="+starId+","+"holds="+holds+","+"weapons="+weapons+","+"assists="+assists;		}	}

}