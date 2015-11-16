package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class ReclaimPo {	public final int id;	public final String name;	public final String des;	public final String icon;	public final int money;	public final int room;	public final byte techlv;	public final byte level;

	public ReclaimPo( ReclaimPo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.icon = clone.icon;		this.money = clone.money;		this.room = clone.room;		this.techlv = clone.techlv;		this.level = clone.level;

	}	public ReclaimPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		icon = data.get("icon");		money = Integer.parseInt( StringUtil.convertNumberString( data.get("money") ) );		room = Integer.parseInt( StringUtil.convertNumberString( data.get("room") ) );		techlv = Byte.parseByte( StringUtil.convertNumberString( data.get("techlv") ) );		level = Byte.parseByte( StringUtil.convertNumberString( data.get("level") ) );

	}}