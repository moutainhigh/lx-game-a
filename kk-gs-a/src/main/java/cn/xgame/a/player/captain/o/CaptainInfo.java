package cn.xgame.a.player.captain.o;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.IUObject;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Captain;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;

/**
 * 一个 舰长 信息
 * @author deng		
 * @date 2015-7-9 下午12:28:55
 */
public class CaptainInfo extends IUObject implements ITransformStream{

	private final Captain template; 
	
	public CaptainInfo(int uid, int nid) {
		setuId(uid);
		setnId(nid);
		template = CsvGen.getCaptain(nid);
	}

	public CaptainInfo(CaptainsDto dto) {
		setuId(dto.getUid());
		setnId(dto.getNid());
		template = CsvGen.getCaptain(dto.getNid());
	}

	public Captain template(){ return template; }
	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
	}

}
