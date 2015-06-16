package cn.xgame.utils;

import cn.xgame.net.event.Events;

public class PackageCheck {
	
	/**
	 * 接收相同包号两个包之间允许的最短时间间隔，如果小于这个值则认定客户端有刷包嫌疑，丢弃这个包
	 * 单位	毫秒
	 */
	private static final long 			MIN_INTERVAL_MILS = 150;
	/**
	 * 接收的上一个包
	 */
	private Events						lastPack;
	/**
	 * 上一次收包时间
	 */
	private	long						lastReceiveTime = 0;
	/**
	 * 检查玩家是否存在断时间内恶意刷大量包的情况<br>
	 * 只有在连续两个包的包号相同并且间隔时间少于规定值的时候，才返回false<br>
	 * 具体规则可能还需要根据实际情况进一步调整
	 * @param packageNo
	 * @return
	 */
	public boolean safeCheck( Events pack ){
		long current = System.currentTimeMillis();
		if( pack == lastPack && current - lastReceiveTime < MIN_INTERVAL_MILS ){
//			Logs.error( "刷包行为 - packag:" + pack + ","+ current + "-" + lastReceiveTime  + "=" + (current - lastReceiveTime) );
			return true;
		}
		lastPack 		= pack;
		lastReceiveTime = current;
		return false;
	}
	
}
