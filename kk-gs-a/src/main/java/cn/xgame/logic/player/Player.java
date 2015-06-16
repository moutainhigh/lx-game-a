package cn.xgame.logic.player;

import cn.xgame.net.event.Events;
import cn.xgame.utils.PackageCheck;

public class Player extends IPlayer{

	
	private PackageCheck pcheck = new PackageCheck();
	
	
	
	public String getNickname() {
		return null;
	}

	public boolean safeCheck(Events event) {
		return pcheck.safeCheck( event );
	}
	
	

}
