package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Notice {	public final int id;	public final byte type;	public final String stateicon;	public final String text;

	public Notice( Notice clone ){		this.id = clone.id;		this.type = clone.type;		this.stateicon = clone.stateicon;		this.text = clone.text;

	}	public Notice( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		stateicon = data.get("stateicon");		text = data.get("text");

	}}