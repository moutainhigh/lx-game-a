package cn.xgame.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * netty工具类
 * @author deng		
 * @date 2015-6-12 下午12:10:27
 */
public class Netty {
	
	public static final class Config{
		
		/** 包头 */
		public static final byte 	PACKAGE_HEAD	= 105;
		/** 包尾 */
		public static final byte 	PACKAGE_FOOT	= 104;
		/** 客户端最多发送字节数 */
		public static final int 	PACKAGE_LENGTH	= 8192;
		
	}


	public static final class IP{
		
		/** 根据 ChannelHandlerContext 获取IP ( 本地IP ) */
		public static String formAddress( ChannelHandlerContext ctx ){
			if( ctx == null ) return "";
			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
			return insocket.getAddress().getHostAddress();
		}
	}
	
	public static final class RW{
		
		/** 写入字符串 */
		public static void writeString( ByteBuf buf, String msg ){
			if( msg == null )
				throw new IllegalArgumentException( "null 字符" );
			byte[] temp = msg.getBytes( CharsetUtil.UTF_8 );
			if (temp.length > Short.MAX_VALUE)
				throw new IllegalArgumentException( "字符串长度超" );
			buf.writeShort((short) temp.length);
			if( temp.length != 0 )
				buf.writeBytes(temp);
		}
		
		/** 读取字符串 */
		public static String readString( ByteBuf buf ){
			short len = buf.readShort();
			if( len > 0 ){
				String ret = buf.toString( buf.readerIndex(), len, CharsetUtil.UTF_8 );
				buf.readerIndex( buf.readerIndex() + len );
				return ret;
			}
			return "";
		}
		
		/** 写入bytes */
		public static void writeBytes( ByteBuf buf, byte[] bytes ) {
			buf.writeShort( bytes == null ? 0 : bytes.length );
			if( bytes != null )
				buf.writeBytes(bytes);
		}
		
		/** 读取bytes */
		public static byte[] readBytes( ByteBuf buf ){
			int len = buf.readShort();
			if( len > 0 ){
				byte[] ret = new byte[len];
				buf.readBytes(ret);
				return ret;
			}
			return null;
		}
		
	}
	
	public static final class Attr{
		
		/** 付属性key 对应String */@SuppressWarnings("deprecation")
		private static final AttributeKey<String> SSTRING 	= new AttributeKey<String>( "DENG.XO.STRING" );
		
		/** 付属性key 对应Integer */@SuppressWarnings("deprecation")
		private static final AttributeKey<Integer> SINTEGER = new AttributeKey<Integer>( "DENG.XO.INTEGER" );
		
		public static String getAttachment( ChannelHandlerContext ctx ) {
			return ctx.attr( SSTRING ).get();
		}
		public static int getAttachmentToInteger( ChannelHandlerContext ctx ) {
			Integer i = ctx.attr( SINTEGER ).get();
			return i == null ? 0 : i;
		}
		public static void setAttachment( ChannelHandlerContext ctx, int i ){
			ctx.attr( SINTEGER ).set( i );
		}
		public static void setAttachment( ChannelHandlerContext ctx, String s ){
			ctx.attr( SSTRING ).set( s );
		}
	}
	
	public static void main(String[] args) {
		
		ByteBuf buf = Unpooled.buffer();
		buf.writeInt( 19 );
		byte[] bytes = new byte[]{ 1,2,3,4,5,8,124,12 };
		buf.writeBytes(bytes);
		
		System.out.println( buf );
		
		System.out.println( buf.readInt() );
		System.out.println( buf );
		
//		System.out.println( buf.readerIndex() + ", " + buf.writerIndex() );
		System.out.println( buf.isReadable() );
		byte[] bytes1 = new byte[buf.writerIndex()-buf.readerIndex()];
		buf.readBytes(bytes1);
		for( int i = 0; i < bytes1.length ; i++ )
			System.out.print( bytes1[i] + ", " );
		
		System.out.println( buf.isReadable() );
		
	}
	
}
