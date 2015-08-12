package cn.xgame.a.player.ship.o.v;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;


import cn.xgame.a.IArrayStream;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;

/**
 * 战斗记录
 * @author deng		
 * @date 2015-7-31 上午2:35:19
 */
public class CombatRecordControl implements IArrayStream {

	// 副本ID
	private int enid 		= -1;
	
	// 是否胜利
	private byte isWin		= 0;
	
	// 战斗时间
	private int combatTime 	= 0;
	
	// 奖励列表
	private List<AwardInfo> awards = Lists.newArrayList();
	
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) 
			return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		enid 		= buf.readInt();
		isWin 		= buf.readByte();
		combatTime	= buf.readInt(); 
		byte size 	= buf.readByte();
		for( int i = 0 ; i < size; i++ ){
			AwardInfo award = new AwardInfo( buf );
			awards.add(award);
		}
	}
	@Override
	public byte[] toBytes() {
		if( isEmpty() )
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeInt( enid );
		buf.writeByte( isWin );
		buf.writeInt( combatTime );
		buf.writeByte( awards.size() );
		for( AwardInfo award : awards )
			award.buildTransformStream(buf);
		return buf.array();
	}
	
	public boolean isEmpty(){
		return enid == -1;
	}
	
	public void clear() {
		enid = -1;
		isWin = 0;
		awards.clear();
	}
	
	/**
	 * 发放奖励
	 * @param player
	 * @return
	 */
	public List<IProp> giveoutAward(Player player) {
		List<IProp> ret = Lists.newArrayList();
		// 这里把奖励发放到玩家身上
		for( AwardInfo award : awards ){
			List<IProp> add = player.getDepots().appendProp( award.getId(), award.getCount() );
			ret.addAll(add);
		}
		return ret;
	}
	
	public int getEnid() {
		return enid;
	}
	public void setEnid(int enid) {
		this.enid = enid;
	}
	public List<AwardInfo> getAwards() {
		return awards;
	}
	public byte isWin() {
		return isWin;
	}
	public void setWin(byte isWin) {
		this.isWin = isWin;
	}
	public void addAwards(List<AwardInfo> award) {
		this.awards.addAll(award);
	}
	public int getCombatTime() {
		return combatTime;
	}
	public void setCombatTime(int combatTime) {
		this.combatTime = combatTime;
	}
	


}
