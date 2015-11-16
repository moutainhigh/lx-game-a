package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Equip_chipPo {	public final int id;	public final int control;	public final int perception;	public final int affinity;	public final String answers;	public final String askings;

	public Equip_chipPo( Equip_chipPo clone ){		this.id = clone.id;		this.control = clone.control;		this.perception = clone.perception;		this.affinity = clone.affinity;		this.answers = clone.answers;		this.askings = clone.askings;

	}	public Equip_chipPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		control = Integer.parseInt( StringUtil.convertNumberString( data.get("control") ) );		perception = Integer.parseInt( StringUtil.convertNumberString( data.get("perception") ) );		affinity = Integer.parseInt( StringUtil.convertNumberString( data.get("affinity") ) );		answers = data.get("answers");		askings = data.get("askings");

	}}