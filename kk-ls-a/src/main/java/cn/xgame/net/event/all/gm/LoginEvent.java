package cn.xgame.net.event.all.gm;


import java.io.IOException;
import java.util.List;

import x.javaplus.mysql.db.Condition;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;


import net.sf.json.JSONObject;

import cn.xgame.a.gs.GSData;
import cn.xgame.a.gs.GSManager;
import cn.xgame.gen.dto.MysqlGen.GmUserDao;
import cn.xgame.gen.dto.MysqlGen.GmUserDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.http.classes.IEvent;

public class LoginEvent extends IEvent{

	@Override
	public void run(ChannelHandlerContext ctx, JSONObject data, HttpRequest request) throws IOException {
		
		String serial = data.getString("serial");
		String username = getUser( serial );
		
		JSONObject ret = new JSONObject();
		ret.accumulate( "code", username == null ? 1 : 0 );
		ret.accumulate( "username", username == null ? "" : username );
		ret.accumulate( "servers", "1,校长的爱,127.0.0.1|2,屌丝逆袭,127.0.0.1" );
		sendPackage(ctx, ret, request);
	}

	private String generateServers() {
		String ret = "";
		List<GSData> ls = GSManager.o.getOpenGs();
		for( GSData data : ls )
			ret += ( data.getId() + "," + data.getName() + "," + data.getIp() + "|");
		return ret;
	}

	private String getUser(String serial) {
		GmUserDao dao = SqlUtil.getGmUserDao();
		String sql = new Condition( GmUserDto.serialChangeSql(serial) ).toString();
		List<GmUserDto> dto = dao.getByExact( sql );
		dao.commit();
		if( dto.isEmpty() )
			return null;
		return dto.get(0).getName();
	}
	
}
