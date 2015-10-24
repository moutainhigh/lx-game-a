package cn.xgame.a.player.task.star;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.IBufferStream;

import x.javaplus.collections.Lists;

/**
 * 一个星球的任务
 * @author deng		
 * @date 2015-10-24 下午11:33:09
 */
public class StarTask implements IBufferStream{
	
	// 星球ID
	private final int SNID;
	
	// NPC列表
	private List<NpcTask> npcs = Lists.newArrayList();
	
	public StarTask( int id ){
		SNID = id;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( npcs.size() );
		for( NpcTask npc : npcs ){
			buf.writeInt( npc.getNpcId() );
			npc.putBuffer(buf);
		}
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			NpcTask npc = new NpcTask( buf.readInt() );
			npc.wrapBuffer(buf);
			npcs.add(npc);
		}
	}
	
	public int getSNID() {
		return SNID;
	}
	public List<NpcTask> getNpcs(){
		return npcs;
	}
	
	/**
	 * 添加一个NPC
	 * @param npc
	 */
	public void addNpc( NpcTask npc ) {
		npcs.add(npc);
	}

	public NpcTask getNpc( int npcid ) {
		for( NpcTask npc : npcs ){
			if( npc.getNpcId() == npcid )
				return npc;
		}
		return null;
	}

	public void removeNpc( int npcId ) {
		Iterator<NpcTask> iter = npcs.iterator();
		while(iter.hasNext()){
			if( iter.next().getNpcId() == npcId ){
				iter.remove();
				break;
			}
		}
	}

}
