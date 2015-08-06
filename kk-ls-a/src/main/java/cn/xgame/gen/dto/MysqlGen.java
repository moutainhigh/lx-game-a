package cn.xgame.gen.dto;import java.sql.SQLException;import java.util.List;import x.javaplus.collections.Lists;import x.javaplus.mysql.db.DBObject;import x.javaplus.mysql.db.SqlDao;import x.javaplus.mysql.db.SqlDto;public class MysqlGen {		/**	 * Mysql操作类 <br>	 * <br>	 * 列子 <br>	 * <br>	 * =====获取数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao(); <br>	 * XXXDto dto = dao.get( "101" );// 获取单个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getAll( "101" );// 获取多个数据  默认根据id查找 <br>	 * List.XXXDto dto = dao.getByExact( "id="+101 );// 获取多个数据  根据自定义sql语句查找 可以用Condition类方便的生成语句;<br>	 * dao.commit(); // 最后提交 每个操作后 都要调用 commit<br>	 * dao.commit( dto ); // 提交并保存dto  只能保存单个数据<br>	 * <br>	 * =====创建数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * RoleDto dto = dao.create();// 表示创建开始 <br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto <br>	 * <br>	 * =====保存数据===== <br>	 * XXXDao dao = SqlUtil.getXXXDao();<br>	 * XXXDto dto = dao.update();// 保存单个数据 默认根据id来保存 <br>	 * XXXDto dto = dao.update( "id="+101 ); // 保存多个数据  根据自定义语句来保存   这种模式必须设置id<br>	 * dto.setId( "101" );<br>	 * dto.setName( "大峰哥" );<br>	 * dto.setLevel( 99 );<br>	 * dao.commit( dto );// 提交并保存dto 默认根据id保存 <br>	 * <br>	 * =====删除数据===== <br>	 * RoleDao dao = SqlUtil.getRoleDao(); <br>	 * dao.delete( "101" );<br>     * dao.deleteByExact( "id="+101 );<br>     * dao.commit();<br>	 *<br>	 *<br>	 * @author deng	 *	 */	public static final class SqlUtil {				/**		 * 获取数据库某张表的最大id		 * @param tableName		 * @param col		 * @param criteria 条件		 * @return		 * @throws SQLException		 */		public static int getMaxId( String tableName, String col, String criteria ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select max(" + col + ") from " + tableName + " where " + criteria );				db.executeQuery();				if ( db.next())					return db.getInt( 1 );			} catch (Exception e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				/**		 * 获取某张表的数据总数		 * @param tableName		 * @return		 */		public static long getCount( String tableName ) {			DBObject db = DBObject.create();			try {				db.prepareStatement( "select COUNT(*) from " + tableName );				db.executeQuery();				if ( db.next() )					return db.getLong( 1 );			} catch (SQLException e) {				e.printStackTrace();			}finally{				db.close();			}			return 0;		}				@SuppressWarnings("rawtypes")		public static String getClassName( Class clzss ) {			return clzss.getSimpleName().replaceAll("Dto", "").toLowerCase();		}		public static UserDao getUserDao() {			return new UserDao( "`kkloginserver`.user" );		}

	}			public static class UserDao extends SqlDao{				public UserDao( String tableName ) {			super( tableName );		}				public UserDto get( String id ) {			super.select( "'"+id+"'", true );			if( next() ){				UserDto x = new UserDto();				x.fromDBObject( getObject() );				return x;			}			return null;		}				public List<UserDto> getAll( String id ) {			super.select( "'"+id+"'", false );			return getLs();		}		public List<UserDto> getByExact( String arg ) {			super.selectByExact( arg );			return getLs();		}				private List<UserDto> getLs() {			List<UserDto> ls = Lists.newArrayList();			while( next() ){				UserDto x = new UserDto();				x.fromDBObject( getObject() ) ;				ls.add( x );			}			return ls;		}				public UserDto update(){			_update( );			return new UserDto();		}		public UserDto updateByExact( String arg ){			_updateByExact( arg );			return new UserDto();		}				public UserDto create() {			insert();			return new UserDto();		}				public void delete( String id ){			super.delete( id );		}		public void deleteByExact( String arg ){			super.deleteByExact( arg );		}				public void commit(){			super.commit( false );		}				public void commit( UserDto dto ) {			setObject( "id", dto.getId() );
			setObject( "account", dto.getAccount() );
			setObject( "password", dto.getPassword() );
			setObject( "lastGsid", dto.getLastGsid() );

			super.commit( true );		}	}

	public static class UserDto implements SqlDto{		private String id = null;
		private String account = null;
		private String password = null;
		private Short lastGsid = null;

		public UserDto() {		}				/**		 * Copy new one		 */		public UserDto(UserDto src) {			this.id = src.id;
			this.account = src.account;
			this.password = src.password;
			this.lastGsid = src.lastGsid;

		}		/** 无 */		public String getId(){			return this.id;		}
		/**   账号  */		public String getAccount(){			return this.account;		}
		/**   密码  */		public String getPassword(){			return this.password;		}
		/**   最后一次登录的服务器ID  */		public Short getLastGsid(){			return this.lastGsid;		}

		/** 无 */		public void setId( String id ){			this.id = id;		}
		/**   账号  */		public void setAccount( String account ){			this.account = account;		}
		/**   密码  */		public void setPassword( String password ){			this.password = password;		}
		/**   最后一次登录的服务器ID  */		public void setLastGsid( Short lastGsid ){			this.lastGsid = lastGsid;		}

		public static String idChangeSql( String x) {			return "id=" + "'"+x+"'";		}
		public static String accountChangeSql( String x) {			return "account=" + "'"+x+"'";		}
		public static String passwordChangeSql( String x) {			return "password=" + "'"+x+"'";		}
		public static String lastGsidChangeSql( Short x) {			return "lastGsid=" + x;		}

		@Override		public void fromDBObject(DBObject o) {			id = o.getString( "id" );
			account = o.getString( "account" );
			password = o.getString( "password" );
			lastGsid = o.getShort( "lastGsid" );

		}				@Override		public String toString() {			return "id="+id+","+"account="+account+","+"password="+password+","+"lastGsid="+lastGsid;		}	}

}