package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Taverndata {	public final int id;	public final int rand;

	public Taverndata( Taverndata clone ){		this.id = clone.id;		this.rand = clone.rand;

	}	public Taverndata( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		rand = Integer.parseInt( StringUtil.convertNumberString( data.get("rand") ) );

	}}