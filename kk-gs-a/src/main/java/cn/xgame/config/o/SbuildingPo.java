package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class SbuildingPo {	public final int id;	public final String name;	public final String intro;	public final String des;	public final String icon;	public final String model;	public final byte modelcd;	public final byte level;	public final String iconbg;	public final String needres;	public final int needtime;	public final short type;	public final byte usegrid;	public final int ProduceValue;	public final String ProduceTpye;	public final int ram;	public final byte buildtype;	public final String front;	public final String technology;	public final String mutex;	public final String showtype;

	public SbuildingPo( SbuildingPo clone ){		this.id = clone.id;		this.name = clone.name;		this.intro = clone.intro;		this.des = clone.des;		this.icon = clone.icon;		this.model = clone.model;		this.modelcd = clone.modelcd;		this.level = clone.level;		this.iconbg = clone.iconbg;		this.needres = clone.needres;		this.needtime = clone.needtime;		this.type = clone.type;		this.usegrid = clone.usegrid;		this.ProduceValue = clone.ProduceValue;		this.ProduceTpye = clone.ProduceTpye;		this.ram = clone.ram;		this.buildtype = clone.buildtype;		this.front = clone.front;		this.technology = clone.technology;		this.mutex = clone.mutex;		this.showtype = clone.showtype;

	}	public SbuildingPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		intro = data.get("intro");		des = data.get("des");		icon = data.get("icon");		model = data.get("model");		modelcd = Byte.parseByte( StringUtil.convertNumberString( data.get("modelcd") ) );		level = Byte.parseByte( StringUtil.convertNumberString( data.get("level") ) );		iconbg = data.get("iconbg");		needres = data.get("needres");		needtime = Integer.parseInt( StringUtil.convertNumberString( data.get("needtime") ) );		type = Short.parseShort( StringUtil.convertNumberString( data.get("type") ) );		usegrid = Byte.parseByte( StringUtil.convertNumberString( data.get("usegrid") ) );		ProduceValue = Integer.parseInt( StringUtil.convertNumberString( data.get("ProduceValue") ) );		ProduceTpye = data.get("ProduceTpye");		ram = Integer.parseInt( StringUtil.convertNumberString( data.get("ram") ) );		buildtype = Byte.parseByte( StringUtil.convertNumberString( data.get("buildtype") ) );		front = data.get("front");		technology = data.get("technology");		mutex = data.get("mutex");		showtype = data.get("showtype");

	}}