package cn.xgame.a.player.ship.o.v;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;


import cn.xgame.a.IArrayStream;
import cn.xgame.a.award.AwardInfo;

/**
 * 舰船副本战斗信息 
 * 最后根据这个信息 发放奖励
 * @author deng		
 * @date 2015-7-31 上午2:35:19
 */
public class EctypeCombatInfo implements IArrayStream {

	// 副本ID
	private int enid = -1;
	
	// 是否胜利
	private byte isWin;
	
	// 奖励列表
	private List<AwardInfo> awards = Lists.newArrayList();
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) 
			return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		enid 	= buf.readInt();
		isWin 	= buf.readByte();
		byte size = buf.readByte();
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



}
