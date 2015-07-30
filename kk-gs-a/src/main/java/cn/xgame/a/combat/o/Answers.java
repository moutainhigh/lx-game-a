package cn.xgame.a.combat.o;

import java.util.List;


import x.javaplus.collections.Lists;

/**
 * 答结构
 * @author deng		
 * @date 2015-7-30 上午11:10:58
 */
public class Answers {

	// 表格ID
	public int id;
	
	// 要回答的问列表
	public List<Respond> responds = Lists.newArrayList();
	
	public Answers(int id) {
		this.id = id;
	}

	public Respond getRespond( int id ){
		for( Respond r : responds ){
			if( r.id == id )
				return r;
		}
		return null;
	}
	
}
