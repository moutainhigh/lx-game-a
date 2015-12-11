package cn.xgame.a.player.manor.info;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.string.StringUtil;
import x.javaplus.util.Util.Random;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.Build_treasurePo;
import cn.xgame.system.LXConstants;

/**
 * 行商建筑
 * @author deng		
 * @date 2015-12-11 下午5:44:13
 */
public class PedleryBuilding extends IBuilding {

	// 刷新等级 影响刷新间隔
	private byte updatelevel;
	// 货物数量等级
	private byte goodclevel;
	// 货物品质等级
	private byte goodqlevel;
	// 货物列表
	private List<Integer> treasures = Lists.newArrayList();
	// 货物刷新记录时间
	private int rtime;
		
	public PedleryBuilding(BType type, BbuildingPo templet) {
		super(type, templet);
	}
	
	@Override
	public void init() {
		updatelevel 	= 1;
		goodclevel 		= 1;
		goodqlevel 		= 1;
		rtime 			= (int)(System.currentTimeMillis()/1000);
		randomGoods();
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBuffer(buf);
		buf.writeByte( updatelevel );
		buf.writeByte( goodclevel );
		buf.writeByte( goodqlevel );
		buf.writeByte( treasures.size() );
		for( int id : treasures ){
			buf.writeInt(id);
		}
		buf.writeInt( rtime );
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBuffer(buf);
		updatelevel = buf.readByte();
		goodclevel = buf.readByte();
		goodqlevel = buf.readByte();
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			treasures.add( buf.readInt() );
		}
		rtime = buf.readInt();
	}
	
	@Override
	public void update() {
		int past = (int)(System.currentTimeMillis()/1000) - rtime;
		int t = LXConstants.BUILD_SHOP_UPDATETIME[updatelevel-1];
		if( past < t )
			return;
		randomGoods();
		rtime = (int)(System.currentTimeMillis()/1000);
	}

	private void randomGoods() {
		treasures.clear();
		Build_treasurePo templet = CsvGen.getBuild_treasurePo(getNid());
		if( templet == null || templet.content.isEmpty() ) return;
		
		// 先获取随机池子
		List<Integer> pool = StringUtil.arrayToInteger( templet.content, ";");
		// 开始随机
		while( treasures.size() < goodclevel ){
			int idx = Random.get(0, pool.size()-1);
			treasures.add( pool.remove(idx) );
		}
	}
	
	/**
	 * 获取下次刷新时间
	 * @return
	 */
	public int getNextUpdateTime(){
		return LXConstants.BUILD_SHOP_UPDATETIME[updatelevel-1] + rtime;
	}
	
	/**
	 * 删除一个宝箱
	 * @param tid
	 */
	public void removeChests(int tid) {
		int idx = treasures.indexOf(tid);
		if( idx != -1 )
			treasures.remove(idx);
	}

	public byte getUpdatelevel() {
		return updatelevel;
	}
	public void setUpdatelevel(byte updatelevel) {
		this.updatelevel = updatelevel;
	}
	public byte getGoodclevel() {
		return goodclevel;
	}
	public void setGoodclevel(byte goodclevel) {
		this.goodclevel = goodclevel;
	}
	public int getRtime() {
		return rtime;
	}
	public void setRtime(int rtime) {
		this.rtime = rtime;
	}
	public byte getGoodqlevel() {
		return goodqlevel;
	}
	public void setGoodqlevel(byte goodqlevel) {
		this.goodqlevel = goodqlevel;
	}
	public List<Integer> getTreasures() {
		return treasures;
	}



}
