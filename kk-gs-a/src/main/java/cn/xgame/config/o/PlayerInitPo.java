package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class PlayerInitPo {	public final int currency;	public final int gold;	public final String item;	public final String ship;	public final String captain;

	public PlayerInitPo( PlayerInitPo clone ){		this.currency = clone.currency;		this.gold = clone.gold;		this.item = clone.item;		this.ship = clone.ship;		this.captain = clone.captain;

	}	public PlayerInitPo( Map<String, String> data ){		currency = Integer.parseInt( StringUtil.convertNumberString( data.get("currency") ) );		gold = Integer.parseInt( StringUtil.convertNumberString( data.get("gold") ) );		item = data.get("item");		ship = data.get("ship");		captain = data.get("captain");

	}}