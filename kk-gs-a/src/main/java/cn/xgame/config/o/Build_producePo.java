package cn.xgame.config.o;import java.util.Map;import x.javaplus.string.StringUtil;public class Build_producePo {	public final int id;	public final float ProduceValue;	public final int producttime;	public final String ProduceTpye;	public final int ram;

	public Build_producePo( Build_producePo clone ){		this.id = clone.id;		this.ProduceValue = clone.ProduceValue;		this.producttime = clone.producttime;		this.ProduceTpye = clone.ProduceTpye;		this.ram = clone.ram;

	}	public Build_producePo( Map<String, String> data ){		id = Integer.parseInt( StringUtil.convertNumberString( data.get("id") ) );		ProduceValue = Float.parseFloat( StringUtil.convertNumberString( data.get("ProduceValue") ) );		producttime = Integer.parseInt( StringUtil.convertNumberString( data.get("producttime") ) );		ProduceTpye = data.get("ProduceTpye");		ram = Integer.parseInt( StringUtil.convertNumberString( data.get("ram") ) );

	}}