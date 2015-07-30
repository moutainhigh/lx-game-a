package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Answer {	public final int id;	public final String name;	public final String des;	public final String icon;	public final String asking;	public final int intvalue;	public final float pctvalue;

	public Answer( Answer clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.icon = clone.icon;		this.asking = clone.asking;		this.intvalue = clone.intvalue;		this.pctvalue = clone.pctvalue;

	}	public Answer( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		icon = data.get("icon");		asking = data.get("asking");		intvalue = Integer.parseInt( StringUtil.convertNumberString( data.get("intvalue") ) );		pctvalue = Float.parseFloat( StringUtil.convertNumberString( data.get("pctvalue") ) );

	}}