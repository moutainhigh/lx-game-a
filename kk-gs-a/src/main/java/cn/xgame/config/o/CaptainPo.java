package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class CaptainPo {	public final int id;	public final int weekpay;	public final int control;	public final int perception;	public final int affinity;	public final String answer;

	public CaptainPo( CaptainPo clone ){		this.id = clone.id;		this.weekpay = clone.weekpay;		this.control = clone.control;		this.perception = clone.perception;		this.affinity = clone.affinity;		this.answer = clone.answer;

	}	public CaptainPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		weekpay = Integer.parseInt( StringUtil.convertNumberString( data.get("weekpay") ) );		control = Integer.parseInt( StringUtil.convertNumberString( data.get("control") ) );		perception = Integer.parseInt( StringUtil.convertNumberString( data.get("perception") ) );		affinity = Integer.parseInt( StringUtil.convertNumberString( data.get("affinity") ) );		answer = data.get("answer");

	}}