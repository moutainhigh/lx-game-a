package cn.xgame.a.prop.captain;



import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;

/**
 * 舰长对象
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class Captain extends IProp {

	


	public Captain(int uid, int nid, int count) {
		initialize(uid, nid, count);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		
	}

	@Override
	public PropType type() {
		return PropType.CAPTAIN;
	}

	@Override
	public void createDB(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDB(Player player) {
		// TODO Auto-generated method stub
		
	}
	
	




}
