package cn.xgame.net.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * netty的一些配置 * 
 * @author deng
 */
public class NettyConfig {
	
	/** 付属性key 对应String */@SuppressWarnings("deprecation")
	public static final AttributeKey<String> SSTRING 	= new AttributeKey<String>( "Deng.XO.STRING" );
	
	/** 付属性key 对应Integer */@SuppressWarnings("deprecation")
	public static final AttributeKey<Integer> SINTEGER 	= new AttributeKey<Integer>( "Deng.XO.INTEGER" );
	
	/** 客户端最多发送字节数 */
	public static final byte 	PACKAGE_HEAD	= 115;
	/** 客户端最多发送字节数 */
	public static final byte 	PACKAGE_FOOT	= 114;
	/** 客户端最多发送字节数 */
	public static final int 	PACKAGE_LENGTH	= 8192;
	
	public static String getAttachment( ChannelHandlerContext ctx ) {
		String s = ctx.attr( NettyConfig.SSTRING ).get();
		return s == null ? "" : s;
	}
//	public static int getAttachment( ChannelHandlerContext ctx ) {
//		Integer i = ctx.attr( NettyConfig.SINTEGER ).get();
//		return i == null ? 0 : i;
//	}
	public static void setAttachment( ChannelHandlerContext ctx, int i ){
		ctx.attr( NettyConfig.SINTEGER ).set( i );
	}
	public static void setAttachment( ChannelHandlerContext ctx, String s ){
		ctx.attr( NettyConfig.SSTRING ).set( s );
	}
	
}
