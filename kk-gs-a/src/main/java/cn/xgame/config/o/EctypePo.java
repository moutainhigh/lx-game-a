package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class EctypePo {	public final int id;	public final String reward;	public final String money;	public final int btime;	public final short maxran;	public final String atkvalue;	public final String defvalue;	public final int hp;

	public EctypePo( EctypePo clone ){		this.id = clone.id;		this.reward = clone.reward;		this.money = clone.money;		this.btime = clone.btime;		this.maxran = clone.maxran;		this.atkvalue = clone.atkvalue;		this.defvalue = clone.defvalue;		this.hp = clone.hp;

	}	public EctypePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		reward = data.get("reward");		money = data.get("money");		btime = Integer.parseInt( StringUtil.convertNumberString( data.get("btime") ) );		maxran = Short.parseShort( StringUtil.convertNumberString( data.get("maxran") ) );		atkvalue = data.get("atkvalue");		defvalue = data.get("defvalue");		hp = Integer.parseInt( StringUtil.convertNumberString( data.get("hp") ) );

	}}