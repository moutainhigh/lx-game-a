package cn.xgame.gen.dto;import java.sql.SQLException;import java.util.List;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				/**		 * 获取数据库某张表的最大id		 * @param tableName		 * @param col		 * @param criteria 条件		 * @return		 * @throws SQLException		 */		public static int getMaxId( String tableName, String col, String criteria ) throws SQLException{			DBObject db = DBObject.create();			db.prepareStatement( "select max(" + col + ") from " + tableName + " where " + criteria );			db.executeQuery();			if ( db.next())				return db.getInt( 1 );			db.close();			return 0;		}				/**		 * 获取某张表的数据总数		 * @param tableName		 * @return		 */		public static long getCount( String tableName ) throws SQLException {			DBObject db = DBObject.create();			db.prepareStatement( "select COUNT(*) from " + tableName );			db.executeQuery();			if ( db.next() )				return db.getLong( 1 );			db.close();			return 0;		}				@SuppressWarnings("rawtypes")		public static String getClassName( Class clzss ) {			return clzss.getSimpleName().replaceAll("Dto", "").toLowerCase();		}		public static CaptainDao getCaptainDao() {			return new CaptainDao( "captain" );		}
		public static PlayerDataDao getPlayerDataDao() {			return new PlayerDataDao( "playerdata" );		}
		public static StuffDao getStuffDao() {			return new StuffDao( "stuff" );		}

	}			public static class CaptainDao extends SqlDao{				public CaptainDao( String tableName ) {			super( tableName );		}				public CaptainDto get( String id ) {			super.select( id, true );			if( next() ){				CaptainDto x = new CaptainDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<CaptainDto> getAll( String id ) {			super.select( id, false );			return getLs();		}		public List<CaptainDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<CaptainDto> getLs() {			List<CaptainDto> ls = Lists.newArrayList();			while( next() ){				CaptainDto x = new CaptainDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public CaptainDto update(){			_update( );			return new CaptainDto();		}		public CaptainDto updateByExact( String arg ){			_updateByExact( arg );			return new CaptainDto();		}				public CaptainDto create() {			insert();			return new CaptainDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( CaptainDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class PlayerDataDao extends SqlDao{				public PlayerDataDao( String tableName ) {			super( tableName );		}				public PlayerDataDto get( String id ) {			super.select( id, true );			if( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlayerDataDto> getAll( String id ) {			super.select( id, false );			return getLs();		}		public List<PlayerDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlayerDataDto> getLs() {			List<PlayerDataDto> ls = Lists.newArrayList();			while( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlayerDataDto update(){			_update( );			return new PlayerDataDto();		}		public PlayerDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlayerDataDto();		}				public PlayerDataDto create() {			insert();			return new PlayerDataDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlayerDataDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uid", dto.getUid() );
			setObject( "createTime", dto.getCreateTime() );
			setObject( "lastLogoutTime", dto.getLastLogoutTime() );
			setObject( "nickname", dto.getNickname() );
			setObject( "headIco", dto.getHeadIco() );
			setObject( "countryId", dto.getCountryId() );
			setObject( "currency", dto.getCurrency() );
			setObject( "gold", dto.getGold() );
			setObject( "manors", dto.getManors() );

			super.commit( true );		}	}
	public static class StuffDao extends SqlDao{				public StuffDao( String tableName ) {			super( tableName );		}				public StuffDto get( String id ) {			super.select( id, true );			if( next() ){				StuffDto x = new StuffDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<StuffDto> getAll( String id ) {			super.select( id, false );			return getLs();		}		public List<StuffDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<StuffDto> getLs() {			List<StuffDto> ls = Lists.newArrayList();			while( next() ){				StuffDto x = new StuffDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public StuffDto update(){			_update( );			return new StuffDto();		}		public StuffDto updateByExact( String arg ){			_updateByExact( arg );			return new StuffDto();		}				public StuffDto create() {			insert();			return new StuffDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( StuffDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}

	public static class CaptainDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public CaptainDto() {		}				/**		 * Copy new one		 */		public CaptainDto(CaptainDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.count = src.count;

		}		/** 服务器ID */		public Short getGsid(){			return this.gsid;		}
		/** 玩家唯一ID */		public String getUname(){			return this.uname;		}
		/** 唯一ID */		public Integer getUid(){			return this.uid;		}
		/** 表格ID */		public Integer getNid(){			return this.nid;		}
		/** 数量 */		public Integer getCount(){			return this.count;		}

		/** 服务器ID */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/** 玩家唯一ID */		public void setUname( String uname ){			this.uname = uname;		}
		/** 唯一ID */		public void setUid( Integer uid ){			this.uid = uid;		}
		/** 表格ID */		public void setNid( Integer nid ){			this.nid = nid;		}
		/** 数量 */		public void setCount( Integer count ){			this.count = count;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x) {			return "nid=" + x;		}
		public static String countChangeSql( Integer x) {			return "count=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			count = o.getInt( "count" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"count="+count;		}	}
	public static class PlayerDataDto implements SqlDto{		private Short gsid = null;
		private String uid = null;
		private Long createTime = null;
		private Long lastLogoutTime = null;
		private String nickname = null;
		private Integer headIco = null;
		private Short countryId = null;
		private Integer currency = null;
		private Integer gold = null;
		private byte[] manors = null;

		public PlayerDataDto() {		}				/**		 * Copy new one		 */		public PlayerDataDto(PlayerDataDto src) {			this.gsid = src.gsid;
			this.uid = src.uid;
			this.createTime = src.createTime;
			this.lastLogoutTime = src.lastLogoutTime;
			this.nickname = src.nickname;
			this.headIco = src.headIco;
			this.countryId = src.countryId;
			this.currency = src.currency;
			this.gold = src.gold;
			this.manors = src.manors;

		}		/** 服务器ID */		public Short getGsid(){			return this.gsid;		}
		/** 唯一ID */		public String getUid(){			return this.uid;		}
		/** 创建时间 */		public Long getCreateTime(){			return this.createTime;		}
		/** 上次下线时间 */		public Long getLastLogoutTime(){			return this.lastLogoutTime;		}
		/** 名字 */		public String getNickname(){			return this.nickname;		}
		/** 头像图标ID */		public Integer getHeadIco(){			return this.headIco;		}
		/** 区域 */		public Short getCountryId(){			return this.countryId;		}
		/** 游戏币 */		public Integer getCurrency(){			return this.currency;		}
		/** 充值币 */		public Integer getGold(){			return this.gold;		}
		/** 领地 */		public byte[] getManors(){			return this.manors;		}

		/** 服务器ID */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/** 唯一ID */		public void setUid( String uid ){			this.uid = uid;		}
		/** 创建时间 */		public void setCreateTime( Long createTime ){			this.createTime = createTime;		}
		/** 上次下线时间 */		public void setLastLogoutTime( Long lastLogoutTime ){			this.lastLogoutTime = lastLogoutTime;		}
		/** 名字 */		public void setNickname( String nickname ){			this.nickname = nickname;		}
		/** 头像图标ID */		public void setHeadIco( Integer headIco ){			this.headIco = headIco;		}
		/** 区域 */		public void setCountryId( Short countryId ){			this.countryId = countryId;		}
		/** 游戏币 */		public void setCurrency( Integer currency ){			this.currency = currency;		}
		/** 充值币 */		public void setGold( Integer gold ){			this.gold = gold;		}
		/** 领地 */		public void setManors( byte[] manors ){			this.manors = manors;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String uidChangeSql( String x) {			return "uid=" + "'"+x+"'";		}
		public static String createTimeChangeSql( Long x) {			return "createTime=" + x;		}
		public static String lastLogoutTimeChangeSql( Long x) {			return "lastLogoutTime=" + x;		}
		public static String nicknameChangeSql( String x) {			return "nickname=" + "'"+x+"'";		}
		public static String headIcoChangeSql( Integer x) {			return "headIco=" + x;		}
		public static String countryIdChangeSql( Short x) {			return "countryId=" + x;		}
		public static String currencyChangeSql( Integer x) {			return "currency=" + x;		}
		public static String goldChangeSql( Integer x) {			return "gold=" + x;		}
		public static String manorsChangeSql( byte[] x) {			return "manors=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uid = o.getString( "uid" );
			createTime = o.getLong( "createTime" );
			lastLogoutTime = o.getLong( "lastLogoutTime" );
			nickname = o.getString( "nickname" );
			headIco = o.getInt( "headIco" );
			countryId = o.getShort( "countryId" );
			currency = o.getInt( "currency" );
			gold = o.getInt( "gold" );
			manors = o.getBytes( "manors" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uid="+uid+","+"createTime="+createTime+","+"lastLogoutTime="+lastLogoutTime+","+"nickname="+nickname+","+"headIco="+headIco+","+"countryId="+countryId+","+"currency="+currency+","+"gold="+gold+","+"manors="+manors;		}	}
	public static class StuffDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public StuffDto() {		}				/**		 * Copy new one		 */		public StuffDto(StuffDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.count = src.count;

		}		/** 服务器ID */		public Short getGsid(){			return this.gsid;		}
		/** 玩家唯一ID */		public String getUname(){			return this.uname;		}
		/** 唯一ID */		public Integer getUid(){			return this.uid;		}
		/** 表格ID */		public Integer getNid(){			return this.nid;		}
		/** 数量 */		public Integer getCount(){			return this.count;		}

		/** 服务器ID */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/** 玩家唯一ID */		public void setUname( String uname ){			this.uname = uname;		}
		/** 唯一ID */		public void setUid( Integer uid ){			this.uid = uid;		}
		/** 表格ID */		public void setNid( Integer nid ){			this.nid = nid;		}
		/** 数量 */		public void setCount( Integer count ){			this.count = count;		}

		public static String gsidChangeSql( Short x) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x) {			return "nid=" + x;		}
		public static String countChangeSql( Integer x) {			return "count=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			count = o.getInt( "count" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"count="+count;		}	}

}