package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class ChapterPo {	public final int id;	public final byte times;	public final String node;

	public ChapterPo( ChapterPo clone ){		this.id = clone.id;		this.times = clone.times;		this.node = clone.node;

	}	public ChapterPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		times = Byte.parseByte( StringUtil.convertNumberString( data.get("times") ) );		node = data.get("node");

	}}