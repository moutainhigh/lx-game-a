package cn.xgame.a.combat.o;

import cn.xgame.config.o.AskingPo;


/**
 * 回答结构
 * @author deng		
 * @date 2015-7-30 上午11:12:04
 */
public class Respond extends Askings{

	
	public Respond(AskingPo asking) {
		super(asking);
	}

	public float getValue( float value ){
		if( intvalue != 0 )
			value += intvalue;
		if( pctvalue != 0 )
			value *= pctvalue;
		return value;
	}
	
}
