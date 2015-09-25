package cn.xgame.a.player.manor.o;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;

/**
 * 玩家领地建筑物
 * @author deng		
 * @date 2015-9-25 上午10:55:57
 */
public class Bbuilding {
	
	private final BbuildingPo templet ;
	
	// 建筑物位置
	private byte index;
	
	public Bbuilding( int id ){
		templet = CsvGen.getBbuildingPo(id);
	}
	
	public BbuildingPo templet(){ return templet; }

	public byte getIndex() {
		return index;
	}

	public void setIndex(byte index) {
		this.index = index;
	}
	
	
}
