package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Build_treasurePo {	public final int id;	public final String content;	public final byte number;

	public Build_treasurePo( Build_treasurePo clone ){		this.id = clone.id;		this.content = clone.content;		this.number = clone.number;

	}	public Build_treasurePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		content = data.get("content");		number = Byte.parseByte( StringUtil.convertNumberString( data.get("number") ) );

	}}