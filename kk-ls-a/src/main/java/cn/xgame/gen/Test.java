package cn.xgame.gen;


import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.UserDao;
import cn.xgame.gen.dto.MysqlGen.UserDto;
import x.javaplus.mysql.db.Dbcp;


public class Test {

	public static void main(String[] args) {
		
		/////////// 只获取数据 /////////////
		UserDao dao = SqlUtil.getUserDao();
		UserDto dto = dao.get( "101" );
//		List<UserDto> dto = dao.getByExact( UserDto.idChangeSql("1") );
		System.out.println( dto.toString() );
		dao.commit( );
		/////////// 获取数据 ////////////////
//		RankDao rd	= SqlUtil.getRankDao();
//		String sql 	= new Condition( RankDto.nameChangeSql( "a" ) ).toString();
//		String sql 	= "(" + RankDto.idChangeSql( "1" ) + " and " + RankDto.nameChangeSql( "a" ) + ") or " + RankDto.gradChangeSql( 20 );
//		List<RankDto> rt = rd.getByExact( sql );
//		for( RankDto o : rt )
//			System.out.println( o.toString() );
//		rd.commit( ); 
		///////////////////////////
//		RoleDao dao = SqlUtil.getRoleDao();
//		RoleDto n = dao.create();
//		n.setId( "1008" );
//		n.setName( "大峰哥100" );
//		dao.commit( n );
		///////////////////////
//		RoleDao dao = SqlUtil.getRoleDao();
//		RoleDto g = dao.update();
////		String sql 	= new Condition( RoleDto.idChangeSql( "1005" ) ).AND( RoleDto.levelChangeSql( 99 ) ).toString();
////		RoleDto g = dao.updateByExact( RoleDto.levelChangeSql( 99 ) );
//		g.setId( "1007" );
//		g.setName( "大峰哥1007" );
//		g.setA4( (float) 1231.012 );
//		g.setA5( (short) 12 );
//		g.setA6( (byte) 1 );
//		g.setB( new byte[]{1,2,3} );
//		dao.commit( g );
		
//		String sql 	= new Condition( RoleDto.idChangeSql( "1001" ) ).AND( RoleDto.levelChangeSql( 110 ) ).toString();
//		RoleDao dao = SqlUtil.getRoleDao();
////		dao.delete( "1004" );
//		dao.deleteByExact( sql );
//		dao.commit();
		
//		UserDao dao = SqlUtil.getUserDao();
//		UserDto dto = dao.create();
//		
//		dto.setId( "101" );
//		dto.setAccount( "dxf" );
//		dto.setPassword( "1111" );
//		
//		dao.commit(dto);
		
		Dbcp.print();
	}
}
