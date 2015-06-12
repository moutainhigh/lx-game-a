package cn.xgame.a.system;

/**
 * 平台
 * @author deng		
 * @date 2015-6-12 下午12:02:58
 */
public enum ESB {
	
	// 苹果
	APPLE, 
	
	// 安卓
	ANDROID;
	
	public static ESB fromNum( int n ){
		for( ESB a : values() ){
			if( a.ordinal() == n )
				return a;
		}
		return null;
	}
}
