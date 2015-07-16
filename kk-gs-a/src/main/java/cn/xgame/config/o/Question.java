package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Question {	public final int id;	public final String name;	public final String des;	public final String eff;

	public Question( Question clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.eff = clone.eff;

	}	public Question( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		eff = data.get("eff");

	}}