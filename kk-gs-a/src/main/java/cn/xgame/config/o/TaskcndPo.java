package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TaskcndPo {	public final int id;	public final String des;	public final byte type;	public final String value;

	public TaskcndPo( TaskcndPo clone ){		this.id = clone.id;		this.des = clone.des;		this.type = clone.type;		this.value = clone.value;

	}	public TaskcndPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		des = data.get("des");		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		value = data.get("value");

	}}