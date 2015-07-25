package cn.xgame.a.world.planet.data.vote;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.netty.Netty.RW;

/**
 * 投票器
 * @author deng		
 * @date 2015-6-30 下午5:04:43
 */
public class Vote implements ITransformStream {

	// 同意的玩家
	private List<VotePlayer> agrees;
	
	// 不同意的玩家
	private List<VotePlayer> disagrees;
	
	// 发起人
	private String sponsorUid;
	private String sponsorName;
	
	// 时间限制 单位-秒
	private int timeRestrict;
	private int rTime;
	
	// 临时变量
	private short agreePrivileges = 0; // 不同意的所有 话语权总和
	private short disagreePrivileges = 0;// 同意的所有 话语权总和

	public Vote( Player player, int time ) {
		this.sponsorUid = player.getUID();
		this.sponsorName = player.getNickname();
		this.timeRestrict = time;
		this.rTime = (int) (System.currentTimeMillis()/1000);
		agrees = Lists.newArrayList();
		disagrees = Lists.newArrayList();
	}

	/**
	 * 从数据库 获取
	 * @param buf
	 */
	public Vote( ByteBuf buf ) {
		sponsorUid = RW.readString(buf);
		sponsorName = RW.readString(buf);
		timeRestrict = buf.readInt();
		rTime = buf.readInt();
		agrees = Lists.newArrayList();
		disagrees = Lists.newArrayList();
		short size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			VotePlayer o = new VotePlayer(buf);
			agreePrivileges += o.getPrivilege();
			agrees.add( o );
		}
		size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			VotePlayer o = new VotePlayer(buf);
			disagreePrivileges += o.getPrivilege();
			disagrees.add( o );
		}
	}

	public void putBuffer( ByteBuf buf ) {
		RW.writeString( buf, sponsorUid );
		RW.writeString( buf, sponsorName );
		buf.writeInt( timeRestrict );
		buf.writeInt( rTime );
		buf.writeShort( agrees.size() );
		for( VotePlayer vote : agrees ){
			vote.putBuffer( buf );
		}
		buf.writeShort( disagrees.size() );
		for( VotePlayer vote : disagrees ){
			vote.putBuffer( buf );
		}
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		RW.writeString( buffer, sponsorName );
		buffer.writeByte( agreePrivileges/100 );
		buffer.writeByte( disagreePrivileges/100 );
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
	public byte isParticipateVote( String uid ) {
		for( VotePlayer v : agrees ){
			if( v.getUID().equals(uid) )
				return 1;
		}
		for( VotePlayer v : disagrees ){
			if( v.getUID().equals(uid) )
				return 0;
		}
		return 2;
	}
	
	/**
	 * 清除玩家的投票
	 * @param uid
	 */
	public void purgeVote( String uid ) {
		Iterator<VotePlayer> iter = agrees.iterator();
		purge( iter, uid  );
		iter = disagrees.iterator();
		purge( iter, uid  );
	}
	private void purge( Iterator<VotePlayer> iter, String uid) {
		while( iter.hasNext() ){
			VotePlayer o = iter.next();
			if( o.getUID().equals(uid) ){
				iter.remove();
				return;
			}
		}
	}

	public int getTimeRestrict() {
		return timeRestrict;
	}
	public String getSponsorUid() {
		return sponsorUid;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public short getAgreePrivileges(){
		return agreePrivileges;
	}
	public short getDisagreePrivileges(){
		return disagreePrivileges;
	}
	
	/**
	 * 时间是否到
	 * @return
	 */
	public boolean isComplete() {
		int t = (int) (System.currentTimeMillis()/1000 - rTime);
		return t >= timeRestrict;
	}



}
