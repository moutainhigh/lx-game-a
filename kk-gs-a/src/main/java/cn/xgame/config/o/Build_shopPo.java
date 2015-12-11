package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Build_shopPo {	public final int id;	public final int minsize;	public final int maxsize;	public final String content;

	public Build_shopPo( Build_shopPo clone ){		this.id = clone.id;		this.minsize = clone.minsize;		this.maxsize = clone.maxsize;		this.content = clone.content;

	}	public Build_shopPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		minsize = Integer.parseInt( StringUtil.convertNumberString( data.get("minsize") ) );		maxsize = Integer.parseInt( StringUtil.convertNumberString( data.get("maxsize") ) );		content = data.get("content");

	}}