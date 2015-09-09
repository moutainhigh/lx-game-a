package cn.xgame.a.prop.captain;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.config.o.ItemPo;
import cn.xgame.utils.LuaUtil;

/**
 * 舰长属性
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class CaptainAttr extends IProp {

	private final CaptainPo templet;
	
	// 亲密度
	private int curIntimacy = 0;
	
	// 忠诚度
	private int loyalty ;
	
	// 周薪
	private int weekpay;
	
	// 操控
	private int control;
	
	// 感知
	private int perception;
	
	// 亲和力
	private int affinity;
	
	// 应答 - 问
	private List<Integer> askings = Lists.newArrayList();
	// 应答 - 答
	private List<Integer> answers = Lists.newArrayList();
	
	public CaptainAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
		templet = CsvGen.getCaptainPo(nid);
	}

	private CaptainAttr( CaptainAttr clone ) {
		super( clone );
		templet 	= clone.templet;
		curIntimacy = clone.curIntimacy;
		loyalty		= clone.loyalty;
		weekpay 	= clone.weekpay;
		control 	= clone.control;
		perception 	= clone.perception;
		affinity 	= clone.affinity;
		askings.addAll( clone.askings );
		answers.addAll( clone.answers );
	}
	
	@Override
	public CaptainAttr clone() { return new CaptainAttr( this ); }
	
	public CaptainPo templet(){ return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 20 );
		buildTransformStream( buf );
		buf.writeByte( askings.size() );
		for( int i : askings )
			buf.writeInt(i);
		buf.writeByte( answers.size() );
		for( int i : answers )
			buf.writeInt(i);
		return buf.array();
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		curIntimacy = buf.readInt();
		loyalty 	= buf.readInt();
		weekpay 	= buf.readInt();
		control 	= buf.readInt();
		perception 	= buf.readInt();
		affinity 	= buf.readInt();
		byte size 	= buf.readByte();
		for( int i = 0; i < size; i++ )
			askings.add( buf.readInt() );
		size 		= buf.readByte();
		for( int i = 0; i < size; i++ )
			answers.add( buf.readInt() );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( curIntimacy );
		buffer.writeInt( loyalty );
		buffer.writeInt( weekpay );
		buffer.writeInt( control );
		buffer.writeInt( perception );
		buffer.writeInt( affinity );
	}

	@Override
	public void randomAttachAttr() {
		Lua lua = LuaUtil.getGameData();
		LuaValue[] value = lua.getField( "randomAttachAttr" ).call( 4, templet, type().toNumber(), getQuality().toNumber() );
		curIntimacy	= 0;
		loyalty		= 70;
		weekpay 	= value[0].getInt();
		control 	= value[1].getInt();
		perception 	= value[2].getInt();
		affinity 	= value[3].getInt();
	}

	public int getCurIntimacy() {
		return curIntimacy;
	}
	public void setCurIntimacy(int curIntimacy) {
		this.curIntimacy = curIntimacy;
	}
	public int getWeekpay() {
		return weekpay;
	}
	public void setWeekpay(int weekpay) {
		this.weekpay = weekpay;
	}
	public int getControl() {
		return control;
	}
	public void setControl(int control) {
		this.control = control;
	}
	public int getPerception() {
		return perception;
	}
	public void setPerception(int perception) {
		this.perception = perception;
	}
	public int getAffinity() {
		return affinity;
	}
	public void setAffinity(int affinity) {
		this.affinity = affinity;
	}
	public int getLoyalty() {
		return loyalty;
	}
	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}
	public List<Integer> getAskings(){ 
		return askings; 
	}
	public List<Integer> getAnswers(){ 
		return answers; 
	}
	
	
	public void addIntimacy( int value ) {
		curIntimacy += value;
		int maxIntimacy = getMaxIntimacy();
		if( curIntimacy > maxIntimacy ) curIntimacy = maxIntimacy;
	}
	
	/** 获取最大亲密度 */
	public int getMaxIntimacy(){
		if( templet.intimate.isEmpty() )
			return 0;
		String[] str = templet.intimate.split(";");
		return Integer.parseInt( str[str.length-1] );
	}
	
	/** 获取当前阶段 */
	public int getCurphase(){
		if( templet.intimate.isEmpty() )
			return 0;
		String[] str = templet.intimate.split(";");
		int i = 0;
		while( i < str.length ){
			if( curIntimacy < Integer.parseInt( str[i] ) ) break;
			++i;
		}
		return i;
	}
	
	/**
	 * 添加一个应答-问
	 * @param id
	 */
	public void addAsking( int id ){
		if( askings.indexOf(id) == -1 )
			askings.add(id);
	}
	
	/**
	 * 添加一个应答-答
	 * @param id
	 */
	public void addAnswer( int id ){
		if( answers.indexOf(id) == -1 )
			answers.add(id);
	}
	
}
