package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class RquestionPo {	public final int ID;	public final String question;

	public RquestionPo( RquestionPo clone ){		this.ID = clone.ID;		this.question = clone.question;

	}	public RquestionPo( Map<String, String> data ){		ID = Integer.parseInt( StringUtil.convertNumberString( data.get("ID") ) );		question = data.get("question");

	}}