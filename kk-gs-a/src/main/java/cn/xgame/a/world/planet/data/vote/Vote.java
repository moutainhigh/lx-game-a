package cn.xgame.a.world.planet.data.vote;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;

/**
 * 投票器
 * @author deng		
 * @date 2015-6-30 下午5:04:43
 */
public class Vote implements ITransformStream , IBufferStream{

	// 同意的玩家
	private List<VotePlayer> agrees;
	
	// 不同意的玩家
	private List<VotePlayer> disagrees;
	
	// 发起人
	private String sponsorUid;
	
	// 时间限制 单位-秒
	private int timeRestrict;
	
	// 临时变量
	private short agreePrivileges = 0; // 不同意的所有 话语权总和
	private short disagreePrivileges = 0;// 同意的所有 话语权总和

	public Vote( String uid, int time ) {
		this.sponsorUid = uid;
		this.timeRestrict = time;
		agrees = Lists.newArrayList();
		disagrees = Lists.newArrayList();
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeShort( agreePrivileges );
		buffer.writeShort( disagreePrivileges );
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置投票
	 * @param plyer 玩家
	 * @param isAgree 1.同意 0.不同意 -1.还没出现结果
	 * @return 
	 */
	public byte setIsAgrees( VotePlayer plyer, byte isAgree ) {
		if( isAgree == 1 ){
			agrees.add(plyer);
			agreePrivileges += plyer.getPrivilege();
			if( agreePrivileges > 5000 ) // 必须大于50%才能算同意
				return 1;
		}else{
			disagrees.add(plyer);
			disagreePrivileges += plyer.getPrivilege();
			if( disagreePrivileges >= 5000 ) // 大于等于50%就算不同意
				return 0;
		}
		return -1;
	}

	/**
	 * uid玩家是否已经参与投票了
	 * @param uid
	 * @return
	 */
	public boolean isParticipateVote( String uid ) {
		for( VotePlayer v : agrees ){
			if( v.getUID().equals(uid) )
				return true;
		}
		for( VotePlayer v : disagrees ){
			if( v.getUID().equals(uid) )
				return true;
		}
		return false;
	}
	

	public int getTimeRestrict() {
		return timeRestrict;
	}
	public String getSponsorUid() {
		return sponsorUid;
	}
	
}
