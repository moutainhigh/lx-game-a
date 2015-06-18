package cn.xgame.a.player.bag;

import cn.xgame.a.player.prop.IProp;
import cn.xgame.a.player.prop.captain.Captain;

public class IBag {

	protected byte type;
	
	
	protected IProp newProp( ){
		
		switch( type ){
		case 3:
			return new Captain();
		}
		
		return null;
	}
	
}
