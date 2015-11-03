package cn.xgame.a.player.fleet.info.result;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IResult;

/**
 * 战斗中
 * @author deng		
 * @date 2015-11-3 下午5:57:38
 */
public class CombatIn extends IResult{

	// 起始时间
	private int starttime;
	
	// 深度时间 
	private int depthtime;
	
	// 战斗时间
	private int fighttime;

	public CombatIn() {
		super((byte) 1);
	}
	
	public CombatIn(int starttime, int depthtime, int fighttime) {
		super((byte) 1);
		this.starttime = starttime;
		this.depthtime = depthtime;
		this.fighttime = fighttime;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( starttime );
		buf.writeInt( depthtime );
		buf.writeInt( fighttime );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		starttime = buf.readInt();
		depthtime = buf.readInt();
		fighttime = buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getAlreadyFighttime() );
		buffer.writeInt( fighttime );
	}
	
	// 获取已经战斗的时间 - 主要用于前端显示
	private int getAlreadyFighttime() {
		return (int)(System.currentTimeMillis()/1000) - starttime;
	}
	
	@Override
	public boolean isComplete() {
		return (int)(System.currentTimeMillis()/1000) >= (starttime+fighttime+depthtime*2);
	}
	
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public int getDepthtime() {
		return depthtime;
	}
	public void setDepthtime(int depthtime) {
		this.depthtime = depthtime;
	}
	public int getFighttime() {
		return fighttime;
	}
	public void setFighttime(int fighttime) {
		this.fighttime = fighttime;
	}


}
