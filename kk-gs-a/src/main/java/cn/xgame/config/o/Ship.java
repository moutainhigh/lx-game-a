package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Ship {	public final int id;	public final String name;	public final int room;	public final int en;	public final int hp;	public final int mess;	public final String answer;

	public Ship( Ship clone ){		this.id = clone.id;		this.name = clone.name;		this.room = clone.room;		this.en = clone.en;		this.hp = clone.hp;		this.mess = clone.mess;		this.answer = clone.answer;

	}	public Ship( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		name = data.get("name");		room = Integer.parseInt( StringUtil.convertNumberString( data.get("room") ) );		en = Integer.parseInt( StringUtil.convertNumberString( data.get("en") ) );		hp = Integer.parseInt( StringUtil.convertNumberString( data.get("hp") ) );		mess = Integer.parseInt( StringUtil.convertNumberString( data.get("mess") ) );		answer = data.get("answer");

	}}