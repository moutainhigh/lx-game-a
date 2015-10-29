package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TaskPo {	public final int id;	public final String name;	public final String des;	public final String details;	public final String icon;	public final int needlv;	public final int needsite;	public final String completecnd;	public final byte type;	public final String award;	public final int exp;	public final String answer;	public final byte times;	public final int timelimit;	public final int next;

	public TaskPo( TaskPo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.details = clone.details;		this.icon = clone.icon;		this.needlv = clone.needlv;		this.needsite = clone.needsite;		this.completecnd = clone.completecnd;		this.type = clone.type;		this.award = clone.award;		this.exp = clone.exp;		this.answer = clone.answer;		this.times = clone.times;		this.timelimit = clone.timelimit;		this.next = clone.next;

	}	public TaskPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		details = data.get("details");		icon = data.get("icon");		needlv = Integer.parseInt( StringUtil.convertNumberString( data.get("needlv") ) );		needsite = Integer.parseInt( StringUtil.convertNumberString( data.get("needsite") ) );		completecnd = data.get("completecnd");		type = Byte.parseByte( StringUtil.convertNumberString( data.get("type") ) );		award = data.get("award");		exp = Integer.parseInt( StringUtil.convertNumberString( data.get("exp") ) );		answer = data.get("answer");		times = Byte.parseByte( StringUtil.convertNumberString( data.get("times") ) );		timelimit = Integer.parseInt( StringUtil.convertNumberString( data.get("timelimit") ) );		next = Integer.parseInt( StringUtil.convertNumberString( data.get("next") ) );

	}}