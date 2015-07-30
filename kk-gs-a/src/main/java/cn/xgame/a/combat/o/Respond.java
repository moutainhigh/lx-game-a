package cn.xgame.a.combat.o;

/**
 * 回答结构
 * @author deng		
 * @date 2015-7-30 上午11:12:04
 */
public class Respond extends Asking{

	
	public float getValue( float value ){
		if( intvalue != 0 )
			value += intvalue;
		if( pctvalue != 0 )
			value *= pctvalue;
		return value;
	}
	
}
