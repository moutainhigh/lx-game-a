package cn.xgame.utils;

public class VoteTimeChange {

	public static int conversionTime(byte type) {
		if( type == 1 ){
			return 43200;
		}else if( type == 2 ){
			return 86400;
		}else if( type == 3 ){
			return 172800;
		}
		return 300;
	}
	
}
