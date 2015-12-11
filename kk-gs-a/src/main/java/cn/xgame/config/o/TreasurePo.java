package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TreasurePo {	public final int id;	public final String content;	public final String needres;

	public TreasurePo( TreasurePo clone ){		this.id = clone.id;		this.content = clone.content;		this.needres = clone.needres;

	}	public TreasurePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		content = data.get("content");		needres = data.get("needres");

	}}