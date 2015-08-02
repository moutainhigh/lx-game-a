package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class AnswerPo {	public final int id;	public final String name;	public final String des;	public final String icon;	public final String asking;

	public AnswerPo( AnswerPo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.icon = clone.icon;		this.asking = clone.asking;

	}	public AnswerPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		icon = data.get("icon");		asking = data.get("asking");

	}}