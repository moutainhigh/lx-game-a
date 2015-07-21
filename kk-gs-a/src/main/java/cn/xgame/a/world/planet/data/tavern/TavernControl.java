package cn.xgame.a.world.planet.data.tavern;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.system.Constants;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Taverndata;
import cn.xgame.utils.Logs;

/**
 * 酒馆操作中心
 * @author deng		
 * @date 2015-7-16 上午10:08:14
 */
public class TavernControl implements ITransformStream{

	private final List<Taverndata> csv = CsvGen.taverndatas;
	
	
	private int rTime = 0;
	private List<TCaptain> datas = Lists.newArrayList();
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( getRtime() );
		buffer.writeByte( datas.size() );
		for( TCaptain tc : datas ){
			tc.buildTransformStream( buffer );
		}
	}

	// 一分钟
	private int getRtime() {
		int t = Constants.TAVERN_UPDATE_TIME - (int) (System.currentTimeMillis()/1000 - rTime);
		return t <= 0 ? 1 : t;
	}

	public TCaptain getTCaptain( int id ){
		for( TCaptain t : datas ){
			if( t.getNid() == id )
				return t;
		}
		return null;
	}
	
	/**
	 * 删除
	 * @param tcap
	 */
	public void remove( TCaptain tcap ) {
		Iterator<TCaptain> iter = datas.iterator();
		while( iter.hasNext() ){
			TCaptain o = iter.next();
			if( o.getNid() == tcap.getNid() ){
				iter.remove();
				break;
			}
		}
	}
	
	/**
	 * 刷新酒馆数据
	 * @param times 刷新个数
	 */
	public void updateTavern( byte times ){
		
		datas.clear();
		
		rTime = (int) (System.currentTimeMillis()/1000);
		
		// 下面随机出舰长
		for( int i = 0; i < times; i++ ){
			TCaptain x = getRandomCap();
			if( x != null )
				datas.add( x );
		}
		
		Logs.debug( "更新酒馆 " + datas );
	}
	

	private TCaptain getRandomCap(  ){
		
		int max = 0;
		for( Taverndata tavern : csv ){
			if( getTCaptain( tavern.id ) != null )
				continue;
			max += tavern.rand;
		}
		int rand = Random.get( 0, max );
		for( Taverndata tavern : csv ){
			if( getTCaptain( tavern.id ) != null )
				continue;
			if( tavern.rand >= rand ){
				return new TCaptain( tavern.id );
			}
			rand -= tavern.rand;
		}
		
		return null;
	}


	
}
