package cn.xgame.gen.o;

public interface User {

	String id();
	
	/**  账号 */
	String account();
	
	/**  密码 */
	String password();
	
	/**  最后一次登录的服务器ID */
	short lastGsid();
}
