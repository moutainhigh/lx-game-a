package cn.xgame.gen.dto;import java.sql.SQLException;import java.util.List;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				/**		 * 获取数据库某张表的最大id		 * @param tableName		 * @param col		 * @param criteria 条件		 * @return		 * @throws SQLException		 */		public static int getMaxId( String tableName, String col, String criteria ) throws SQLException{			DBObject db = DBObject.create();			db.prepareStatement( "select max(" + col + ") from " + tableName + " where " + criteria );			db.executeQuery();			if ( db.next())				return db.getInt( 1 );			db.close();			return 0;		}				/**		 * 获取某张表的数据总数		 * @param tableName		 * @return		 */		public static long getCount( String tableName ) throws SQLException {			DBObject db = DBObject.create();			db.prepareStatement( "select COUNT(*) from " + tableName );			db.executeQuery();			if ( db.next() )				return db.getLong( 1 );			db.close();			return 0;		}				@SuppressWarnings("rawtypes")		public static String getClassName( Class clzss ) {			return clzss.getSimpleName().replaceAll("Dto", "").toLowerCase();		}		public static M_captainDao getM_captainDao() {			return new M_captainDao( "m_captain" );		}
		public static M_cequipDao getM_cequipDao() {			return new M_cequipDao( "m_cequip" );		}
		public static M_sequipDao getM_sequipDao() {			return new M_sequipDao( "m_sequip" );		}
		public static M_shipDao getM_shipDao() {			return new M_shipDao( "m_ship" );		}
		public static M_stuffDao getM_stuffDao() {			return new M_stuffDao( "m_stuff" );		}
		public static PlanetDataDao getPlanetDataDao() {			return new PlanetDataDao( "planetdata" );		}
		public static PlayerDataDao getPlayerDataDao() {			return new PlayerDataDao( "playerdata" );		}
		public static TestDao getTestDao() {			return new TestDao( "test" );		}

	}			public static class M_captainDao extends SqlDao{				public M_captainDao( String tableName ) {			super( tableName );		}				public M_captainDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				M_captainDto x = new M_captainDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<M_captainDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<M_captainDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<M_captainDto> getLs() {			List<M_captainDto> ls = Lists.newArrayList();			while( next() ){				M_captainDto x = new M_captainDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public M_captainDto update(){			_update( );			return new M_captainDto();		}		public M_captainDto updateByExact( String arg ){			_updateByExact( arg );			return new M_captainDto();		}				public M_captainDto create() {			insert();			return new M_captainDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( M_captainDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class M_cequipDao extends SqlDao{				public M_cequipDao( String tableName ) {			super( tableName );		}				public M_cequipDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				M_cequipDto x = new M_cequipDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<M_cequipDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<M_cequipDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<M_cequipDto> getLs() {			List<M_cequipDto> ls = Lists.newArrayList();			while( next() ){				M_cequipDto x = new M_cequipDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public M_cequipDto update(){			_update( );			return new M_cequipDto();		}		public M_cequipDto updateByExact( String arg ){			_updateByExact( arg );			return new M_cequipDto();		}				public M_cequipDto create() {			insert();			return new M_cequipDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( M_cequipDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class M_sequipDao extends SqlDao{				public M_sequipDao( String tableName ) {			super( tableName );		}				public M_sequipDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				M_sequipDto x = new M_sequipDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<M_sequipDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<M_sequipDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<M_sequipDto> getLs() {			List<M_sequipDto> ls = Lists.newArrayList();			while( next() ){				M_sequipDto x = new M_sequipDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public M_sequipDto update(){			_update( );			return new M_sequipDto();		}		public M_sequipDto updateByExact( String arg ){			_updateByExact( arg );			return new M_sequipDto();		}				public M_sequipDto create() {			insert();			return new M_sequipDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( M_sequipDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class M_shipDao extends SqlDao{				public M_shipDao( String tableName ) {			super( tableName );		}				public M_shipDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				M_shipDto x = new M_shipDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<M_shipDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<M_shipDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<M_shipDto> getLs() {			List<M_shipDto> ls = Lists.newArrayList();			while( next() ){				M_shipDto x = new M_shipDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public M_shipDto update(){			_update( );			return new M_shipDto();		}		public M_shipDto updateByExact( String arg ){			_updateByExact( arg );			return new M_shipDto();		}				public M_shipDto create() {			insert();			return new M_shipDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( M_shipDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class M_stuffDao extends SqlDao{				public M_stuffDao( String tableName ) {			super( tableName );		}				public M_stuffDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				M_stuffDto x = new M_stuffDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<M_stuffDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<M_stuffDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<M_stuffDto> getLs() {			List<M_stuffDto> ls = Lists.newArrayList();			while( next() ){				M_stuffDto x = new M_stuffDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public M_stuffDto update(){			_update( );			return new M_stuffDto();		}		public M_stuffDto updateByExact( String arg ){			_updateByExact( arg );			return new M_stuffDto();		}				public M_stuffDto create() {			insert();			return new M_stuffDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( M_stuffDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );

			super.commit( true );		}	}
	public static class PlanetDataDao extends SqlDao{				public PlanetDataDao( String tableName ) {			super( tableName );		}				public PlanetDataDto get( Short id ) {			super.select( String.valueOf(id), true );			if( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlanetDataDto> getAll( Short id ) {			super.select( String.valueOf(id), false );			return getLs();		}		public List<PlanetDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlanetDataDto> getLs() {			List<PlanetDataDto> ls = Lists.newArrayList();			while( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlanetDataDto update(){			_update( );			return new PlanetDataDto();		}		public PlanetDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlanetDataDto();		}				public PlanetDataDto create() {			insert();			return new PlanetDataDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlanetDataDto dto ) {			setObject( "id", dto.getId() );
			setObject( "maxSpace", dto.getMaxSpace() );
			setObject( "players", dto.getPlayers() );
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
			setObject( "countryId", dto.getCountryId() );
			setObject( "currency", dto.getCurrency() );
			setObject( "gold", dto.getGold() );
			setObject( "manors", dto.getManors() );

			super.commit( true );		}	}
	public static class TestDao extends SqlDao{				public TestDao( String tableName ) {			super( tableName );		}				public TestDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				TestDto x = new TestDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<TestDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<TestDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<TestDto> getLs() {			List<TestDto> ls = Lists.newArrayList();			while( next() ){				TestDto x = new TestDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public TestDto update(){			_update( );			return new TestDto();		}		public TestDto updateByExact( String arg ){			_updateByExact( arg );			return new TestDto();		}				public TestDto create() {			insert();			return new TestDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( TestDto dto ) {			setObject( "id", dto.getId() );
			setObject( "a", dto.getA() );

			super.commit( true );		}	}

	public static class M_captainDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public M_captainDto() {		}				/**		 * Copy new one		 */		public M_captainDto(M_captainDto src) {			this.gsid = src.gsid;
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
	public static class M_cequipDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public M_cequipDto() {		}				/**		 * Copy new one		 */		public M_cequipDto(M_cequipDto src) {			this.gsid = src.gsid;
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
	public static class M_sequipDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public M_sequipDto() {		}				/**		 * Copy new one		 */		public M_sequipDto(M_sequipDto src) {			this.gsid = src.gsid;
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
	public static class M_shipDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public M_shipDto() {		}				/**		 * Copy new one		 */		public M_shipDto(M_shipDto src) {			this.gsid = src.gsid;
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
	public static class M_stuffDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;

		public M_stuffDto() {		}				/**		 * Copy new one		 */		public M_stuffDto(M_stuffDto src) {			this.gsid = src.gsid;
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
	public static class PlanetDataDto implements SqlDto{		private Short id = null;
		private Short maxSpace = null;
		private byte[] players = null;
		private byte[] buildings = null;
		private byte[] depots = null;
		private byte[] specialtys = null;
		private byte[] techs = null;

		public PlanetDataDto() {		}				/**		 * Copy new one		 */		public PlanetDataDto(PlanetDataDto src) {			this.id = src.id;
			this.maxSpace = src.maxSpace;
			this.players = src.players;
			this.buildings = src.buildings;
			this.depots = src.depots;
			this.specialtys = src.specialtys;
			this.techs = src.techs;

		}		/** 星球ID */		public Short getId(){			return this.id;		}
		/** 星球总空间 */		public Short getMaxSpace(){			return this.maxSpace;		}
		/** 玩家列表 */		public byte[] getPlayers(){			return this.players;		}
		/** 星球建筑 */		public byte[] getBuildings(){			return this.buildings;		}
		/** 星球仓库 */		public byte[] getDepots(){			return this.depots;		}
		/** 星球特产 */		public byte[] getSpecialtys(){			return this.specialtys;		}
		/** 星球科技 */		public byte[] getTechs(){			return this.techs;		}

		/** 星球ID */		public void setId( Short id ){			this.id = id;		}
		/** 星球总空间 */		public void setMaxSpace( Short maxSpace ){			this.maxSpace = maxSpace;		}
		/** 玩家列表 */		public void setPlayers( byte[] players ){			this.players = players;		}
		/** 星球建筑 */		public void setBuildings( byte[] buildings ){			this.buildings = buildings;		}
		/** 星球仓库 */		public void setDepots( byte[] depots ){			this.depots = depots;		}
		/** 星球特产 */		public void setSpecialtys( byte[] specialtys ){			this.specialtys = specialtys;		}
		/** 星球科技 */		public void setTechs( byte[] techs ){			this.techs = techs;		}

		public static String idChangeSql( Short x) {			return "id=" + x;		}
		public static String maxSpaceChangeSql( Short x) {			return "maxSpace=" + x;		}
		public static String playersChangeSql( byte[] x) {			return "players=" + x;		}
		public static String buildingsChangeSql( byte[] x) {			return "buildings=" + x;		}
		public static String depotsChangeSql( byte[] x) {			return "depots=" + x;		}
		public static String specialtysChangeSql( byte[] x) {			return "specialtys=" + x;		}
		public static String techsChangeSql( byte[] x) {			return "techs=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getShort( "id" );
			maxSpace = o.getShort( "maxSpace" );
			players = o.getBytes( "players" );
			buildings = o.getBytes( "buildings" );
			depots = o.getBytes( "depots" );
			specialtys = o.getBytes( "specialtys" );
			techs = o.getBytes( "techs" );

		}				@Override		public String toString() {			return "id="+id+","+"maxSpace="+maxSpace+","+"players="+players+","+"buildings="+buildings+","+"depots="+depots+","+"specialtys="+specialtys+","+"techs="+techs;		}	}
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
	public static class TestDto implements SqlDto{		private String id = null;
		private Integer a = null;

		public TestDto() {		}				/**		 * Copy new one		 */		public TestDto(TestDto src) {			this.id = src.id;
			this.a = src.a;

		}		/** 无 */		public String getId(){			return this.id;		}
		/** 无 */		public Integer getA(){			return this.a;		}

		/** 无 */		public void setId( String id ){			this.id = id;		}
		/** 无 */		public void setA( Integer a ){			this.a = a;		}

		public static String idChangeSql( String x) {			return "id=" + "'"+x+"'";		}
		public static String aChangeSql( Integer x) {			return "a=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getString( "id" );
			a = o.getInt( "a" );

		}				@Override		public String toString() {			return "id="+id+","+"a="+a;		}	}

}