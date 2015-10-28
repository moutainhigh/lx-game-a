package cn.xgame.a.player.ectype.classes;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.fighter.o.Attackattr;

/**
 * 一个副本数据基类  - 以下数据将会保存数据库
 * @author deng		
 * @date 2015-10-27 下午8:26:24
 */
public class IEctype implements IBufferStream{

	// 难度
	private byte level;
	
	// 血量
	private int hp;
	
	// 战斗时间
	private int fighttime ;
	
	// 最大成功率
	private short maxSuccessRate;
		
	// 攻击列表
	private List<Attackattr> atks = Lists.newArrayList();
	
	// 防御列表
	private List<Attackattr> defs = Lists.newArrayList();
	
	// 道具奖励倍率
	private float awardRate ; 
	
	// 金币奖励
	private int[] money = new int[2];
	
	
	public IEctype( byte level ) {
		this.level = level;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(hp);
		buf.writeByte( atks.size() );
		for( Attackattr o : atks )
			o.putBuffer(buf);
		buf.writeByte( defs.size() );
		for( Attackattr o : defs )
			o.putBuffer(buf);
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		hp = buf.readInt();
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			Attackattr o = new Attackattr();
			o.wrapBuffer(buf);
			atks.add( o );
		}
		size = buf.readByte();
		for (int i = 0; i < size; i++) {
			Attackattr o = new Attackattr();
			o.wrapBuffer(buf);
			defs.add( o );
		}
	}
	
	public byte getLevel() {
		return level;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public List<Attackattr> getAtks() {
		return atks;
	}
	public void addAtks( byte type, float value ) {
		this.atks.add( new Attackattr(type, value) );
	}
	public List<Attackattr> getDefs() {
		return defs;
	}
	public void addDefs( byte type, float value ) {
		this.defs.add( new Attackattr(type, value) );
	}
	public void setMoney( int min, int max ) {
		this.money[0] = min;
		this.money[1] = max;
	}
	public int getMoney(){
		return Random.get( money[0], money[1] );
	}
	public int getFighttime() {
		return fighttime;
	}
	public void setFighttime( int fighttime ) {
		this.fighttime = fighttime;
	}
	public short getMaxSuccessRate() {
		return maxSuccessRate;
	}
	public void setMaxSuccessRate(short maxSuccessRate) {
		this.maxSuccessRate = maxSuccessRate;
	}
	public float getAwardRate() {
		return awardRate;
	}
	public void setAwardRate(float awardRate) {
		this.awardRate = awardRate;
	}
	
	/**
	 * 返回一个战斗者 供lua脚本调用
	 * @return
	 */
	public Fighter fighter(){
		Fighter fighter = new Fighter();
		fighter.hp = hp;
		fighter.attacks.addAll(atks);
		fighter.defends.addAll(defs);
		return fighter;
	}
	
}
