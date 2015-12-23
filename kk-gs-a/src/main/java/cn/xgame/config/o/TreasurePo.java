package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TreasurePo {	public final int id;	public final String name;	public final String intro;	public final String des;	public final String icon;	public final String needres;	public final String showtype;	public final String content;

	public TreasurePo( TreasurePo clone ){		this.id = clone.id;		this.name = clone.name;		this.intro = clone.intro;		this.des = clone.des;		this.icon = clone.icon;		this.needres = clone.needres;		this.showtype = clone.showtype;		this.content = clone.content;

	}	public TreasurePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		intro = data.get("intro");		des = data.get("des");		icon = data.get("icon");		needres = data.get("needres");		showtype = data.get("showtype");		content = data.get("content");

	}}