package cn.xgame.a.combat;

import java.util.List;

import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.combat.o.Respond;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AnswerPo;
import cn.xgame.config.o.AskingPo;
import cn.xgame.utils.Logs;

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

	/**
	 * 塞入问
	 * @param asking
	 * @param askings
	 */
	public static void putAsking(String askingStr, List<Askings> askings) {
		String[] ask = askingStr.split(";");
		for( String x : ask ){
			if( x.isEmpty() ) continue;
			AskingPo asking	= CsvGen.getAskingPo( Integer.parseInt( x ) );
			if( asking == null ) continue;
			Askings o = new Askings(asking);
			askings.add(o);
		}
	}
	
	/**
	 * 塞入基础属性
	 * @param attacks
	 * @param defends
	 * @param type
	 * @param value
	 */
	public static void putBasisProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends, String type, String value) {
		String[] types 	= type.split(";");
		String[] values = value.split(";");
		if( types.length != values.length ){
			Logs.error( "types.length != values.length at EquipControl.warpFightProperty" );
			return;
		}
		
		for( int i = 0; i < types.length; i++ ){
			AtkAndDef o = new AtkAndDef(  );
			o.type 		= Integer.parseInt( types[i] );
			o.value 	= Integer.parseInt( values[i] );
			if( o.type < 100 ) // 代表攻击
				attacks.add(o);
			else if( o.type >= 100 && o.type < 200 ) // 代表防御
				defends.add(o);
		}
	}

	
}
