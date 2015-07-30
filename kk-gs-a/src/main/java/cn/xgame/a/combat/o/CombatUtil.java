package cn.xgame.a.combat.o;

import java.util.List;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AnswerPo;
import cn.xgame.config.o.AskingPo;

/**
 * 战斗工具类
 * @author deng		
 * @date 2015-7-30 下午9:23:03
 */
public class CombatUtil {

	/**
	 * 塞入答
	 * @param answerStr
	 * @param answers
	 */
	public static void putAnswer( String answerStr, List<Answers> answers ) {
		String[] ans = answerStr.split(";");
		for( String x : ans ){
			int ansId = Integer.parseInt( x );
			AnswerPo answer = CsvGen.getAnswerPo( ansId );
			if( answer == null ) continue;
			
			Answers o 	= new Answers( answer.id );
			String[] v 	= answer.asking.split("\\|");
			for( String ask : v ){
				if( ask.isEmpty() ) continue;
				String[] a 		= ask.split(";");
				AskingPo asking	= CsvGen.getAskingPo( Integer.parseInt( a[0] ) );
				if( asking == null ) continue;
				
				Respond respond 	= new Respond( asking );
				respond.intvalue 	= Float.parseFloat( a[1] );
				respond.pctvalue 	= Float.parseFloat( a[2] );
				
				o.responds.add(respond);
			}
			answers.add(o);
		}
	}
	
}
