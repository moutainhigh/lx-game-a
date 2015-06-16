package cn.xgame.gen.dto;import java.util.List;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				public static PlayerDataDao getPlayerDataDao() {			return new PlayerDataDao( "playerdata" );		}

	}			public static class PlayerDataDao extends SqlDao{				public PlayerDataDao( String tableName ) {			super( tableName );		}				public PlayerDataDto get( String id ) {			super.select( id, true );			if( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<PlayerDataDto> getAll( String id ) {			super.select( id, false );			return getLs();		}		public List<PlayerDataDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<PlayerDataDto> getLs() {			List<PlayerDataDto> ls = Lists.newArrayList();			while( next() ){				PlayerDataDto x = new PlayerDataDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public PlayerDataDto update(){			_update( );			return new PlayerDataDto();		}		public PlayerDataDto updateByExact( String arg ){			_updateByExact( arg );			return new PlayerDataDto();		}				public PlayerDataDto create() {			insert();			return new PlayerDataDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( PlayerDataDto dto ) {			setObject( "UID", dto.getUID() );
			setObject( "nickname", dto.getNickname() );
			setObject( "level", dto.getLevel() );
			setObject( "gold", dto.getGold() );

			super.commit( true );		}	}

	public static class PlayerDataDto implements SqlDto{		private String UID = null;
		private String nickname = null;
		private Integer level = null;
		private Long gold = null;

		public PlayerDataDto() {		}				/**		 * Copy new one		 */		public PlayerDataDto(PlayerDataDto src) {			this.UID = src.UID;
			this.nickname = src.nickname;
			this.level = src.level;
			this.gold = src.gold;

		}		/** 唯一ID */		public String getUID(){			return this.UID;		}
		/** 名字 */		public String getNickname(){			return this.nickname;		}
		/** 等级 */		public Integer getLevel(){			return this.level;		}
		/** 金币 */		public Long getGold(){			return this.gold;		}

		/** 唯一ID */		public void setUID( String UID ){			this.UID = UID;		}
		/** 名字 */		public void setNickname( String nickname ){			this.nickname = nickname;		}
		/** 等级 */		public void setLevel( Integer level ){			this.level = level;		}
		/** 金币 */		public void setGold( Long gold ){			this.gold = gold;		}

		public static String UIDChangeSql( String x) {			return "UID=" + "'"+x+"'";		}
		public static String nicknameChangeSql( String x) {			return "nickname=" + "'"+x+"'";		}
		public static String levelChangeSql( Integer x) {			return "level=" + x;		}
		public static String goldChangeSql( Long x) {			return "gold=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			UID = o.getString( "UID" );
			nickname = o.getString( "nickname" );
			level = o.getInt( "level" );
			gold = o.getLong( "gold" );

		}				@Override		public String toString() {			return "UID="+UID+","+"nickname="+nickname+","+"level="+level+","+"gold="+gold;		}	}

}