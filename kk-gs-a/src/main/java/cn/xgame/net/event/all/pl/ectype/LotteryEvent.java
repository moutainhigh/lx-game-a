package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;

import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.fleet.classes.LotteryInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.net.event.IEvent;

/**
 * 抽奖
 * @author deng		
 * @date 2015-11-3 下午7:05:58
 */
public class LotteryEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		byte fid = data.readByte();// 出击舰队ID

		ErrorCode code = null;
		int isNext = 0;
		List<AwardInfo> awards = Lists.newArrayList();
		List<IProp> ret = Lists.newArrayList();
		byte type = 0;
		try {
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 看是否有奖
			if( fleet.getLotterys().isEmpty() )
				throw new Exception( ErrorCode.NOT_LOTTERY.name() );
			
			// 获取奖品信息
			LotteryInfo lottery = fleet.getLotterys().remove(0);
			ChapterPo chapter = CsvGen.getChapterPo( lottery.getChapterId() );
			if( chapter == null )
				throw new Exception( ErrorCode.NOT_LOTTERY.name() );

			type = (byte) lottery.getType();
			// 获取奖品池
			List<AwardInfo> drops = getAward( lottery.getType() == 1 ? chapter.silverpond : chapter.goldenpond );
			if( drops.isEmpty() )
				throw new Exception( ErrorCode.NOT_LOTTERY.name() );
			
			// 获取奖品列表
			while( awards.size() < lottery.getCount() ){
				int index = Random.get( 0, drops.size()-1 );
				awards.add( drops.remove(index) );
				if( drops.isEmpty() )
					break;
			}
			
			// 发放奖品
			StarDepot depot = player.getDepots( fleet.getBerthSnid() );
			for( AwardInfo award : awards ){
				ret.addAll( depot.appendProp( award.getId(), award.getCount() ) );
			}
			
			// 最后判断是否还可以抽奖
			isNext = fleet.getLotterys().isEmpty() ? 0 : 1 ;
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			buffer.writeByte( type );
			buffer.writeByte( awards.size() );
			for( AwardInfo award : awards )
				award.buildTransformStream(buffer);
			// 更新道具
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
			buffer.writeByte( isNext );
		}
		sendPackage( player.getCtx(), buffer );
	}

	/**
	 * 获取奖池
	 * @param string
	 * @return
	 */
	private List<AwardInfo> getAward( String string ) {
		List<AwardInfo> ret = Lists.newArrayList();
		if( string.isEmpty() )
			return ret;
		String[] str = string.split( "\\|" );
		for( String x : str ){
			String[] o = x.split(";");
			ret.add( new AwardInfo( Integer.parseInt(o[0]), Integer.parseInt(o[1]) ) );
		}
		return ret;
	}

}
