package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;

/**
 * 战斗属性
 * @author deng		
 * @date 2015-9-2 下午6:16:20
 */
public class BattleAttr implements ITransformStream{
	
	private byte type;
	private int value;
	
	public BattleAttr(ByteBuf buf) {
		type 	= buf.readByte();
		value 	= buf.readInt();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type );
		buffer.writeInt( value );
	}

	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

}
