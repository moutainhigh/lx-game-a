package cn.xgame.a.world.planet.data.ectype;

/**
 * 星球的一个副本
 * @author deng		
 * @date 2015-7-27 下午3:46:14
 */
public class SEctype {

	// 所属星球ID
	private int snid;
	
	// 副本表格ID
	private int enid;
	
	public SEctype( int snid, int enid ){
		this.snid = snid;
		this.enid = enid;
	}

	public int getSnid() {
		return snid;
	}
	public int getEnid() {
		return enid;
	}
	
	
}
