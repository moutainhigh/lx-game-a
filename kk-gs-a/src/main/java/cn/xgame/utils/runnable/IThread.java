package cn.xgame.utils.runnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程 类
 * @author DXF
 */
public abstract class IThread implements Runnable {
	
	private ScheduledExecutorService s = null;
	
	public IThread(){
		s = Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * 开启线程
	 * @param tick (间隔 毫秒)
	 */
	public void start( long tick ){
		s.scheduleAtFixedRate( this, 0, tick, TimeUnit.MILLISECONDS );
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				s.shutdown();
			}
		});
	}
	
	/**
	 * 开启线程
	 * @param delay (延期 毫秒)
	 * @param tick (间隔 毫秒)
	 */
	public void start( long delay, long tick ){
		s.scheduleAtFixedRate( this, delay, tick, TimeUnit.MILLISECONDS );
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				s.shutdown();
			}
		});
	}
	
	public void stop(){
		s.shutdown();
	}
	
	public abstract void run();
	
}
