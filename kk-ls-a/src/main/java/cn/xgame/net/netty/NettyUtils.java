package cn.xgame.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * netty工具 * 
 * @author deng
 */
public class NettyUtils {
	/**
	 * 根据 ChannelHandlerContext 获取IP
	 * @param ctx
	 * @return ip
	 */
	public static String ctxToAddress( ChannelHandlerContext ctx ){
		if( ctx == null ) return "";
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		return insocket.getAddress().getHostAddress();
	}
	
	/**
	 * 写入字符
	 * @param buf
	 * @param arg
	 */
	public static void writeString( ByteBuf buf, String arg ){
		if( arg == null )
			throw new IllegalArgumentException( "null 字符" );
		byte[] temp = arg.getBytes();
		if (temp.length > Short.MAX_VALUE)
			throw new IllegalArgumentException( "字符串长度超" );
		buf.writeShort((short) temp.length);
		if( temp.length != 0 )
			buf.writeBytes(temp);
	}
	
	/**
	 * 读取字符
	 * @return
	 */
	public static String readString( ByteBuf buf ){
		short len = buf.readShort();
		if( len > 0 ){
			if( buf.writerIndex() - buf.readerIndex() < len ) return "";
			byte[] content = new byte[len];
			buf.readBytes(content);
			return new String(content);
		}
		return "";
	}
	
}
