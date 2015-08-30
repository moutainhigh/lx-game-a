package cn.xgame.a.prop.stuff;


import cn.xgame.a.prop.IProp;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 材料属性
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class StuffAttr extends IProp{
	
	
	public StuffAttr( int uid, int nid, int count ) {
		super( uid, nid, count );
	}
	
	public StuffAttr( PropsDto o ){
		super(o);
	}
	
	@Override
	public IProp clone() {
		StuffAttr ret = new StuffAttr(getUid(), getNid(), getCount());
		
		return ret;
	}

	@Override
	public byte[] toAttachBytes() {
		return null;
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		
	}

	
}
