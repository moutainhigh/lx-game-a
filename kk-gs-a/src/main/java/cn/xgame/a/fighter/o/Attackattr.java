package cn.xgame.a.fighter.o;

import io.netty.buffer.ByteBuf;


/**
 * 攻击和防御属性 结构
 * @author deng		
 * @date 2015-7-30 上午11:16:36
 */
public class Attackattr {

	// 类型
	private byte type;
	
	// 数值
	private float value;

	public Attackattr( byte type, float value ) {
		this.type = type;
		this.value = value;
	}

	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type );
		buffer.writeFloat( value );
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
	
}
