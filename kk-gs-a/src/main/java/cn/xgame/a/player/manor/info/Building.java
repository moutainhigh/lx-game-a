package cn.xgame.a.player.manor.info;


import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;

import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.system.LXConstants;
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
		setEndtime( (int) (System.currentTimeMillis()/1000 + LXConstants.BUILDING_PRODUCE_TIME) );
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
	 * 更新产出
	 */
	public void update() {
		int past 	= (int)(System.currentTimeMillis()/1000) - getEndtime();
		// 根据已过去的时间算出 次数
		int times	= past/LXConstants.BUILDING_PRODUCE_TIME + 1;
		// 根据次数算出总个数
		float count = times * templet().ProduceValue;
		// 通过lua算出产出
		Lua lua = LuaUtil.getGameData();
		lua.getField( "manorBuildingProduce" ).call( 0, this, count, getSumScale(), getProduceTemplet() );
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
