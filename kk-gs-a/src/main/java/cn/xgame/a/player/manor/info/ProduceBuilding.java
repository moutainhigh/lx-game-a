package cn.xgame.a.player.manor.info;


import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.Build_producePo;
import cn.xgame.utils.Logs;

/**
 *  产出建筑
 * @author deng		
 * @date 2015-10-15 上午11:55:33
 */
public class ProduceBuilding extends IBuilding{
	
	// 产出列表模板
	private List<Goods> produceTemplet = Lists.newArrayList();
	// 每次产出个数
	private float oneProduce;
	// 产出时间间隔
	private int interval;
	// 总比例
	private float sumScale;
	// 可以产出的总量
	private int sumProduce;
	
	
	// 产出列表
	private List<Goods> produces = Lists.newArrayList();
	// 产出记录时间
	private int rtime = 0;
	
	
	public ProduceBuilding( BType type,BbuildingPo templet ) {
		super(type,templet);
		initProduceTemplet();
	}
	private void initProduceTemplet() {
		Build_producePo templet = CsvGen.getBuild_producePo(getNid());
		if( templet == null ) return;
		sumProduce 	= templet.ram;
		interval 	= templet.producttime;
		oneProduce	= templet.ProduceValue;
		String[] array = templet.ProduceTpye.split("\\|");
		for( String str : array ){
			String[] x = str.split(";");
			Goods g = new Goods();
			g.setId( Integer.parseInt( x[0] ) );
			g.addCount( Float.parseFloat( x[1] ) );
			produceTemplet.add(g);
			sumScale += g.getCount();
		}
	}
	
	@Override
	public void init() {
		produces.clear();
		rtime = (int)(System.currentTimeMillis()/1000);
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBuffer(buf);
		buf.writeByte( produces.size() );
		for( Goods g : produces ){
			buf.writeInt( g.getId() );
			buf.writeFloat( g.getCount() );
		}
		buf.writeInt(rtime);
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
		rtime = buf.readInt();
	}
	
	public void update() {
		int curSumCount = curSumCount();
		if( curSumCount >= sumProduce )
			return;
		
		// 根据已过去的时间算出 次数
		int past 	= (int)(System.currentTimeMillis()/1000) - rtime;
		int times	= past/interval;
		if( times < 1 )
			return;
		
		// 根据次数算出总个数
		float sumCount = times * oneProduce;
		// 这里把超出的减掉
		if( sumCount + curSumCount > sumProduce )
			sumCount = sumProduce - curSumCount;
		// 算出单个比例
		float single = sumCount/sumScale; 
		// 开始加到每个道具上面
		for( Goods goods : produceTemplet ){
			float count = goods.getCount()*single;
			addGoods( goods.getId(), count );
		}
		
		// 最后记录时间
		rtime = (int)(System.currentTimeMillis()/1000);
		
		Logs.debug( "当前建筑("+getNid()+","+getIndex()+")总产出" + produces );
	}
	
	/** 获取当前总数量 */
	private int curSumCount() {
		int ret = 0;
		for( Goods g : produces )
			ret += g.getCount();
		return ret;
	}
	
	private void addGoods( int id, float count ){
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
		if( curSumCount() >= sumProduce && !ret.isEmpty() )
			rtime = (int)(System.currentTimeMillis()/1000);
		return ret;
	}

	public boolean isEmpty() {
		return produces.isEmpty();
	}
	
	public List<Goods> getProduces(){
		return produces;
	}
	
}
