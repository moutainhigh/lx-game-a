package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class TaverndataPo {	public final int id;	public final int rand;

	public TaverndataPo( TaverndataPo clone ){		this.id = clone.id;		this.rand = clone.rand;

	}	public TaverndataPo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		rand = Integer.parseInt( StringUtil.convertNumberString( data.get("rand") ) );

	}}