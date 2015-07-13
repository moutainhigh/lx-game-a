package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Ectypelist {	public final int id;	public final String list;

	public Ectypelist( Ectypelist clone ){		this.id = clone.id;		this.list = clone.list;

	}	public Ectypelist( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		list = data.get("list");

	}}