package cn.xgame.a.user;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.CRC;

import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.UserDao;
import cn.xgame.gen.dto.MysqlGen.UserDto;


public class UserManager {

	public static UserManager o = new UserManager();
	private UserManager(){}
	
	
//	private Map<String, UserData> users = new HashMap<String, UserData>();
	
	/**
	 * 根据账号密码 获取UID 如果没有找到账号 那么就创建
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String getMustUID( String account, String password ) throws Exception {
		
		UserDao dao 		= SqlUtil.getUserDao();
		UserDto dto 		= null;
		// 先判断是否有这个账号
		List<UserDto> dtos 	= dao.getByExact( UserDto.accountChangeSql(account) );
		if( !dtos.isEmpty() ){
			dao.commit();
			// 如果有这个账号判断 密码是否正确
			dto				= dtos.get(0);
			if( !dto.getPassword().equals(password) )
				throw new Exception( ErrorCode.PASSWORD_ERROR.name() );
		}else{
			// 如果没有这个账号 那么就在数据库创建
			String UID = generateUID( account, dao );
			dto = dao.create();
			dto.setId( UID );
			dto.setAccount( account );
			dto.setPassword( password );
			dao.commit( dto );
		}
		
		return dto.getId();
	}
	
	/**
	 * 根据账号密码 获取UID
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public String getUID( String account, String password ) throws Exception {
		
		UserDao dao 		= SqlUtil.getUserDao();
		
		String sql			= new Condition( UserDto.accountChangeSql(account) ).AND( UserDto.passwordChangeSql(password) ).toString();
		List<UserDto> dtos 	= dao.getByExact( sql );
		dao.commit();
		
		if( dtos.isEmpty() )
			throw new Exception( ErrorCode.AORP_ERROR.name() );
		
		// 默认获取第一个
		UserDto dto = dtos.get(0);
		return dto.getId();
	}
	
	/**
	 * 根据账号密码 创建一个用户 
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public String create(String account, String password) throws Exception {
		
		UserDao dao = SqlUtil.getUserDao();
		// 先获取 看是不是有该账号了
		String sql			= new Condition( UserDto.accountChangeSql( account ) ).toString();
		List<UserDto> dtos 	= dao.getByExact( sql );
		if( !dtos.isEmpty() ){
			dao.commit();
			throw new Exception( ErrorCode.ACCOUNT_EXIST.name() );
		}
		// 先获取 唯一ID
		String UID = generateUID( account, dao );
		
		// 在数据库 创建一个用户
		UserDto dto = dao.create();
		dto.setId( UID );
		dto.setAccount( account );
		dto.setPassword( password );
		
		dao.commit( dto );
		return dto.getId();
	}

	/**
	 * 获取用户 最后一次登录的服务器ID
	 * @param uID
	 * @return
	 */
	public short getLastGsid( String uID ) {
		UserDao dao = SqlUtil.getUserDao();
		UserDto dto = dao.get(uID);
		dao.commit();
		return dto == null ? 0 : dto.getLastGsid();
	}
	

	/**
	 * 保存 用户最后一次登录的服务器ID
	 * @param uid
	 * @param gsid
	 */
	public void updateLastGsid( String uid, short gsid ) {
		UserDao dao = SqlUtil.getUserDao();
		UserDto dto = dao.update();
		dto.setId(uid);
		dto.setLastGsid(gsid);
		dao.commit(dto);
	}
	
	
	/**
	 * 生成一个 唯一ID
	 * @param uin 唯一标示
	 * @param dao 数据库
	 * @return
	 */
	private String generateUID( String uin, UserDao dao ) {
		long cid = CRC.update_9( uin );
		while( dao.get( String.valueOf( cid ) ) != null ){
			if( ++cid >= 1000000000 )
				cid = 100000000;
		}
		return String.valueOf( cid );
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		try {
			System.out.println( o.getLastGsid( "331058520" ) );
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
	}






	
	
	
}
