package cn.xgame.a.combat.o;

/**
 * 问结构
 * @author deng		
 * @date 2015-7-30 上午10:41:29
 */
public class Asking {

	// 表格ID
	public int id;
	
	// 结算类型 1.战斗前 2.战斗中 3.战斗后
	public byte settype;
	
	// 类型 
	public byte type;
	
	// 小类型
	public byte atttype;
	
	// 对应值
	public float intvalue;
	
	// 对应值
	public float pctvalue;
	
	/**
	 * 改变值
	 * @param value
	 * @return
	 */
	public float getValue( float value ){
		if( intvalue != 0 )
			value += intvalue;
		if( pctvalue != 0 )
			value *= pctvalue;
		return value;
	}
	
}
