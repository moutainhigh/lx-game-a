package cn.xgame.a.world.planet.data.exchange;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import x.javaplus.collections.Lists;
import x.javaplus.collections.Maps;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.utils.Logs;

/**
 * 交易所 操作中心
 * @author deng		
 * @date 2015-8-30 下午2:19:40
 */
public class ExchangeControl implements IArrayStream{

	// 一页最大个数
	private final static int MAX_NUM = 20;
	private final int SNID;
	
	// 物品唯一ID
	private int GOODS_UID = 0;
	
	// 交易所 物品集合
	private Map<PropType, List<ExchGoods>> goodsSet = Maps.newHashMap();
	
	
	public ExchangeControl( int id ) {
		SNID = id;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		goodsSet.clear();
		GOODS_UID	= 0;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		int size = buf.readInt();
		for( int i = 0; i < size; i++ ){
			ExchGoods g = new ExchGoods();
			g.wrapBuffer(buf);
			if( g.getUid() > GOODS_UID ) 
				GOODS_UID = g.getUid() ;
		}
	}

	@Override
	public byte[] toBytes() {
		if( goodsSet.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		List<ExchGoods> ls = getAll();
		buf.writeInt( ls.size() );
		for( ExchGoods g : ls ){
			g.putBuffer(buf);
		}
		return buf.array();
	}
	
	private List<ExchGoods> getAll() {
		List<ExchGoods> ret = Lists.newArrayList();
		for( List<ExchGoods> gls : goodsSet.values() ){
			ret.addAll(gls);
		}
		return ret;
	}

	private List<ExchGoods> getEGoodsList( PropType type ){
		return goodsSet.get(type);
	}
	
	private void putExchGoods( ExchGoods g ) {
		List<ExchGoods> list = getEGoodsList( g.getProp().type() );
		if( list == null ){
			list = Lists.newArrayList(g);
			goodsSet.put( g.getProp().type(), list );
		}else{
			list.add(g);
		}
	}
	
	private int generateUID() {
		return ++GOODS_UID;
	}
	
	private int getMaxpage( PropType type ) {
		List<ExchGoods> list = getEGoodsList(type);
		return list == null ? 0 : list.size()/MAX_NUM + 1;
	}
	
	/**
	 * 根据唯一ID获取物品
	 * @param uid
	 * @return
	 */
	public ExchGoods getGoods( int uid ) {
		for( List<ExchGoods> gls : goodsSet.values() ){
			for( ExchGoods g : gls ){
				if( g.getUid() == uid )
					return g;
			}
		}
		return null;
	}

	/**
	 * 删除物品
	 * @param goods
	 */
	public void removeGoods( ExchGoods g ) {
		List<ExchGoods> list = getEGoodsList( g.getProp().type() );
		Iterator<ExchGoods> iter = list.iterator();
		while( iter.hasNext() ){
			if( iter.next().getUid() == g.getUid() ){
				iter.remove();
				return;
			}
		}
	}
	
	/**
	 * 根据页数获取物品列表
	 * @param page
	 * @return
	 */
	public List<ExchGoods> getGoodsByPage( PropType type, int page ) {
		List<ExchGoods> ret = Lists.newArrayList();
		int maxPage 		= getMaxpage( type );
		if( maxPage == 0 ) return ret;
		int curPage 		= page > maxPage ? maxPage : page;
		curPage				= curPage <= 0 ? 1 : curPage;
		List<ExchGoods> g 	= getEGoodsList(type);
		for( int i = (page-1)*MAX_NUM; i < MAX_NUM && i < g.size(); i++ ){
			ret.add( g.get(i) );
		}
		Logs.debug( "星球" + SNID + " 交易所" + ret );
		return ret;
	}

	/**
	 * 根据玩家获取物品列表
	 * @param player 
	 * @param page
	 * @return
	 */
	public List<ExchGoods> getGoodsByPlayer( Player player, int page ) {
		List<ExchGoods> ret = Lists.newArrayList();
		for( List<ExchGoods> gls : goodsSet.values() ){
			for( ExchGoods g : gls ){
				if( g.getSellUid().equals( player.getUID() ) )
					ret.add(g);
			}
		}
		return ret;
	}
	
	/**
	 * 上架
	 * @param player
	 * @param newPorp
	 * @param count
	 * @param price
	 * @return 
	 */
	public int added( Player player, IProp newPorp, int count, int price ) {
		newPorp.setUid( generateUID() );
		newPorp.setCount( count );
		ExchGoods g = new ExchGoods();
		g.setSellUid( player.getUID() );
		g.setSellName( player.getNickname() );
		g.setUnitprice( price );
		g.setProp( newPorp );
		
		putExchGoods( g );
		return newPorp.getUid();
	}


	
	
}
