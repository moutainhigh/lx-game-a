package cn.xgame.a.fighter.o;

import java.util.List;

import cn.xgame.config.o.AnswerPo;


import x.javaplus.collections.Lists;

/**
 * 答结构
 * @author deng		
 * @date 2015-7-30 上午11:10:58
 */
public class Answers {

	// 表格ID
	public int id;
	
	// 权重值
	public int heavy;
	
	// 要回答的问列表
	public List<Respond> responds = Lists.newArrayList();
	
	public Answers( AnswerPo answer ) {
		id = answer.id;
		heavy = answer.heavy;
		initRespond( answer.asking );
	}

	private void initRespond(String asking) {
		if( asking.isEmpty() )
			return ;
		String[] array = asking.split( "\\|" );
		for( String str : array ){
			String[] x = str.split(";");
			int id = Integer.parseInt( x[0] );
			int intvalue = Integer.parseInt( x[1] );
			float pctvalue = Float.parseFloat( x[2] );
			responds.add( new Respond(id, intvalue, pctvalue) );
		}
	}

	public Respond getRespond( int id ){
		for( Respond r : responds ){
			if( r.id == id )
				return r;
		}
		return null;
	}
	
}
