package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Answers {	public final int id;	public final String name;	public final String des;	public final String answer;

	public Answers( Answers clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.answer = clone.answer;

	}	public Answers( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		answer = data.get("answer");

	}}