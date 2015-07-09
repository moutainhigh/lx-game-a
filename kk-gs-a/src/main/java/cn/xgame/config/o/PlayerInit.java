package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class PlayerInit {	public final int currency;	public final int gold;	public final String item;	public final String ship;	public final String captain;

	public PlayerInit( PlayerInit clone ){		this.currency = clone.currency;		this.gold = clone.gold;		this.item = clone.item;		this.ship = clone.ship;		this.captain = clone.captain;

	}	public PlayerInit( Map<String, String> data ){		currency = Integer.parseInt( StringUtil.convertNumberString( data.get("currency") ) );		gold = Integer.parseInt( StringUtil.convertNumberString( data.get("gold") ) );		item = data.get("item");		ship = data.get("ship");		captain = data.get("captain");

	}}