package cn.xgame.a.player.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.NumberFilter;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.netty.Netty.RW;

/**
 * 玩家群聊&私聊
 * @author deng		
 * @date 2015-8-2 下午4:01:50
 */
public class ChatAxnControl implements IArrayStream, ITransformStream{
	
	private Player root;
	
	// 聊天操作类
	private final AxnControl chatControl = ChatManager.o.axns();
	
	// 群聊频道ID列表
	private List<Integer> groupaxn = Lists.newArrayList();
	
	// 私聊频道ID列表
	private List<Integer> privateaxn = Lists.newArrayList();
	
	
	public ChatAxnControl(Player player) {
		root = player;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null )
			return;
		groupaxn.clear();
		privateaxn.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int axnid = buf.readInt();
			AxnInfo axnInfo = chatControl.getAXNInfo(axnid);
			if( axnInfo != null && axnInfo.isHave(root) )
				groupaxn.add( axnid );
		}
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int axnid = buf.readInt();
			AxnInfo axnInfo = chatControl.getAXNInfo(axnid);
			if( axnInfo != null && axnInfo.isHave(root) )
				privateaxn.add( axnid );
		}
	}
	@Override
	public byte[] toBytes() {
		if( groupaxn.isEmpty() ) 
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( groupaxn.size() );
		for( int i : groupaxn ){
			buf.writeInt(i);
		}
		buf.writeByte( privateaxn.size() );
		for( int i : privateaxn ){
			buf.writeInt(i);
		}
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		List<FleetInfo> allFleet = root.getFleets().getHaveTeam();
		
		buffer.writeByte( groupaxn.size() + privateaxn.size() + allFleet.size() );
		// 塞入 群聊和私聊的频道
		for( int axnid : groupaxn ){
			AxnInfo axn = chatControl.getAXNInfo(axnid);
			buffer.writeInt( axn.getAxnId() );
			RW.writeString( buffer, axn.getName() );
		}
		// 塞入 私聊的频道
		for( int axnid : privateaxn ){
			AxnInfo axn = chatControl.getAXNInfo(axnid);
			buffer.writeInt( axn.getAxnId() );
			RW.writeString( buffer, axn.getPrivateName(root.getUID()) );
		}
		// 塞入 组队的频道
		for( FleetInfo fleet : allFleet ){
			AxnInfo axn = chatControl.getAXNInfo(fleet.getAxnId());
			buffer.writeInt( axn.getAxnId() );
			RW.writeString( buffer, NumberFilter.convertChineseStr(fleet.getNo()) );
		}
	}
	
	/**
	 * 根据频道类型获取 频道ID列表
	 * @param type
	 * @return
	 */
	private List<Integer> getAxn( ChatType type ){
		if( type == ChatType.GROUP )
			return groupaxn;
		if( type == ChatType.PRIVATE ){
			return privateaxn;
		}
		return null;
	}
	
	/**
	 * 获取频道个数是否满
	 * @param type
	 * @return
	 */
	public boolean axnIsMax( ChatType type ) {
		List<Integer> axns = getAxn( type );
		return axns == null ? true : axns.size() >= type.max();
	}

	/**
	 * 添加一个频道
	 * @param type
	 * @param axnId
	 */
	public void appendAxn( ChatType type, int axnId ) {
		List<Integer> axns = getAxn( type );
		if( axns == null )
			return;
		if( axns.indexOf(axnId) == -1 )
			axns.add( axnId );
	}
	
	/**
	 * 删除一个频道ID
	 * @param axnId
	 */
	public void removeAxn( ChatType type, int axnId ) {
		List<Integer> axns = getAxn( type );
		if( axns == null )
			return;
		Iterator<Integer> iter = axns.iterator();
		while( iter.hasNext() ){
			int next = iter.next();
			if( next == axnId ){
				iter.remove();
				return;
			}
		}
	}

}
