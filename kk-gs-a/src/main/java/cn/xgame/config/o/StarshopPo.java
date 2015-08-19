package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class StarshopPo {	public final int id;	public final String item;

	public StarshopPo( StarshopPo clone ){		this.id = clone.id;		this.item = clone.item;

	}	public StarshopPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		item = data.get("item");

	}}