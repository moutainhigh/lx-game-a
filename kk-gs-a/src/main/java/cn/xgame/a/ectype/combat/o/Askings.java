package cn.xgame.a.ectype.combat.o;

import cn.xgame.config.o.AskingPo;


/**
 * 问结构
 * @author deng		
 * @date 2015-7-30 上午10:41:29
 */
public class Askings {

	// 表格ID
	public int id;
	
	// 类型 
	public byte type;
	
	// 小类型
	public byte atttype;
	
	// 对应值
	public float intvalue;
	
	// 对应值
	public float pctvalue;
	
	public Askings(AskingPo asking) {
		id 			= asking.id;
		type 		= asking.type;
		atttype 	= asking.atttype;
		intvalue 	= asking.intvalue;
		pctvalue 	= asking.pctvalue;
	}

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
