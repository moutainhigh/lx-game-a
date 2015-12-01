package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TaskcndPo {	public final int id;	public final String des;	public final byte type;	public final int starid;	public final int target;	public final String needitem;	public final String talk;	public final int dialog;

	public TaskcndPo( TaskcndPo clone ){		this.id = clone.id;		this.des = clone.des;		this.type = clone.type;		this.starid = clone.starid;		this.target = clone.target;		this.needitem = clone.needitem;		this.talk = clone.talk;		this.dialog = clone.dialog;

	}	public TaskcndPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		des = data.get("des");		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		starid = Integer.parseInt( StringUtil.convertNumberString( data.get("starid") ) );		target = Integer.parseInt( StringUtil.convertNumberString( data.get("target") ) );		needitem = data.get("needitem");		talk = data.get("talk");		dialog = Integer.parseInt( StringUtil.convertNumberString( data.get("dialog") ) );

	}}