package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Material {	public final int id;	public final String name;	public final byte tpye;

	public Material( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		tpye = Byte.parseByte( StringUtil.convertNumberString( data.get("tpye") ) );

	}}