package cn.xgame.a.prop.info;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.classes.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Equip_auxiliaryPo;
import cn.xgame.config.o.ItemPo;
import cn.xgame.utils.LuaUtil;

public class EquipAuxiliaryAttr extends IProp{
	
	private static final int version = 1;
	
	private final Equip_auxiliaryPo templet;
	
	// 当前耐久度
	private int currentDur;
	// 总耐久度
	private int maxDur;
	
	// 消耗能量
	private int energy;
	// 精密度
	private int accuracy;
	// 复杂度
	private int perplexity;
	// 添加生命值
	private int addHp;
	// 推进器
	private int push;
	
	// 应答 - 问
	private List<Integer> askings = Lists.newArrayList();
	// 应答 - 答
	private List<Integer> answers = Lists.newArrayList();
	

	public EquipAuxiliaryAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
		templet 	= CsvGen.getEquip_auxiliaryPo(nid);
	}
	
	private EquipAuxiliaryAttr( EquipAuxiliaryAttr clone ){
		super( clone );
		templet 	= clone.templet;
	}
	
	@Override
	public IProp clone() { return new EquipAuxiliaryAttr(this); }
	
	public Equip_auxiliaryPo templet() { return templet; }

	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( );
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "SAuxiliaryAttr_ToBytes" ).call( 0, version, buf, this );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "SAuxiliaryAttr_WrapBytes" ).call( 0, buf, this );
	}

	@Override
	public void randomAttachAttr() {
		LuaUtil.getGameData().getField( "randomAttachAttr" ).call( 0, this );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( energy );
		buffer.writeInt( accuracy );
		buffer.writeInt( perplexity );
		buffer.writeInt( currentDur );
		buffer.writeInt( maxDur );
		buffer.writeInt( addHp );
		buffer.writeInt( push );
		buffer.writeByte( askings.size() );
		for( int id : askings )
			buffer.writeInt(id);
		buffer.writeByte( answers.size() );
		for( int id : answers )
			buffer.writeInt(id);
	}

	public int getCurrentDur() {
		return currentDur;
	}
	public void setCurrentDur(int currentDur) {
		this.currentDur = currentDur;
	}
	public void addCurrentDur( int value ) {
		this.currentDur += value;
		if( this.currentDur < 0 )
			this.currentDur = 0;
		if( this.currentDur > maxDur )
			this.currentDur = maxDur;
	}
	public int getMaxDur() {
		return maxDur;
	}
	public void setMaxDur(int maxDur) {
		this.maxDur = maxDur;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public int getPerplexity() {
		return perplexity;
	}
	public void setPerplexity(int perplexity) {
		this.perplexity = perplexity;
	}
	public int getAddHp() {
		return addHp;
	}
	public void setAddHp(int addHp) {
		this.addHp = addHp;
	}
	public int getPush() {
		return push;
	}
	public void setPush(int push) {
		this.push = push;
	}
	public List<Integer> getAskings() {
		return askings;
	}
	public List<Integer> getAnswers() {
		return answers;
	}
	
	
}
