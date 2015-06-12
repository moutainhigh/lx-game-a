package cn.xgame.user;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;

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
	
	
	public static void main(String[] args) {
		
		String UID = null;
		ErrorCode code = null;
		try {
			
			UID = o.getUID( "dxf", "1111" );
			code = ErrorCode.SUCCEED;
			
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		System.out.println( "UID=" + UID );
		System.out.println( "code=" + code.getDesc() );
	}
	
	
}
