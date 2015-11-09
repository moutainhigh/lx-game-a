package cn.xgame.net.event.all;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cn.xgame.net.netty.classes.IEvent;


public class TestEevet extends IEvent{

	@Override
	public void run(ChannelHandlerContext ctx, ByteBuf data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println( InetAddress.getLocalHost().getHostAddress() );//获得本机IP
//		　　address=addr.getHostName()toString;//获得本机名称
	}
}
