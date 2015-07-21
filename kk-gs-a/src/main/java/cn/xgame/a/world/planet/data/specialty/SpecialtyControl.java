package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IProp;

/**
 * 特产 操作中心
 * @author deng		
 * @date 2015-6-26 上午11:37:14
 */
public class SpecialtyControl implements IArrayStream,ITransformStream{

	// 特产列表
	private List<Specialty> specialtys = Lists.newArrayList();
	
	
	/**
	 * 通过配置表 初始化
	 * @param content
	 */
	public void fromTemplet( String content ) {
		if( content.isEmpty() ) return;
		String[] ls = content.split("\\|");
		for( int i = 0; i < ls.length; i++ ){
			if( ls[i].isEmpty() ) continue;
			String[] x 	= ls[i].split( ";" );
			Specialty o = new Specialty( Integer.parseInt( x[0] ) );
			o.setYieldTime( Integer.parseInt( x[1] ) );
			o.setYieldNum( Integer.parseInt( x[2] ) );
			o.setMaxYieldNum( Integer.parseInt( x[3] ) );
			specialtys.add(o);
		}
	}

	@Override
	public void fromBytes( byte[] data ) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Specialty o = new Specialty( id );
			o.wrapBuffer(buf);
			specialtys.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		if( specialtys.isEmpty() )
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( specialtys.size() );
		for( Specialty o : specialtys ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
		}
		return buf.array();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( getSpecialtys().size() );
		for( Specialty o : getSpecialtys() ){
			o.buildTransformStream( buffer );
		}
	}

	public List<Specialty> getSpecialtys() {
		return specialtys;
	}
	public void setSpecialtys(List<Specialty> specialtys) {
		this.specialtys = specialtys;
	}

	public List<IProp> toProps() {
		List<IProp> ret = Lists.newArrayList();
		for( Specialty spe : specialtys ){
			ret.add( spe.toProp() );
		}
		return ret;
	}

	public Specialty getSpecialty( int nid ) {
		for( Specialty o : specialtys ){
			if( o.templet().id == nid )
				return o;
		}
		return null;
	}
	
	
	/**
	 * 扣除对应特产数量
	 * @param nid
	 * @param count
	 */
	public void deduct( int nid, int count ) {
		Specialty o = getSpecialty( nid );
		if( o == null ) return;
		int rcount = o.getYieldCount() - count;
		o.setYieldCount( rcount );
	}



	

}
