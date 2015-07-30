package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class MeetPo {	public final int id;	public final String ships;	public final byte count;	public final String lvs;	public final String equips;	public final String rewards;

	public MeetPo( MeetPo clone ){		this.id = clone.id;		this.ships = clone.ships;		this.count = clone.count;		this.lvs = clone.lvs;		this.equips = clone.equips;		this.rewards = clone.rewards;

	}	public MeetPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		ships = data.get("ships");		count = Byte.parseByte( StringUtil.convertNumberString( data.get("count") ) );		lvs = data.get("lvs");		equips = data.get("equips");		rewards = data.get("rewards");

	}}