package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Stardomain {	public final short id;	public final String name;	public final String include;

	public Stardomain( Map<String, String> data ){		id = Short.parseShort( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		include = data.get("include");

	}}