package cn.xgame.a.world.planet.home.o;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.vote.Vote;
import cn.xgame.net.netty.Netty.RW;

/**
 * 驱逐元老 信息
 * @author deng		
 * @date 2015-7-10 下午5:27:42
 */
public class OustChild implements IBufferStream{
	
	// 元老唯一ID
	private String uid;
	// 投票说明
	private String explain;
	
	// 投票器 - 用于投票的
	private Vote vote;
	
	
	public OustChild( Player player, String UID, String explain ) {
		this.setUid(UID);
		this.explain = explain;
		vote = new Vote(player, 0);
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		RW.writeString(buf, uid);
		RW.writeString(buf, explain);
		vote.putBuffer(buf);
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		uid = RW.readString(buf);
		explain = RW.readString(buf);
		vote = new Vote(buf);
	}
	
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Vote getVote() {
		return vote;
	}
	public void setVote(Vote vote) {
		this.vote = vote;
	}




}
