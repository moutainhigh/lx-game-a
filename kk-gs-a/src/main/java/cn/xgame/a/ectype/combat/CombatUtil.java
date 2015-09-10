package cn.xgame.a.ectype.combat;

import java.util.List;

import cn.xgame.a.ectype.combat.o.Answers;
import cn.xgame.a.ectype.combat.o.Askings;
import cn.xgame.a.ectype.combat.o.Respond;
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
	 * @param list
	 * @param answers
	 */
	public static void putAnswer( List<Integer> list, List<Answers> answers ) {
		for( int ansId : list ){
			AnswerPo answer = CsvGen.getAnswerPo( ansId );
			if( answer == null ) continue;
			
			Answers o 	= new Answers( answer.id );
			String[] v 	= answer.asking.split("\\|");
			for( String ask : v ){
				if( ask.isEmpty() ) continue;
				String[] a 		= ask.split(";");
				AskingPo asking	= CsvGen.getAskingPo( Integer.parseInt( a[0] ) );
				if( asking == null ) continue;
				
				Respond respond = new Respond( asking.id, Float.parseFloat( a[1] ), Float.parseFloat( a[2] ) );
				o.responds.add(respond);
			}
			answers.add(o);
		}
	}

	/**
	 * 塞入问
	 * @param asking
	 * @param askings
	 */
	public static void putAsking(List<Integer> list, List<Askings> askings) {
		for( int id : list ){
			AskingPo asking	= CsvGen.getAskingPo( id );
			if( asking == null ) continue;
			Askings o = new Askings(asking);
			askings.add(o);
		}
	}
	
	
}
