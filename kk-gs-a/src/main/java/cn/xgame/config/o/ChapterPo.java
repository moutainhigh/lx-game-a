package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class ChapterPo {	public final int id;	public final String name;	public final String des;	public final String icon;	public final int time;	public final byte times;	public final String node;

	public ChapterPo( ChapterPo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.icon = clone.icon;		this.time = clone.time;		this.times = clone.times;		this.node = clone.node;

	}	public ChapterPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		icon = data.get("icon");		time = Integer.parseInt( StringUtil.convertNumberString( data.get("time") ) );		times = Byte.parseByte( StringUtil.convertNumberString( data.get("times") ) );		node = data.get("node");

	}}