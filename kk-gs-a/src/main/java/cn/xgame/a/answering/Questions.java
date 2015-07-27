package cn.xgame.a.answering;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Question;

/**
 * 提问
 * @author deng		
 * @date 2015-7-25 下午1:09:08
 */
public class Questions {

	private final Question template;
	
	public Questions( int id ){
		template = CsvGen.getQuestion(id);
	}
	
	
	public Question template(){ return template; }
	public int getNid(){ return template.id; }
	
}
