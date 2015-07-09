package cn.xgame.a.prop.stuff;


import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 材料对象
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class Stuff extends IProp{
	
	
	public Stuff( int uid, int nid, int count ) {
		super( uid, nid, count );
	}
	
	public Stuff( PropsDto o ){
		super(o);
		// 下面加自己的属性 o.getAttach();
		
	}
	
	@Override
	public IProp clone() {
		Stuff ret = new Stuff(getuId(), getnId(), getCount());
		return ret;
	}
	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		
		
	}

	@Override
	public PropType type() { return PropType.STUFF; }

	@Override
	public void createDB(Player player) {
		super.create(player, null);
	}

	@Override
	public void updateDB(Player player) {
		super.update(player, null);
	}

	
}
