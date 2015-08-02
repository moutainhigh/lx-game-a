package cn.xgame.a.player.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.chat.AxnControl;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.chat.o.ChatType;
import cn.xgame.a.player.u.Player;

/**
 * 玩家聊天信息 操作中心
 * @author deng		
 * @date 2015-8-2 下午4:01:50
 */
public class ChatAxnControl implements IArrayStream, ITransformStream{
	
	// 聊天操作类
	private final AxnControl chatControl = ChatManager.o.getChatControl();
	
	
	// 临时频道
	private List<Integer> tempaxn = Lists.newArrayList();
	
	
	public ChatAxnControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null )
			return;
		tempaxn.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			tempaxn.add( buf.readInt() );
		}
	}

	@Override
	public byte[] toBytes() {
		if( tempaxn.isEmpty() ) 
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( tempaxn.size() );
		for( int i : tempaxn ){
			buf.writeInt(i);
		}
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( tempaxn.size() );
		for( int axnid : tempaxn ){
			AxnInfo axn = chatControl.getAXNInfo(axnid);
			axn.buildTransformStream(buffer);
		}
	}
	
	/**
	 * 根据频道类型获取 频道ID列表
	 * @param type
	 * @return
	 */
	public List<Integer> getAxn( ChatType type ){
		if( type == ChatType.TEMPAXN )
			return tempaxn;
		return null;
	}
	
	
	/**
	 * 获取频道个数是否满
	 * @param type
	 * @return
	 */
	public boolean axnIsMax(ChatType type) {
		List<Integer> axns = getAxn( type );
		return axns == null ? true : axns.size() >= type.max();
	}

	/**
	 * 添加一个频道
	 * @param type
	 * @param axnId
	 */
	public void appendAxn(ChatType type, int axnId) {
		List<Integer> axns = getAxn( type );
		if( axns.indexOf(axnId) == -1 )
			axns.add( axnId );
	}



}
