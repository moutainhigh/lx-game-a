package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class PalyerlevelPo {	public final int id;	public final String name;	public final int exp;

	public PalyerlevelPo( PalyerlevelPo clone ){		this.id = clone.id;		this.name = clone.name;		this.exp = clone.exp;

	}	public PalyerlevelPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		exp = Integer.parseInt( StringUtil.convertNumberString( data.get("exp") ) );

	}}