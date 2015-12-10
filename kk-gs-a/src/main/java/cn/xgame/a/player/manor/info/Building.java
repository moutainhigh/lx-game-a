package cn.xgame.a.player.manor.info;


import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;

import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 一个领地 建筑
 * @author deng		
 * @date 2015-10-15 上午11:55:33
 */
public class Building extends IBuilding{

	// 产出列表
	private List<Goods> produces = Lists.newArrayList();
	
	public Building( int id ) {
		super(id);
		setType( BuildingType.INSERVICE );
	}
	
	public Building( IBuilding o ) {
		super( o.templet().id );
		setIndex( o.getIndex() );
		setType( BuildingType.INSERVICE );
		setEndtime( (int) (System.currentTimeMillis()/1000) );
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBuffer(buf);
		buf.writeByte( produces.size() );
		for( Goods g : produces ){
			buf.writeInt( g.getId() );
			buf.writeFloat( g.getCount() );
		}
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBuffer(buf);
		produces.clear();
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			Goods g = new Goods();
			g.setId( buf.readInt() );
			g.addCount( buf.readFloat() );
			produces.add(g);
		}
	}
	
	/**
	 * 塞入产出 - 前端
	 * @param buffer
	 */
	public void putProduces( ByteBuf buffer ) {
		buffer.writeByte( produces.size() );
		for( Goods goods : produces ){
			buffer.writeInt( goods.getId() );
			buffer.writeInt( (int) goods.getCount() );
		}
	}
	
	public void addGoods( int id, float count ){
		Goods goods = getGoods( id );
		if( goods == null ){
			goods = new Goods();
			goods.setId(id);
			produces.add(goods);
		}
		goods.addCount( count );
	}
	
	private Goods getGoods(int id) {
		for( Goods goods : produces ){
			if( goods.getId() == id )
				return goods;
		}
		return null;
	}
	
	/**
	 * 获取当前总数量
	 * @return
	 */
	private int curSumCount() {
		int ret = 0;
		for( Goods g : produces )
			ret += g.getCount();
		return ret;
	}
	
	/*
  	 * 更新产出
	 */
	public void update() {
		
		int curCount = curSumCount();
		if( curCount >= templet().ram )
			return;
		
		int past 	= (int)(System.currentTimeMillis()/1000) - getEndtime();
		// 根据已过去的时间算出 次数
		int times	= past/templet().producttime;
		if( times < 1 )
			return;
		
		// 根据次数算出总个数
		float count = times * templet().ProduceValue;
		// 这里如果超出总容量 那么减掉多余的
		count		= curCount+count > templet().ram ? templet().ram-curCount : count;
		// 通过lua算出产出
		Lua lua 	= LuaUtil.getGameData();
		lua.getField( "manorBuildingProduce" ).call( 0, this, count, getSumScale(), getProduceTemplet() );
		
		setEndtime( (int)(System.currentTimeMillis()/1000) );
		Logs.debug( "当前建筑总产出" + produces );
	}

	/**
	 * 扣除资源 
	 * @param goods
	 * @return 真真扣除的资源
	 */
	public List<Goods> deductGoods( List<Goods> goods ) {
		List<Goods> ret = Lists.newArrayList();
		for( Goods g : goods ){
			Goods o = getGoods( g.getId() );
			if( o == null ) continue;
			Goods x = new Goods();
			x.setId( g.getId() );
			if( o.getCount() >= g.getCount() ){
				x.addCount( g.getCount() );
				o.addCount( -g.getCount() );
			}else{
				x.addCount( o.getCount() );
				o.clear();
			}
			if( x.getCount() != 0 )
				ret.add( x );
		}
		// 如果收取的时候 是满的 那么就要重新计时
		if( curSumCount() >= templet().ram && !ret.isEmpty() )
			setEndtime( (int)(System.currentTimeMillis()/1000) );
		return ret;
	}

	@Override
	public void inUpgrade() {
		super.inUpgrade();
		// 这里要把产出全部删除掉
		produces.clear();
	}
	
	@Override
	public void inDestroy() {
		super.inDestroy();
		// 这里要把产出全部删除掉
		produces.clear();
	}


	
}
