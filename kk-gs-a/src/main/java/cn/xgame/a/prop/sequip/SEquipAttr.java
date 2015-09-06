package cn.xgame.a.prop.sequip;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.string.StringUtil;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.WeaponPo;
import cn.xgame.utils.LuaUtil;

/**
 * 舰船装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquipAttr extends IProp{

	private final WeaponPo templet;
	
	// 消耗能量
	private int energy;
	// 精密度
	private int accuracy;
	// 复杂度
	private int perplexity;
	// 质量
	private int mass;
	// 当前耐久度
	private int currentDur;
	// 总耐久度
	private int maxDur;
	// 推进
	private int boost;
	// 当前弹药量
	private int curAmmo;
	// 最大弹药量
	private int maxAmmo;
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
	
	public SEquipAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
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
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		energy 		= buf.readInt();
		accuracy 	= buf.readInt();
		perplexity 	= buf.readInt();
		mass 		= buf.readInt();
		currentDur 	= buf.readInt();
		maxDur 		= buf.readInt();
		boost 		= buf.readInt();
		curAmmo 	= buf.readInt();
		maxAmmo 	= buf.readInt();
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
	public void randomAttachAttr() {
		Lua lua 	= LuaUtil.getGameData();
		LuaValue[] value = lua.getField( "randomAttachAttr" ).call( 14, type().toNumber(), getQuality().toNumber() );
		int i 		= 0;
		energy 		= value[i++].getInt();
		accuracy 	= value[i++].getInt();
		perplexity 	= value[i++].getInt();
		mass 		= value[i++].getInt();
		currentDur 	= value[i++].getInt();
		maxDur 		= value[i++].getInt();
		boost 		= value[i++].getInt();
		curAmmo 	= value[i++].getInt();
		maxAmmo 	= value[i++].getInt();
		addHp 		= value[i++].getInt();
		String str  = value[i++].getString();
		if( !str.isEmpty() ){
			String[] ls = str.split("\\|");
			for( String o : ls ){
				if( o.isEmpty() ) continue;
				atks.add( new BattleAttr(o) );
			}
		}
		str  		= value[i++].getString();
		if( !str.isEmpty() ){
			String[] ls = str.split("\\|");
			for( String o : ls ){
				if( o.isEmpty() ) continue;
				defs.add( new BattleAttr(o) );
			}
		}
		str  		= value[i++].getString();
		if( !str.isEmpty() ){
			String[] ls = str.split(";");
			for( String o : ls ){
				askings.add( Integer.parseInt( StringUtil.convertNumberString( o ) ) );
			}
		}
		str  		= value[i++].getString();
		if( !str.isEmpty() ){
			String[] ls = str.split(";");
			for( String o : ls ){
				answers.add( Integer.parseInt( StringUtil.convertNumberString( o ) ) );
			}
		}
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( energy );
		buffer.writeInt( accuracy );
		buffer.writeInt( perplexity );
		buffer.writeInt( mass );
		buffer.writeInt( currentDur );
		buffer.writeInt( maxDur );
		buffer.writeInt( boost );
		buffer.writeInt( curAmmo );
		buffer.writeInt( maxAmmo );
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



	
}
