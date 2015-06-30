package cn.xgame.a.world.planet.data.tech;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IArrayStream;

/**
 * 科技 操作中心
 * @author deng		
 * @date 2015-6-26 下午1:32:00
 */
public class TechControl implements IArrayStream{

	// 已学习的科技列表
	private List<Techs> techs = Lists.newArrayList();
	
	// 投票中的科技列表
	private List<UnTechs> voTechs = Lists.newArrayList();
	
	// 研究中的科技列表
	private List<UnTechs> unTechs = Lists.newArrayList();
	
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}


	public void putTechs(ByteBuf buffer) {
		buffer.writeByte( techs.size() );
		for( Techs o : techs ){
			o.buildTransformStream(buffer);
		}
	}

	public void putUnTech(ByteBuf buffer) {
		buffer.writeByte( unTechs.size() );
		for( UnTechs o : unTechs ){
			o.buildTransformStream(buffer);
		}
	}
	
	public void putVoTech(ByteBuf buffer){
		buffer.writeByte( voTechs.size() );
		for( UnTechs o : unTechs ){
			o.buildTransformStream(buffer);
		}
	}

}
