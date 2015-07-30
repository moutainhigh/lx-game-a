package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class NoticePo {	public final int id;	public final byte type;	public final String stateicon;	public final String text;

	public NoticePo( NoticePo clone ){		this.id = clone.id;		this.type = clone.type;		this.stateicon = clone.stateicon;		this.text = clone.text;

	}	public NoticePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		stateicon = data.get("stateicon");		text = data.get("text");

	}}