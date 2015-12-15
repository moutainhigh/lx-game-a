package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TaskPo {	public final int id;	public final String name;	public final String des;	public final String intro;	public final String icon;	public final String guide;	public final int starid;	public final int receivenpc;	public final int completenpc;	public final byte tasktype;	public final byte labeltype;	public final int needlv;	public final int needlast;	public final String course;	public final String award;	public final int money;	public final byte loopcount;	public final int exp;	public final String showtype;	public final int time;	public final int end;	public final int start;	public final String mailwrite;	public final String mailtitle;

	public TaskPo( TaskPo clone ){		this.id = clone.id;		this.name = clone.name;		this.des = clone.des;		this.intro = clone.intro;		this.icon = clone.icon;		this.guide = clone.guide;		this.starid = clone.starid;		this.receivenpc = clone.receivenpc;		this.completenpc = clone.completenpc;		this.tasktype = clone.tasktype;		this.labeltype = clone.labeltype;		this.needlv = clone.needlv;		this.needlast = clone.needlast;		this.course = clone.course;		this.award = clone.award;		this.money = clone.money;		this.loopcount = clone.loopcount;		this.exp = clone.exp;		this.showtype = clone.showtype;		this.time = clone.time;		this.end = clone.end;		this.start = clone.start;		this.mailwrite = clone.mailwrite;		this.mailtitle = clone.mailtitle;

	}	public TaskPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		des = data.get("des");		intro = data.get("intro");		icon = data.get("icon");		guide = data.get("guide");		starid = Integer.parseInt( StringUtil.convertNumberString( data.get("starid") ) );		receivenpc = Integer.parseInt( StringUtil.convertNumberString( data.get("receivenpc") ) );		completenpc = Integer.parseInt( StringUtil.convertNumberString( data.get("completenpc") ) );		tasktype = Byte.parseByte( StringUtil.convertNumberString( data.get("tasktype") ) );		labeltype = Byte.parseByte( StringUtil.convertNumberString( data.get("labeltype") ) );		needlv = Integer.parseInt( StringUtil.convertNumberString( data.get("needlv") ) );		needlast = Integer.parseInt( StringUtil.convertNumberString( data.get("needlast") ) );		course = data.get("course");		award = data.get("award");		money = Integer.parseInt( StringUtil.convertNumberString( data.get("money") ) );		loopcount = Byte.parseByte( StringUtil.convertNumberString( data.get("loopcount") ) );		exp = Integer.parseInt( StringUtil.convertNumberString( data.get("exp") ) );		showtype = data.get("showtype");		time = Integer.parseInt( StringUtil.convertNumberString( data.get("time") ) );		end = Integer.parseInt( StringUtil.convertNumberString( data.get("end") ) );		start = Integer.parseInt( StringUtil.convertNumberString( data.get("start") ) );		mailwrite = data.get("mailwrite");		mailtitle = data.get("mailtitle");

	}}