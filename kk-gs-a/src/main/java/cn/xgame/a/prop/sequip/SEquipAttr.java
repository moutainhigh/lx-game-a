package cn.xgame.a.prop.sequip;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.WeaponPo;

/**
 * 舰船装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquipAttr extends IProp{

	private final WeaponPo templet;
	
	// 品质
	private byte quality;
	// 消耗能量
	private int energy;
	// 精密度
	private int accuracy;
	// 复杂度
	private int perplexity;
	// 当前耐久度
	private int currentDur;
	// 总耐久度
	private int maxDur;
	// 推进
	private int boost;
	// 弹药量
	private int ammo;
	// 添加生命值
	private int addHp;
	// 攻击属性列表
	private List<BattleAttr> atks = Lists.newArrayList();
	// 防御属性列表
	private List<BattleAttr> defs = Lists.newArrayList();
	// 应答 - 问
	private List<Integer> askings = Lists.newArrayList();
	// 应答 - 答
	private List<Integer> answers = Lists.newArrayList();
	
	public SEquipAttr( ItemPo item, int uid, int nid, int count ) {
		super( item, uid, nid, count);
		templet 	= CsvGen.getWeaponPo(nid);
	}
	
	private SEquipAttr( SEquipAttr clone ){
		super( clone );
		templet 	= clone.templet;
		currentDur 	= clone.currentDur;
	}
	
	@Override
	public SEquipAttr clone() { return new SEquipAttr( this ); }
	
	public WeaponPo templet() { return templet; }

	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 125 );
		buildTransformStream( buf );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		quality		= buf.readByte();
		energy 		= buf.readInt();
		accuracy 	= buf.readInt();
		perplexity 	= buf.readInt();
		currentDur 	= buf.readInt();
		maxDur 		= buf.readInt();
		boost 		= buf.readInt();
		ammo 		= buf.readInt();
		addHp 		= buf.readInt();
		byte size	= buf.readByte();
		for( int i = 0; i < size; i++ )
			atks.add( new BattleAttr(buf) );
		size		= buf.readByte();
		for( int i = 0; i < size; i++ )
			defs.add( new BattleAttr(buf) );
		size 		= buf.readByte();
		for( int i = 0; i < size; i++ )
			askings.add( buf.readInt() );
		size 		= buf.readByte();
		for( int i = 0; i < size; i++ )
			answers.add( buf.readInt() );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( quality );
		buffer.writeInt( energy );
		buffer.writeInt( accuracy );
		buffer.writeInt( perplexity );
		buffer.writeInt( currentDur );
		buffer.writeInt( maxDur );
		buffer.writeInt( boost );
		buffer.writeInt( ammo );
		buffer.writeInt( addHp );
		buffer.writeByte( atks.size() );
		for( BattleAttr a : atks )
			a.buildTransformStream( buffer );
		buffer.writeByte( defs.size() );
		for( BattleAttr a : defs )
			a.buildTransformStream( buffer );
		buffer.writeByte( askings.size() );
		for( int id : askings )
			buffer.writeInt(id);
		buffer.writeByte( answers.size() );
		for( int id : answers )
			buffer.writeInt(id);
	}
	
	
	/** 是否武器 */
	public boolean isWeapon() {
		return item().itemtype == 1;
	}
	/** 是否辅助装备 */
	public boolean isAssistEquip() {
		return item().itemtype == 2;
	}
	public int getCurrentDur() {
		return currentDur;
	}
	public void setCurrentDur(int currentDur) {
		this.currentDur = currentDur;
	}
	public byte getQuality() {
		return quality;
	}
	public void setQuality(byte quality) {
		this.quality = quality;
	}

	
}
