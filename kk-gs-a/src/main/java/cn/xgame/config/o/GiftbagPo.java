package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class GiftbagPo {	public final int id;	public final byte israndom;	public final int minsize;	public final int maxsize;	public final String content;	public final String needres;

	public GiftbagPo( GiftbagPo clone ){		this.id = clone.id;		this.israndom = clone.israndom;		this.minsize = clone.minsize;		this.maxsize = clone.maxsize;		this.content = clone.content;		this.needres = clone.needres;

	}	public GiftbagPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		israndom = Byte.parseByte( StringUtil.convertNumberString( data.get("israndom") ) );		minsize = Integer.parseInt( StringUtil.convertNumberString( data.get("minsize") ) );		maxsize = Integer.parseInt( StringUtil.convertNumberString( data.get("maxsize") ) );		content = data.get("content");		needres = data.get("needres");

	}}