package cn.xgame.a.fighter.o;

import cn.xgame.a.IBufferStream;
import io.netty.buffer.ByteBuf;


/**
 * 攻击和防御属性 结构
 * @author deng		
 * @date 2015-7-30 上午11:16:36
 */
public class Attackattr implements IBufferStream{

	// 类型
	private byte type;
	
	// 数值
	private float value;
	
	public Attackattr(){}

	public Attackattr( byte type, float value ) {
		this.type = type;
		this.value = value;
	}
	
	public String toString(){
		return type + "-" + value;
	}
	
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type );
		buffer.writeInt( (int) value );
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( type );
		buf.writeFloat( value );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		type = buf.readByte();
		value = buf.readFloat();
	}
	
	public byte getType() {
		return type;
	}
	public void setType( byte type ) {
		this.type = type;
	}
	public float getValue() {
		return value;
	}
	public void setValue( float value ) {
		this.value = value;
	}
	public void addValue( float value ) {
		this.value += value;
	}
	
}
