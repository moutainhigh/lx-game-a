package cn.xgame.logic.user;


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
	 * 根据账号密码 获取UID
	 * @param account
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public String getUID( String account, String password ) throws Exception {
		
		UserDao dao 		= SqlUtil.getUserDao();
		
		String sql			= new Condition( UserDto.accountChangeSql( account ) ).AND(UserDto.passwordChangeSql(password) ).toString();
		List<UserDto> dtos 	= dao.getByExact( sql );
		if( dtos.isEmpty() )
			throw new Exception( ErrorCode.AORP_ERROR.name() );
		
		// 默认获取第一个
		UserDto dto = dtos.get(0);
		
		dao.commit();
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
		if( !dtos.isEmpty() )
			throw new Exception( ErrorCode.ACCOUNT_EXIST.name() );
		
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
			o.create( "test4", "111" );
		} catch (Exception e) {
			System.out.println( e.getMessage() );
		}
	}

	
	
	
}
