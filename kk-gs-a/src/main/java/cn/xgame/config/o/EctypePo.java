package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class EctypePo {	public final int id;	public final String name;	public final String des;	public final String icon;	public final byte quality;	public final String reward;	public final String money;	public final String eanswers;	public final byte type;	public final String etime;	public final byte level;	public final String ranswers;	public final String ianswers;	public final String meetid;	public final int btime;	public final short maxran;	public final String silverpond;	public final String manswers;	public final String goldenpond;

	public EctypePo( EctypePo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.icon = clone.icon;		this.quality = clone.quality;		this.reward = clone.reward;		this.money = clone.money;		this.eanswers = clone.eanswers;		this.type = clone.type;		this.etime = clone.etime;		this.level = clone.level;		this.ranswers = clone.ranswers;		this.ianswers = clone.ianswers;		this.meetid = clone.meetid;		this.btime = clone.btime;		this.maxran = clone.maxran;		this.silverpond = clone.silverpond;		this.manswers = clone.manswers;		this.goldenpond = clone.goldenpond;

	}	public EctypePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		icon = data.get("icon");		quality = Byte.parseByte( StringUtil.convertNumberString( data.get("quality") ) );		reward = data.get("reward");		money = data.get("money");		eanswers = data.get("eanswers");		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		etime = data.get("etime");		level = Byte.parseByte( StringUtil.convertNumberString( data.get("level") ) );		ranswers = data.get("ranswers");		ianswers = data.get("ianswers");		meetid = data.get("meetid");		btime = Integer.parseInt( StringUtil.convertNumberString( data.get("btime") ) );		maxran = Short.parseShort( StringUtil.convertNumberString( data.get("maxran") ) );		silverpond = data.get("silverpond");		manswers = data.get("manswers");		goldenpond = data.get("goldenpond");

	}}