package cn.xgame.net.event.all.gm;

import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;


import net.sf.json.JSONObject;

import cn.xgame.gen.dto.MysqlGen.GmUserDao;
import cn.xgame.gen.dto.MysqlGen.GmUserDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.http.classes.IEvent;

public class SignupEvent extends IEvent{
	
	private final String PASSWORD = "longxun";
	
	
	@Override
	public void run(ChannelHandlerContext ctx, JSONObject data, HttpRequest request) throws IOException{
		
		String username = data.getString( "username" );
		String password = data.getString( "password" );
		String serial	= data.getString( "serial" );
		
		int code = 0;
		if( password.equals( PASSWORD ) ){
			GmUserDao dao = SqlUtil.getGmUserDao();
			GmUserDto dto = dao.create();
			dto.setName(username);
			dto.setSerial(serial);
			dao.commit( dto );
		}else{
			code = 1;
		}
		
		JSONObject ret = new JSONObject();
		ret.accumulate( "code", code );
		sendPackage(ctx, ret, request);
	}

}
