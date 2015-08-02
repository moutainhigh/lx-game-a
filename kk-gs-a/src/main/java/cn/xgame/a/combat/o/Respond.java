package cn.xgame.a.combat.o;



/**
 * 回答结构
 * @author deng		
 * @date 2015-7-30 上午11:12:04
 */
public class Respond {

	// 表格ID
	public int id;
	
	// 对应值
	public float intvalue;
	
	// 对应值
	public float pctvalue;
	
	public Respond( int id, float intvalue, float pctvalue ) {
		this.id 		= id;
		this.intvalue 	= intvalue;
		this.pctvalue 	= pctvalue;
	}

	public float getValue( float value ){
		if( intvalue != 0 )
			value += intvalue;
		if( pctvalue != 0 )
			value *= pctvalue;
		return value;
	}
	
}
