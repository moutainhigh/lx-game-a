package cn.xgame.a.player.manor.info;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.Build_shopPo;
import cn.xgame.system.LXConstants;


/**
 * 商店建筑
 * @author deng		
 * @date 2015-12-11 下午2:27:10
 */
public class ShopBuilding extends IBuilding{
	
	// 刷新等级 影响刷新间隔
	private byte updatelevel;
	// 提供货物
	private byte goodclevel;
	// 货物品质等级
	private byte goodqlevel;
	// 货物列表
	private List<AwardInfo> goods = Lists.newArrayList();
	// 货物刷新记录时间
	private int rtime;
	
	public ShopBuilding(BType type,BbuildingPo templet) {
		super(type,templet);
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
		buf.writeByte( goods.size() );
		for( AwardInfo g : goods ){
			g.buildTransformStream(buf);
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
			goods.add( new AwardInfo(buf) );
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
		goods.clear();
		Build_shopPo templet = CsvGen.getBuild_shopPo(getNid());
		if( templet == null || templet.content.isEmpty() ) return;
		
		int size = Random.get(templet.minsize, templet.maxsize);
		if( size == 0 ) return;
		// 先获取随机池子
		List<AwardInfo> pool = Lists.newArrayList();
		String[] array = templet.content.split("\\|");
		for( String str : array ){
			pool.add( new AwardInfo( str.split(";") ) );
		}
		if( size > pool.size() )
			size = pool.size();
		// 开始随机
		while( goods.size() < size ){
			int idx = Random.get(0, pool.size()-1);
			goods.add( pool.remove(idx) );
		}
	}
	
	/**
	 * 获取下次刷新时间
	 * @return
	 */
	public int getNextUpdateTime(){
		return LXConstants.BUILD_SHOP_UPDATETIME[updatelevel-1] + rtime;
	}

	public byte getUpdatelevel() {
		return updatelevel;
	}
	public void setUpdatelevel(byte updatelevel) {
		this.updatelevel = updatelevel;
	}
	public byte getGoodqlevel() {
		return goodqlevel;
	}
	public void setGoodqlevel(byte goodqlevel) {
		this.goodqlevel = goodqlevel;
	}
	public List<AwardInfo> getGoods() {
		return goods;
	}
	public int getRtime() {
		return rtime;
	}
	public void setRtime(int rtime) {
		this.rtime = rtime;
	}
	public byte getGoodclevel() {
		return goodclevel;
	}
	public void setGoodclevel(byte goodclevel) {
		this.goodclevel = goodclevel;
	}
	
}
