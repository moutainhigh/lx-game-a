package cn.xgame.gen.dto;import java.sql.SQLException;import java.util.List;import cn.xgame.system.SystemCfg;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				/**		 * 获取数据库某张表的最大id		 * @param tableName		 * @param col		 * @param criteria 条件		 * @return		 * @throws SQLException		 */		public static int getMaxId( String tableName, String col, String criteria ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select max(" + col + ") from `" + SystemCfg.getDatabaseName()+"`." + tableName + " where " + criteria );				db.executeQuery();				if ( db.next())					return db.getInt( 1 );			} catch (Exception e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				/**		 * 获取某张表的数据总数		 * @param tableName		 * @return		 */		public static long getCount( String tableName ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select COUNT(*) from `" + SystemCfg.getDatabaseName()+"`." + tableName );				db.executeQuery();				if ( db.next() )					return db.getLong( 1 );			} catch (SQLException e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				@SuppressWarnings("rawtypes")		public static String getClassName( Class clzss ) {			return clzss.getSimpleName().replaceAll("Dto", "").toLowerCase();		}		public static AxnInfoDao getAxnInfoDao() {			return new AxnInfoDao( "`"+SystemCfg.getDatabaseName()+"`.axninfo" );		}
		public static CaptainsDao getCaptainsDao() {			return new CaptainsDao( "`"+SystemCfg.getDatabaseName()+"`.captains" );		}
		public static MailInfoDao getMailInfoDao() {			return new MailInfoDao( "`"+SystemCfg.getDatabaseName()+"`.mailinfo" );		}
		public static PlanetDataDao getPlanetDataDao() {			return new PlanetDataDao( "`"+SystemCfg.getDatabaseName()+"`.planetdata" );		}
		public static PlayerDataDao getPlayerDataDao() {			return new PlayerDataDao( "`"+SystemCfg.getDatabaseName()+"`.playerdata" );		}
		public static PropsDao getPropsDao() {			return new PropsDao( "`"+SystemCfg.getDatabaseName()+"`.props" );		}
		public static ShipsDao getShipsDao() {			return new ShipsDao( "`"+SystemCfg.getDatabaseName()+"`.ships" );		}

	}			public static class AxnInfoDao extends SqlDao{				public AxnInfoDao( String tableName ) {			super( tableName );		}				public AxnInfoDto get( Integer id ) {			super.select( String.valueOf(id), true );			if( next() ){				AxnInfoDto x = new AxnInfoDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<AxnInfoDto> getAll( Integer id ) {			super.select( String.valueOf(id), false );			return getLs();		}		public List<AxnInfoDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<AxnInfoDto> getLs() {			List<AxnInfoDto> ls = Lists.newArrayList();			while( next() ){				AxnInfoDto x = new AxnInfoDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public AxnInfoDto update(){			_update( );			return new AxnInfoDto();		}		public AxnInfoDto updateByExact( String arg ){			_updateByExact( arg );			return new AxnInfoDto();		}				public AxnInfoDto create() {			insert();			return new AxnInfoDto();		}				public void delete( Integer id ){			super.delete( String.valueOf(id) );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( AxnInfoDto dto ) {			setObject( "id", dto.getId() );
			setObject( "name", dto.getName() );
			setObject( "type", dto.getType() );
			setObject( "players", dto.getPlayers() );
			setObject( "chatHistory", dto.getChatHistory() );

			super.commit( true );		}	}
	public static class CaptainsDao extends SqlDao{				public CaptainsDao( String tableName ) {			super( tableName );		}				public CaptainsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				CaptainsDto x = new CaptainsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<CaptainsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<CaptainsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<CaptainsDto> getLs() {			List<CaptainsDto> ls = Lists.newArrayList();			while( next() ){				CaptainsDto x = new CaptainsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public CaptainsDto update(){			_update( );			return new CaptainsDto();		}		public CaptainsDto updateByExact( String arg ){			_updateByExact( arg );			return new CaptainsDto();		}				public CaptainsDto create() {			insert();			return new CaptainsDto();		}				public void delete( String id ){			super.delete( "'"+id+"'" );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( CaptainsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "quality", dto.getQuality() );
			setObject( "attachAttr", dto.getAttachAttr() );
			setObject( "shipUid", dto.getShipUid() );
			setObject( "equips", dto.getEquips() );

			super.commit( true );		}	}
	public static class MailInfoDao extends SqlDao{				public MailInfoDao( String tableName ) {			super( tableName );		}				public MailInfoDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				MailInfoDto x = new MailInfoDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<MailInfoDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<MailInfoDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<MailInfoDto> getLs() {			List<MailInfoDto> ls = Lists.newArrayList();			while( next() ){				MailInfoDto x = new MailInfoDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public MailInfoDto update(){			_update( );			return new MailInfoDto();		}		public MailInfoDto updateByExact( String arg ){			_updateByExact( arg );			return new MailInfoDto();		}				public MailInfoDto create() {			insert();			return new MailInfoDto();		}				public void delete( String id ){			super.delete( "'"+id+"'" );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( MailInfoDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "type", dto.getType() );
			setObject( "title", dto.getTitle() );
			setObject( "content", dto.getContent() );
			setObject( "money", dto.getMoney() );
			setObject( "adjuncts", dto.getAdjuncts() );
			setObject( "senderUID", dto.getSenderUID() );
			setObject( "senderName", dto.getSenderName() );
			setObject( "sendtime", dto.getSendtime() );
			setObject( "isRead", dto.getIsRead() );
			setObject( "durationtime", dto.getDurationtime() );

			super.commit( true );		}	}
	public static class PlanetDataDao extends SqlDao{				public PlanetDataDao( String tableName ) {			super( tableName );		}				public PlanetDataDto get( Integer id ) {			super.select( String.valueOf(id), true );			if( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlanetDataDto> getAll( Integer id ) {			super.select( String.valueOf(id), false );			return getLs();		}		public List<PlanetDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlanetDataDto> getLs() {			List<PlanetDataDto> ls = Lists.newArrayList();			while( next() ){				PlanetDataDto x = new PlanetDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlanetDataDto update(){			_update( );			return new PlanetDataDto();		}		public PlanetDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlanetDataDto();		}				public PlanetDataDto create() {			insert();			return new PlanetDataDto();		}				public void delete( Integer id ){			super.delete( String.valueOf(id) );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlanetDataDto dto ) {			setObject( "id", dto.getId() );
			setObject( "maxSpace", dto.getMaxSpace() );
			setObject( "players", dto.getPlayers() );
			setObject( "expelGenr", dto.getExpelGenr() );
			setObject( "buildings", dto.getBuildings() );
			setObject( "depots", dto.getDepots() );
			setObject( "specialtys", dto.getSpecialtys() );
			setObject( "techs", dto.getTechs() );
			setObject( "exchanges", dto.getExchanges() );

			super.commit( true );		}	}
	public static class PlayerDataDao extends SqlDao{				public PlayerDataDao( String tableName ) {			super( tableName );		}				public PlayerDataDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlayerDataDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<PlayerDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlayerDataDto> getLs() {			List<PlayerDataDto> ls = Lists.newArrayList();			while( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlayerDataDto update(){			_update( );			return new PlayerDataDto();		}		public PlayerDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlayerDataDto();		}				public PlayerDataDto create() {			insert();			return new PlayerDataDto();		}				public void delete( String id ){			super.delete( "'"+id+"'" );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlayerDataDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uid", dto.getUid() );
			setObject( "createTime", dto.getCreateTime() );
			setObject( "lastLogoutTime", dto.getLastLogoutTime() );
			setObject( "nickname", dto.getNickname() );
			setObject( "headIco", dto.getHeadIco() );
			setObject( "level", dto.getLevel() );
			setObject( "exp", dto.getExp() );
			setObject( "adjutantId", dto.getAdjutantId() );
			setObject( "countryId", dto.getCountryId() );
			setObject( "currency", dto.getCurrency() );
			setObject( "gold", dto.getGold() );
			setObject( "ectypes", dto.getEctypes() );
			setObject( "manors", dto.getManors() );
			setObject( "chatAxns", dto.getChatAxns() );
			setObject( "taverns", dto.getTaverns() );
			setObject( "fleets", dto.getFleets() );
			setObject( "tasks", dto.getTasks() );
			setObject( "swops", dto.getSwops() );

			super.commit( true );		}	}
	public static class PropsDao extends SqlDao{				public PropsDao( String tableName ) {			super( tableName );		}				public PropsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				PropsDto x = new PropsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PropsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<PropsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PropsDto> getLs() {			List<PropsDto> ls = Lists.newArrayList();			while( next() ){				PropsDto x = new PropsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PropsDto update(){			_update( );			return new PropsDto();		}		public PropsDto updateByExact( String arg ){			_updateByExact( arg );			return new PropsDto();		}				public PropsDto create() {			insert();			return new PropsDto();		}				public void delete( String id ){			super.delete( "'"+id+"'" );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PropsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "beSnid", dto.getBeSnid() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "count", dto.getCount() );
			setObject( "quality", dto.getQuality() );
			setObject( "attach", dto.getAttach() );

			super.commit( true );		}	}
	public static class ShipsDao extends SqlDao{				public ShipsDao( String tableName ) {			super( tableName );		}				public ShipsDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				ShipsDto x = new ShipsDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<ShipsDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<ShipsDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<ShipsDto> getLs() {			List<ShipsDto> ls = Lists.newArrayList();			while( next() ){				ShipsDto x = new ShipsDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public ShipsDto update(){			_update( );			return new ShipsDto();		}		public ShipsDto updateByExact( String arg ){			_updateByExact( arg );			return new ShipsDto();		}				public ShipsDto create() {			insert();			return new ShipsDto();		}				public void delete( String id ){			super.delete( "'"+id+"'" );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( ShipsDto dto ) {			setObject( "gsid", dto.getGsid() );
			setObject( "uname", dto.getUname() );
			setObject( "uid", dto.getUid() );
			setObject( "nid", dto.getNid() );
			setObject( "attachAttr", dto.getAttachAttr() );
			setObject( "currentHp", dto.getCurrentHp() );
			setObject( "captainUid", dto.getCaptainUid() );
			setObject( "berthSid", dto.getBerthSid() );
			setObject( "holds", dto.getHolds() );
			setObject( "weapons", dto.getWeapons() );
			setObject( "assists", dto.getAssists() );

			super.commit( true );		}	}

	public static class AxnInfoDto implements SqlDto{		private Integer id = null;
		private String name = null;
		private Byte type = null;
		private byte[] players = null;
		private byte[] chatHistory = null;

		public AxnInfoDto() {		}				/**		 * Copy new one		 */		public AxnInfoDto(AxnInfoDto src) {			this.id = src.id;
			this.name = src.name;
			this.type = src.type;
			this.players = src.players;
			this.chatHistory = src.chatHistory;

		}		/**  频道ID   */		public Integer getId(){			return this.id;		}
		/**  频道名字   */		public String getName(){			return this.name;		}
		/**  频道类型   */		public Byte getType(){			return this.type;		}
		/**  玩家列表   */		public byte[] getPlayers(){			return this.players;		}
		/**  聊天记录   */		public byte[] getChatHistory(){			return this.chatHistory;		}

		/**  频道ID   */		public void setId( Integer id ){			this.id = id;		}
		/**  频道名字   */		public void setName( String name ){			this.name = name;		}
		/**  频道类型   */		public void setType( Byte type ){			this.type = type;		}
		/**  玩家列表   */		public void setPlayers( byte[] players ){			this.players = players;		}
		/**  聊天记录   */		public void setChatHistory( byte[] chatHistory ){			this.chatHistory = chatHistory;		}

		public static String idChangeSql( Integer x ) {			return "id=" + x;		}
		public static String nameChangeSql( String x ) {			return "name=" + "'"+x+"'";		}
		public static String typeChangeSql( Byte x ) {			return "type=" + x;		}
		public static String playersChangeSql( byte[] x ) {			return "players=" + x;		}
		public static String chatHistoryChangeSql( byte[] x ) {			return "chatHistory=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getInt( "id" );
			name = o.getString( "name" );
			type = o.getByte( "type" );
			players = o.getBytes( "players" );
			chatHistory = o.getBytes( "chatHistory" );

		}				@Override		public String toString() {			return "id="+id+","+"name="+name+","+"type="+type+","+"players="+players+","+"chatHistory="+chatHistory;		}	}
	public static class CaptainsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private Byte quality = null;
		private byte[] attachAttr = null;
		private Integer shipUid = null;
		private byte[] equips = null;

		public CaptainsDto() {		}				/**		 * Copy new one		 */		public CaptainsDto(CaptainsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.quality = src.quality;
			this.attachAttr = src.attachAttr;
			this.shipUid = src.shipUid;
			this.equips = src.equips;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}
		/**  品质  */		public Byte getQuality(){			return this.quality;		}
		/**  附加属性  */		public byte[] getAttachAttr(){			return this.attachAttr;		}
		/**  所属舰船UID  */		public Integer getShipUid(){			return this.shipUid;		}
		/**  装备信息  */		public byte[] getEquips(){			return this.equips;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}
		/**  品质  */		public void setQuality( Byte quality ){			this.quality = quality;		}
		/**  附加属性  */		public void setAttachAttr( byte[] attachAttr ){			this.attachAttr = attachAttr;		}
		/**  所属舰船UID  */		public void setShipUid( Integer shipUid ){			this.shipUid = shipUid;		}
		/**  装备信息  */		public void setEquips( byte[] equips ){			this.equips = equips;		}

		public static String gsidChangeSql( Short x ) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x ) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x ) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x ) {			return "nid=" + x;		}
		public static String qualityChangeSql( Byte x ) {			return "quality=" + x;		}
		public static String attachAttrChangeSql( byte[] x ) {			return "attachAttr=" + x;		}
		public static String shipUidChangeSql( Integer x ) {			return "shipUid=" + x;		}
		public static String equipsChangeSql( byte[] x ) {			return "equips=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			quality = o.getByte( "quality" );
			attachAttr = o.getBytes( "attachAttr" );
			shipUid = o.getInt( "shipUid" );
			equips = o.getBytes( "equips" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"quality="+quality+","+"attachAttr="+attachAttr+","+"shipUid="+shipUid+","+"equips="+equips;		}	}
	public static class MailInfoDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Byte type = null;
		private byte[] title = null;
		private byte[] content = null;
		private Integer money = null;
		private byte[] adjuncts = null;
		private String senderUID = null;
		private String senderName = null;
		private Integer sendtime = null;
		private Byte isRead = null;
		private Integer durationtime = null;

		public MailInfoDto() {		}				/**		 * Copy new one		 */		public MailInfoDto(MailInfoDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.type = src.type;
			this.title = src.title;
			this.content = src.content;
			this.money = src.money;
			this.adjuncts = src.adjuncts;
			this.senderUID = src.senderUID;
			this.senderName = src.senderName;
			this.sendtime = src.sendtime;
			this.isRead = src.isRead;
			this.durationtime = src.durationtime;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  邮件类型  */		public Byte getType(){			return this.type;		}
		/**  邮件标题  */		public byte[] getTitle(){			return this.title;		}
		/**  邮件内容  */		public byte[] getContent(){			return this.content;		}
		/**  货币  */		public Integer getMoney(){			return this.money;		}
		/**  附件  */		public byte[] getAdjuncts(){			return this.adjuncts;		}
		/**  发送人UID  */		public String getSenderUID(){			return this.senderUID;		}
		/**  发送人名字  */		public String getSenderName(){			return this.senderName;		}
		/**  发送时间  */		public Integer getSendtime(){			return this.sendtime;		}
		/**  已读 & 已支付  */		public Byte getIsRead(){			return this.isRead;		}
		/**  时效 */		public Integer getDurationtime(){			return this.durationtime;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  邮件类型  */		public void setType( Byte type ){			this.type = type;		}
		/**  邮件标题  */		public void setTitle( byte[] title ){			this.title = title;		}
		/**  邮件内容  */		public void setContent( byte[] content ){			this.content = content;		}
		/**  货币  */		public void setMoney( Integer money ){			this.money = money;		}
		/**  附件  */		public void setAdjuncts( byte[] adjuncts ){			this.adjuncts = adjuncts;		}
		/**  发送人UID  */		public void setSenderUID( String senderUID ){			this.senderUID = senderUID;		}
		/**  发送人名字  */		public void setSenderName( String senderName ){			this.senderName = senderName;		}
		/**  发送时间  */		public void setSendtime( Integer sendtime ){			this.sendtime = sendtime;		}
		/**  已读 & 已支付  */		public void setIsRead( Byte isRead ){			this.isRead = isRead;		}
		/**  时效 */		public void setDurationtime( Integer durationtime ){			this.durationtime = durationtime;		}

		public static String gsidChangeSql( Short x ) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x ) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x ) {			return "uid=" + x;		}
		public static String typeChangeSql( Byte x ) {			return "type=" + x;		}
		public static String titleChangeSql( byte[] x ) {			return "title=" + x;		}
		public static String contentChangeSql( byte[] x ) {			return "content=" + x;		}
		public static String moneyChangeSql( Integer x ) {			return "money=" + x;		}
		public static String adjunctsChangeSql( byte[] x ) {			return "adjuncts=" + x;		}
		public static String senderUIDChangeSql( String x ) {			return "senderUID=" + "'"+x+"'";		}
		public static String senderNameChangeSql( String x ) {			return "senderName=" + "'"+x+"'";		}
		public static String sendtimeChangeSql( Integer x ) {			return "sendtime=" + x;		}
		public static String isReadChangeSql( Byte x ) {			return "isRead=" + x;		}
		public static String durationtimeChangeSql( Integer x ) {			return "durationtime=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			type = o.getByte( "type" );
			title = o.getBytes( "title" );
			content = o.getBytes( "content" );
			money = o.getInt( "money" );
			adjuncts = o.getBytes( "adjuncts" );
			senderUID = o.getString( "senderUID" );
			senderName = o.getString( "senderName" );
			sendtime = o.getInt( "sendtime" );
			isRead = o.getByte( "isRead" );
			durationtime = o.getInt( "durationtime" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"type="+type+","+"title="+title+","+"content="+content+","+"money="+money+","+"adjuncts="+adjuncts+","+"senderUID="+senderUID+","+"senderName="+senderName+","+"sendtime="+sendtime+","+"isRead="+isRead+","+"durationtime="+durationtime;		}	}
	public static class PlanetDataDto implements SqlDto{		private Integer id = null;
		private Short maxSpace = null;
		private byte[] players = null;
		private byte[] expelGenr = null;
		private byte[] buildings = null;
		private byte[] depots = null;
		private byte[] specialtys = null;
		private byte[] techs = null;
		private byte[] exchanges = null;

		public PlanetDataDto() {		}				/**		 * Copy new one		 */		public PlanetDataDto(PlanetDataDto src) {			this.id = src.id;
			this.maxSpace = src.maxSpace;
			this.players = src.players;
			this.expelGenr = src.expelGenr;
			this.buildings = src.buildings;
			this.depots = src.depots;
			this.specialtys = src.specialtys;
			this.techs = src.techs;
			this.exchanges = src.exchanges;

		}		/**  星球ID  */		public Integer getId(){			return this.id;		}
		/**  星球总空间  */		public Short getMaxSpace(){			return this.maxSpace;		}
		/**  玩家列表  */		public byte[] getPlayers(){			return this.players;		}
		/**  驱逐元老列表  */		public byte[] getExpelGenr(){			return this.expelGenr;		}
		/**  星球建筑  */		public byte[] getBuildings(){			return this.buildings;		}
		/**  星球仓库  */		public byte[] getDepots(){			return this.depots;		}
		/**  星球特产  */		public byte[] getSpecialtys(){			return this.specialtys;		}
		/**  星球科技  */		public byte[] getTechs(){			return this.techs;		}
		/**  交易所  */		public byte[] getExchanges(){			return this.exchanges;		}

		/**  星球ID  */		public void setId( Integer id ){			this.id = id;		}
		/**  星球总空间  */		public void setMaxSpace( Short maxSpace ){			this.maxSpace = maxSpace;		}
		/**  玩家列表  */		public void setPlayers( byte[] players ){			this.players = players;		}
		/**  驱逐元老列表  */		public void setExpelGenr( byte[] expelGenr ){			this.expelGenr = expelGenr;		}
		/**  星球建筑  */		public void setBuildings( byte[] buildings ){			this.buildings = buildings;		}
		/**  星球仓库  */		public void setDepots( byte[] depots ){			this.depots = depots;		}
		/**  星球特产  */		public void setSpecialtys( byte[] specialtys ){			this.specialtys = specialtys;		}
		/**  星球科技  */		public void setTechs( byte[] techs ){			this.techs = techs;		}
		/**  交易所  */		public void setExchanges( byte[] exchanges ){			this.exchanges = exchanges;		}

		public static String idChangeSql( Integer x ) {			return "id=" + x;		}
		public static String maxSpaceChangeSql( Short x ) {			return "maxSpace=" + x;		}
		public static String playersChangeSql( byte[] x ) {			return "players=" + x;		}
		public static String expelGenrChangeSql( byte[] x ) {			return "expelGenr=" + x;		}
		public static String buildingsChangeSql( byte[] x ) {			return "buildings=" + x;		}
		public static String depotsChangeSql( byte[] x ) {			return "depots=" + x;		}
		public static String specialtysChangeSql( byte[] x ) {			return "specialtys=" + x;		}
		public static String techsChangeSql( byte[] x ) {			return "techs=" + x;		}
		public static String exchangesChangeSql( byte[] x ) {			return "exchanges=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getInt( "id" );
			maxSpace = o.getShort( "maxSpace" );
			players = o.getBytes( "players" );
			expelGenr = o.getBytes( "expelGenr" );
			buildings = o.getBytes( "buildings" );
			depots = o.getBytes( "depots" );
			specialtys = o.getBytes( "specialtys" );
			techs = o.getBytes( "techs" );
			exchanges = o.getBytes( "exchanges" );

		}				@Override		public String toString() {			return "id="+id+","+"maxSpace="+maxSpace+","+"players="+players+","+"expelGenr="+expelGenr+","+"buildings="+buildings+","+"depots="+depots+","+"specialtys="+specialtys+","+"techs="+techs+","+"exchanges="+exchanges;		}	}
	public static class PlayerDataDto implements SqlDto{		private Short gsid = null;
		private String uid = null;
		private Long createTime = null;
		private Long lastLogoutTime = null;
		private String nickname = null;
		private Integer headIco = null;
		private Short level = null;
		private Integer exp = null;
		private Integer adjutantId = null;
		private Integer countryId = null;
		private Integer currency = null;
		private Integer gold = null;
		private byte[] ectypes = null;
		private byte[] manors = null;
		private byte[] chatAxns = null;
		private byte[] taverns = null;
		private byte[] fleets = null;
		private byte[] tasks = null;
		private byte[] swops = null;

		public PlayerDataDto() {		}				/**		 * Copy new one		 */		public PlayerDataDto(PlayerDataDto src) {			this.gsid = src.gsid;
			this.uid = src.uid;
			this.createTime = src.createTime;
			this.lastLogoutTime = src.lastLogoutTime;
			this.nickname = src.nickname;
			this.headIco = src.headIco;
			this.level = src.level;
			this.exp = src.exp;
			this.adjutantId = src.adjutantId;
			this.countryId = src.countryId;
			this.currency = src.currency;
			this.gold = src.gold;
			this.ectypes = src.ectypes;
			this.manors = src.manors;
			this.chatAxns = src.chatAxns;
			this.taverns = src.taverns;
			this.fleets = src.fleets;
			this.tasks = src.tasks;
			this.swops = src.swops;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  唯一ID  */		public String getUid(){			return this.uid;		}
		/**  创建时间  */		public Long getCreateTime(){			return this.createTime;		}
		/**  上次下线时间  */		public Long getLastLogoutTime(){			return this.lastLogoutTime;		}
		/**  名字  */		public String getNickname(){			return this.nickname;		}
		/**  头像图标ID  */		public Integer getHeadIco(){			return this.headIco;		}
		/**  玩家等级  */		public Short getLevel(){			return this.level;		}
		/**  玩家经验  */		public Integer getExp(){			return this.exp;		}
		/**  副官ID  */		public Integer getAdjutantId(){			return this.adjutantId;		}
		/**  区域  */		public Integer getCountryId(){			return this.countryId;		}
		/**  游戏币  */		public Integer getCurrency(){			return this.currency;		}
		/**  充值币  */		public Integer getGold(){			return this.gold;		}
		/**  副本(常驻副本-额外副本-偶发副本)  */		public byte[] getEctypes(){			return this.ectypes;		}
		/**  领地  */		public byte[] getManors(){			return this.manors;		}
		/**  聊天频道ID列表  */		public byte[] getChatAxns(){			return this.chatAxns;		}
		/**  酒馆数据  */		public byte[] getTaverns(){			return this.taverns;		}
		/**  舰队数据  */		public byte[] getFleets(){			return this.fleets;		}
		/**  任务数据  */		public byte[] getTasks(){			return this.tasks;		}
		/**  兑换数据  */		public byte[] getSwops(){			return this.swops;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  唯一ID  */		public void setUid( String uid ){			this.uid = uid;		}
		/**  创建时间  */		public void setCreateTime( Long createTime ){			this.createTime = createTime;		}
		/**  上次下线时间  */		public void setLastLogoutTime( Long lastLogoutTime ){			this.lastLogoutTime = lastLogoutTime;		}
		/**  名字  */		public void setNickname( String nickname ){			this.nickname = nickname;		}
		/**  头像图标ID  */		public void setHeadIco( Integer headIco ){			this.headIco = headIco;		}
		/**  玩家等级  */		public void setLevel( Short level ){			this.level = level;		}
		/**  玩家经验  */		public void setExp( Integer exp ){			this.exp = exp;		}
		/**  副官ID  */		public void setAdjutantId( Integer adjutantId ){			this.adjutantId = adjutantId;		}
		/**  区域  */		public void setCountryId( Integer countryId ){			this.countryId = countryId;		}
		/**  游戏币  */		public void setCurrency( Integer currency ){			this.currency = currency;		}
		/**  充值币  */		public void setGold( Integer gold ){			this.gold = gold;		}
		/**  副本(常驻副本-额外副本-偶发副本)  */		public void setEctypes( byte[] ectypes ){			this.ectypes = ectypes;		}
		/**  领地  */		public void setManors( byte[] manors ){			this.manors = manors;		}
		/**  聊天频道ID列表  */		public void setChatAxns( byte[] chatAxns ){			this.chatAxns = chatAxns;		}
		/**  酒馆数据  */		public void setTaverns( byte[] taverns ){			this.taverns = taverns;		}
		/**  舰队数据  */		public void setFleets( byte[] fleets ){			this.fleets = fleets;		}
		/**  任务数据  */		public void setTasks( byte[] tasks ){			this.tasks = tasks;		}
		/**  兑换数据  */		public void setSwops( byte[] swops ){			this.swops = swops;		}

		public static String gsidChangeSql( Short x ) {			return "gsid=" + x;		}
		public static String uidChangeSql( String x ) {			return "uid=" + "'"+x+"'";		}
		public static String createTimeChangeSql( Long x ) {			return "createTime=" + x;		}
		public static String lastLogoutTimeChangeSql( Long x ) {			return "lastLogoutTime=" + x;		}
		public static String nicknameChangeSql( String x ) {			return "nickname=" + "'"+x+"'";		}
		public static String headIcoChangeSql( Integer x ) {			return "headIco=" + x;		}
		public static String levelChangeSql( Short x ) {			return "level=" + x;		}
		public static String expChangeSql( Integer x ) {			return "exp=" + x;		}
		public static String adjutantIdChangeSql( Integer x ) {			return "adjutantId=" + x;		}
		public static String countryIdChangeSql( Integer x ) {			return "countryId=" + x;		}
		public static String currencyChangeSql( Integer x ) {			return "currency=" + x;		}
		public static String goldChangeSql( Integer x ) {			return "gold=" + x;		}
		public static String ectypesChangeSql( byte[] x ) {			return "ectypes=" + x;		}
		public static String manorsChangeSql( byte[] x ) {			return "manors=" + x;		}
		public static String chatAxnsChangeSql( byte[] x ) {			return "chatAxns=" + x;		}
		public static String tavernsChangeSql( byte[] x ) {			return "taverns=" + x;		}
		public static String fleetsChangeSql( byte[] x ) {			return "fleets=" + x;		}
		public static String tasksChangeSql( byte[] x ) {			return "tasks=" + x;		}
		public static String swopsChangeSql( byte[] x ) {			return "swops=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uid = o.getString( "uid" );
			createTime = o.getLong( "createTime" );
			lastLogoutTime = o.getLong( "lastLogoutTime" );
			nickname = o.getString( "nickname" );
			headIco = o.getInt( "headIco" );
			level = o.getShort( "level" );
			exp = o.getInt( "exp" );
			adjutantId = o.getInt( "adjutantId" );
			countryId = o.getInt( "countryId" );
			currency = o.getInt( "currency" );
			gold = o.getInt( "gold" );
			ectypes = o.getBytes( "ectypes" );
			manors = o.getBytes( "manors" );
			chatAxns = o.getBytes( "chatAxns" );
			taverns = o.getBytes( "taverns" );
			fleets = o.getBytes( "fleets" );
			tasks = o.getBytes( "tasks" );
			swops = o.getBytes( "swops" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uid="+uid+","+"createTime="+createTime+","+"lastLogoutTime="+lastLogoutTime+","+"nickname="+nickname+","+"headIco="+headIco+","+"level="+level+","+"exp="+exp+","+"adjutantId="+adjutantId+","+"countryId="+countryId+","+"currency="+currency+","+"gold="+gold+","+"ectypes="+ectypes+","+"manors="+manors+","+"chatAxns="+chatAxns+","+"taverns="+taverns+","+"fleets="+fleets+","+"tasks="+tasks+","+"swops="+swops;		}	}
	public static class PropsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer beSnid = null;
		private Integer uid = null;
		private Integer nid = null;
		private Integer count = null;
		private Byte quality = null;
		private byte[] attach = null;

		public PropsDto() {		}				/**		 * Copy new one		 */		public PropsDto(PropsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.beSnid = src.beSnid;
			this.uid = src.uid;
			this.nid = src.nid;
			this.count = src.count;
			this.quality = src.quality;
			this.attach = src.attach;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  所属星球ID  */		public Integer getBeSnid(){			return this.beSnid;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}
		/**  数量  */		public Integer getCount(){			return this.count;		}
		/**  品质  */		public Byte getQuality(){			return this.quality;		}
		/**  附加数据  */		public byte[] getAttach(){			return this.attach;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  所属星球ID  */		public void setBeSnid( Integer beSnid ){			this.beSnid = beSnid;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}
		/**  数量  */		public void setCount( Integer count ){			this.count = count;		}
		/**  品质  */		public void setQuality( Byte quality ){			this.quality = quality;		}
		/**  附加数据  */		public void setAttach( byte[] attach ){			this.attach = attach;		}

		public static String gsidChangeSql( Short x ) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x ) {			return "uname=" + "'"+x+"'";		}
		public static String beSnidChangeSql( Integer x ) {			return "beSnid=" + x;		}
		public static String uidChangeSql( Integer x ) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x ) {			return "nid=" + x;		}
		public static String countChangeSql( Integer x ) {			return "count=" + x;		}
		public static String qualityChangeSql( Byte x ) {			return "quality=" + x;		}
		public static String attachChangeSql( byte[] x ) {			return "attach=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			beSnid = o.getInt( "beSnid" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			count = o.getInt( "count" );
			quality = o.getByte( "quality" );
			attach = o.getBytes( "attach" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"beSnid="+beSnid+","+"uid="+uid+","+"nid="+nid+","+"count="+count+","+"quality="+quality+","+"attach="+attach;		}	}
	public static class ShipsDto implements SqlDto{		private Short gsid = null;
		private String uname = null;
		private Integer uid = null;
		private Integer nid = null;
		private byte[] attachAttr = null;
		private Integer currentHp = null;
		private Integer captainUid = null;
		private Integer berthSid = null;
		private byte[] holds = null;
		private byte[] weapons = null;
		private byte[] assists = null;

		public ShipsDto() {		}				/**		 * Copy new one		 */		public ShipsDto(ShipsDto src) {			this.gsid = src.gsid;
			this.uname = src.uname;
			this.uid = src.uid;
			this.nid = src.nid;
			this.attachAttr = src.attachAttr;
			this.currentHp = src.currentHp;
			this.captainUid = src.captainUid;
			this.berthSid = src.berthSid;
			this.holds = src.holds;
			this.weapons = src.weapons;
			this.assists = src.assists;

		}		/**  服务器ID  */		public Short getGsid(){			return this.gsid;		}
		/**  玩家唯一ID  */		public String getUname(){			return this.uname;		}
		/**  唯一ID  */		public Integer getUid(){			return this.uid;		}
		/**  表格ID  */		public Integer getNid(){			return this.nid;		}
		/**  附加属性  */		public byte[] getAttachAttr(){			return this.attachAttr;		}
		/**  当前血量  */		public Integer getCurrentHp(){			return this.currentHp;		}
		/**  舰长唯一ID  */		public Integer getCaptainUid(){			return this.captainUid;		}
		/**  停靠星球ID  */		public Integer getBerthSid(){			return this.berthSid;		}
		/**  货仓  */		public byte[] getHolds(){			return this.holds;		}
		/**  武器  */		public byte[] getWeapons(){			return this.weapons;		}
		/**  辅助  */		public byte[] getAssists(){			return this.assists;		}

		/**  服务器ID  */		public void setGsid( Short gsid ){			this.gsid = gsid;		}
		/**  玩家唯一ID  */		public void setUname( String uname ){			this.uname = uname;		}
		/**  唯一ID  */		public void setUid( Integer uid ){			this.uid = uid;		}
		/**  表格ID  */		public void setNid( Integer nid ){			this.nid = nid;		}
		/**  附加属性  */		public void setAttachAttr( byte[] attachAttr ){			this.attachAttr = attachAttr;		}
		/**  当前血量  */		public void setCurrentHp( Integer currentHp ){			this.currentHp = currentHp;		}
		/**  舰长唯一ID  */		public void setCaptainUid( Integer captainUid ){			this.captainUid = captainUid;		}
		/**  停靠星球ID  */		public void setBerthSid( Integer berthSid ){			this.berthSid = berthSid;		}
		/**  货仓  */		public void setHolds( byte[] holds ){			this.holds = holds;		}
		/**  武器  */		public void setWeapons( byte[] weapons ){			this.weapons = weapons;		}
		/**  辅助  */		public void setAssists( byte[] assists ){			this.assists = assists;		}

		public static String gsidChangeSql( Short x ) {			return "gsid=" + x;		}
		public static String unameChangeSql( String x ) {			return "uname=" + "'"+x+"'";		}
		public static String uidChangeSql( Integer x ) {			return "uid=" + x;		}
		public static String nidChangeSql( Integer x ) {			return "nid=" + x;		}
		public static String attachAttrChangeSql( byte[] x ) {			return "attachAttr=" + x;		}
		public static String currentHpChangeSql( Integer x ) {			return "currentHp=" + x;		}
		public static String captainUidChangeSql( Integer x ) {			return "captainUid=" + x;		}
		public static String berthSidChangeSql( Integer x ) {			return "berthSid=" + x;		}
		public static String holdsChangeSql( byte[] x ) {			return "holds=" + x;		}
		public static String weaponsChangeSql( byte[] x ) {			return "weapons=" + x;		}
		public static String assistsChangeSql( byte[] x ) {			return "assists=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			gsid = o.getShort( "gsid" );
			uname = o.getString( "uname" );
			uid = o.getInt( "uid" );
			nid = o.getInt( "nid" );
			attachAttr = o.getBytes( "attachAttr" );
			currentHp = o.getInt( "currentHp" );
			captainUid = o.getInt( "captainUid" );
			berthSid = o.getInt( "berthSid" );
			holds = o.getBytes( "holds" );
			weapons = o.getBytes( "weapons" );
			assists = o.getBytes( "assists" );

		}				@Override		public String toString() {			return "gsid="+gsid+","+"uname="+uname+","+"uid="+uid+","+"nid="+nid+","+"attachAttr="+attachAttr+","+"currentHp="+currentHp+","+"captainUid="+captainUid+","+"berthSid="+berthSid+","+"holds="+holds+","+"weapons="+weapons+","+"assists="+assists;		}	}

}